package com.example.fo_jump_meter.app.screens.main

sealed interface MainScreenEvent {
    data object StartJumpMeter: MainScreenEvent
    data object CancelJumpMeter: MainScreenEvent
    data class SaveJump(val name: String): MainScreenEvent
}