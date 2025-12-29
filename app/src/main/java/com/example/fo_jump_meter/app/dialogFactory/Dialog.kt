package com.example.fo_jump_meter.app.dialogFactory

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun Dialog(
    config: DialogConfig
){
    var inputText by remember { mutableStateOf(config.baseTextState) }

    AlertDialog(
        onDismissRequest = config.onDismiss,
        title = {
            Text(text = config.mainText)
        },
        text = {
            if (config.hasTextField) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text(config.textFieldShadowText) },
                    keyboardOptions = KeyboardOptions(
                    keyboardType = if (config.isNumerical)
                        KeyboardType.Number
                    else
                        KeyboardType.Text
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    config.onConfirm(inputText)
                },
                enabled = (inputText.isNotEmpty() || !config.hasTextField)
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = config.onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}