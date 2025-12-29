package com.example.fo_jump_meter.app.dialogFactory

sealed class DialogConfigState {
    data object None: DialogConfigState()
    data object InputJumpName: DialogConfigState()
    data object InputWeight: DialogConfigState()
}