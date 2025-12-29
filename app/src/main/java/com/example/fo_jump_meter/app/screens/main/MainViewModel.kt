package com.example.fo_jump_meter.app.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fo_jump_meter.app.database.types.Jump
import com.example.fo_jump_meter.app.database.types.Snapshot
import com.example.fo_jump_meter.app.dialogFactory.DialogConfig
import com.example.fo_jump_meter.app.dialogFactory.DialogConfigState
import com.example.fo_jump_meter.app.dialogFactory.DialogFactory
import com.example.fo_jump_meter.app.jump.JumpCalculator
import com.example.fo_jump_meter.app.repositories.JumpRepository
import com.example.fo_jump_meter.app.repositories.SensorsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    sensorsRepository: SensorsRepository,
    private val jumpsRepository: JumpRepository
) : ViewModel() {



    private val _isSensorsServiceOn = MutableStateFlow(false)
    val isSensorsServiceOn: StateFlow<Boolean> get() = _isSensorsServiceOn

    private val _isCountdownRunning = MutableStateFlow(false)
    val isCountdownRunning: StateFlow<Boolean> get() = _isCountdownRunning

    private val jumpCalculator = JumpCalculator{
        onEvent(MainScreenEvent.SaveJump)
    }
    private val _jumpDataFlow = MutableStateFlow(floatArrayOf(0f, 0f, 0f))
    val jumpDataFlow: StateFlow<FloatArray> get() = _jumpDataFlow

    private var snapshots = mutableListOf<Snapshot>()

    //dialog
    private val dialogFactory = DialogFactory()
    private val _isStopJumpDialogOpen = MutableStateFlow(false)
    val isStopJumpDialogOpen: StateFlow<Boolean> get() = _isStopJumpDialogOpen
    val stopJumpDialogConfig: DialogConfig? =
        dialogFactory.create(
            state = DialogConfigState.InputJumpName,
            onConfirm = { name -> saveJump(name!!) },
            onDismiss = { _isStopJumpDialogOpen.value = false }
        )

    // timer
    private var startTime: Long? = null
    private val _remainingTime = MutableStateFlow<Long>(5)
    val remainingTime: StateFlow<Long> get() = _remainingTime

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    private var timerJob: Job? = null

    private fun startTimer() {
        startTime = System.currentTimeMillis()
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                val elapsedSeconds = (System.currentTimeMillis() - startTime!!) / 1000
                val timeLeft = 5 - elapsedSeconds
                _remainingTime.value = timeLeft

                if (timeLeft <= 0) {
                    break
                }
                delay(1000)
            }
            _events.emit(UiEvent.PlaySound)
            delay(500)
            _isCountdownRunning.value = false
            startJump()
            stopTimer()
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        startTime = null
        _remainingTime.value = 5
    }

    private fun startJump() {
        _isSensorsServiceOn.value = true
        jumpCalculator.reset()
        snapshots.clear()
    }

    private fun saveJump(name: String) {
        viewModelScope.launch {
            val jump = Jump(0,0.0,0,0, name, 0)
            jumpsRepository.saveJumpWithSnapshots(jump, snapshots)
        }
    }


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
                _isStopJumpDialogOpen.value = true
            }
            MainScreenEvent.StartJumpMeter -> {
                _isCountdownRunning.value = true
                startTimer()
            }
        }
    }
}

sealed class UiEvent {
    data object PlaySound : UiEvent()
}