package com.example.fo_jump_meter.app.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fo_jump_meter.app.data.SensorData
import com.example.fo_jump_meter.app.repositories.JumpRepository
import com.example.fo_jump_meter.app.repositories.SensorsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sensorsRepository: SensorsRepository,
    private val jumpsRepository: JumpRepository
) : ViewModel() {


    private val _accelerometerFlow = MutableStateFlow(SensorData())
    val accelerometerFlow: StateFlow<SensorData> get() = _accelerometerFlow

    private val _rotationVectorFlow = MutableStateFlow(SensorData())
    val rotationVectorFlow: StateFlow<SensorData> get() = _rotationVectorFlow

    private val _isSensorsServiceOn = MutableStateFlow<Boolean>(false)
    val isSensorsServiceOn: StateFlow<Boolean> get() = _isSensorsServiceOn

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