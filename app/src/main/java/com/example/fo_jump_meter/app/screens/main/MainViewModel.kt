package com.example.fo_jump_meter.app.screens.main

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.example.fo_jump_meter.app.repositories.JumpRepository
import com.example.fo_jump_meter.app.repositories.SensorsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel(), KoinComponent {

    private val sensorsRepository: SensorsRepository by inject()
    private val jumpsRepository: JumpRepository by inject()

//    private val _acceleratorFlow = MutableStateFlow([type])
//    val acceleratorFlow = StateFlow<[type]> get() = _acceleratorFlow
    //TODO replace [type]
//    private val _gyroscopeFlow = MutableStateFlow([type])
//    val gyroscopeFlow = StateFlow<[type]> get() = _gyroscopeFlow

    private val _isSensorsServiceOn = MutableStateFlow<Boolean>(false)
    val isSensorsServiceOn: StateFlow<Boolean> get() = _isSensorsServiceOn


    fun onEvent(event: MainScreenEvent){
        when(event) {
            MainScreenEvent.SaveJump -> TODO()
            MainScreenEvent.StartJumpMeter -> TODO()
        }
    }
    //TODO
}