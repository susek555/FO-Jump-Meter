package com.example.fo_jump_meter.app.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fo_jump_meter.app.database.types.Jump
import com.example.fo_jump_meter.app.database.types.Snapshot
import com.example.fo_jump_meter.app.jump.JumpCalculator
import com.example.fo_jump_meter.app.repositories.JumpRepository
import com.example.fo_jump_meter.app.repositories.SensorsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    sensorsRepository: SensorsRepository,
    private val jumpsRepository: JumpRepository
) : ViewModel() {



    private val _isSensorsServiceOn = MutableStateFlow(false)
    val isSensorsServiceOn: StateFlow<Boolean> get() = _isSensorsServiceOn

    private val jumpCalculator = JumpCalculator{
        onEvent(MainScreenEvent.SaveJump)
    }
    private val _jumpDataFlow = MutableStateFlow(floatArrayOf(0f, 0f, 0f))
    val jumpDataFlow: StateFlow<FloatArray> get() = _jumpDataFlow

    var snapshots = mutableListOf<Snapshot>()


    init {
        combine(
            sensorsRepository.accelerometerFlow,
            sensorsRepository.rotationVectorFlow
        ) { accData, rotData ->
            if (accData.values.isNotEmpty() && rotData.values.isNotEmpty()) {
                jumpCalculator.calculate(
                    accData.values,
                    rotData.values,
                    accData.timestamp
                )
            } else {
                floatArrayOf(0f, 0f, 0f)
            }
        }
            .onEach { result ->
                if (_isSensorsServiceOn.value) {
                    snapshots += Snapshot(
                        height = result[0],
                        velocity = result[1],
                        acceleration = result[2],
                        timestamp = System.currentTimeMillis(),
                        jumpId = 0
                    )
                    _jumpDataFlow.emit(result)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: MainScreenEvent){
        when(event) {
            MainScreenEvent.SaveJump -> {
                _isSensorsServiceOn.value = false
                snapshots += Snapshot(
                    height = 0f,
                    velocity = 0f,
                    acceleration = 0f,
                    timestamp = System.currentTimeMillis(),
                    jumpId = 0
                )
                viewModelScope.launch {
                    val jump = Jump(0,0.0,0,0)
                    jumpsRepository.saveJumpWithSnapshots(jump, snapshots)
                }
            }
            MainScreenEvent.StartJumpMeter -> {
                _isSensorsServiceOn.value = true
                jumpCalculator.reset()
                snapshots.clear()
            }
        }
    }
    //TODO
}