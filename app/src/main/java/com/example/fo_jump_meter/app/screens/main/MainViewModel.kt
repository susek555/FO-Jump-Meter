package com.example.fo_jump_meter.app.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fo_jump_meter.app.data.SensorData
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
    private val sensorsRepository: SensorsRepository,
    private val jumpsRepository: JumpRepository
) : ViewModel() {

    private val jumpCalculator = JumpCalculator()
    private val _accelerometerFlow = MutableStateFlow(SensorData())

    private val _rotationVectorFlow = MutableStateFlow(SensorData())

    private val _isSensorsServiceOn = MutableStateFlow(false)
    val isSensorsServiceOn: StateFlow<Boolean> get() = _isSensorsServiceOn

    private val _jumpDataFlow = MutableStateFlow(floatArrayOf(0f, 0f, 0f))
    val jumpDataFlow: StateFlow<FloatArray> get() = _jumpDataFlow

    init {
        viewModelScope.launch {
            sensorsRepository.accelerometerFlow.collect { data ->
                _accelerometerFlow.emit(data)
            }
        }
        viewModelScope.launch {
            sensorsRepository.rotationVectorFlow.collect { data ->
                _rotationVectorFlow.emit(data)
            }
        }
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
                _jumpDataFlow.emit(result)
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: MainScreenEvent){
        when(event) {
            MainScreenEvent.SaveJump -> {
                _isSensorsServiceOn.value = false
                //TODO
            }
            MainScreenEvent.StartJumpMeter -> {
                _isSensorsServiceOn.value = true
                //TODO
            }
        }
    }
    //TODO
}