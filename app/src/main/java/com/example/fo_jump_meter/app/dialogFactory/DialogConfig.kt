package com.example.fo_jump_meter.app.dialogFactory

data class DialogConfig(
    val mainText: String,
    val hasTextField: Boolean = false,
    val textFieldShadowText: String = "",
    val baseTextState: String = "",
    val isNumerical: Boolean = false,
    val onConfirm: (String?) -> Unit,
    val onDismiss: () -> Unit
)
