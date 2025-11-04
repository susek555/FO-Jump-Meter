package com.example.fo_jump_meter.app.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fo_jump_meter.ui.theme.FOJumpMeterTheme

@Composable
fun MainScreen(
    startSensorsService: () -> Unit,
    stopSensorsService: () -> Unit,
    displayRecordsScreen: () -> Unit,
    viewModel: MainViewModel
) {
    //TODO
    Greeting(
        name = "Android",
        modifier = Modifier.padding()
    )
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FOJumpMeterTheme {
        Greeting("Android")
    }
}