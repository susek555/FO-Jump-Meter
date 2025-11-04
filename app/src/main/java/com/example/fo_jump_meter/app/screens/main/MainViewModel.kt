package com.example.fo_jump_meter.app.screens.main

import androidx.lifecycle.ViewModel
import com.example.fo_jump_meter.app.repositories.JumpRepository
import com.example.fo_jump_meter.app.repositories.SensorsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel(), KoinComponent {

    private val sensorsRepository: SensorsRepository by inject()
    private val jumpsRepository: JumpRepository by inject()

    fun onEvent(event: MainScreenEvent){
        when(event) {
            MainScreenEvent.CancelJumpMeter -> TODO()
            is MainScreenEvent.SaveJump -> TODO()
            MainScreenEvent.StartJumpMeter -> TODO()
        }
    }
    //TODO
}