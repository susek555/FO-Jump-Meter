package com.example.fo_jump_meter.app.dialogFactory

class DialogFactory {
    fun create(
        state: DialogConfigState,
        onConfirm: (String?) -> Unit,
        onDismiss: () -> Unit,
    ) : DialogConfig? {
        return when(state) {
            DialogConfigState.InputJumpName -> DialogConfig(
                mainText = "Enter name for this jump...",
                hasTextField = true,
                textFieldShadowText = "Name...",
                onConfirm = onConfirm,
                onDismiss = onDismiss
            )
            DialogConfigState.InputWeight -> DialogConfig(
                mainText = "Enter your weight",
                hasTextField = true,
                textFieldShadowText = "Weight...",
                isNumerical = true,
                onConfirm = onConfirm,
                onDismiss = onDismiss
            )
            DialogConfigState.None -> null
        }
    }
}