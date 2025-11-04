package com.example.fo_jump_meter.app.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen(
    startSensorsService: () -> Unit,
    stopSensorsService: () -> Unit,
    displayRecordsScreen: () -> Unit,
    viewModel: MainViewModel
) {
    val isServiceRunning by viewModel.isSensorsServiceOn.collectAsState()

    LaunchedEffect(isServiceRunning) {
        if(isServiceRunning) {
            startSensorsService()
        } else {
            stopSensorsService()
        }
    }

//    val acceleration by viewModel.acceleratorFlow.collectAsState()
//    val orientation by viewModel.gyroscopeFlow.collectAsState()

    val acceleration = 0
    val orientation = 0

    Box(
        modifier = Modifier.fillMaxSize().padding(20.dp)
    ) {
        Button(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 0.dp, y = 20.dp),
            onClick = displayRecordsScreen
        ){
            Text("My records")
        }

        if (isServiceRunning) {
            Text(
                text = "ACCELERATION",
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = 0.dp, y = (-20).dp),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
            Text(
                text = acceleration.toString(),
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = 0.dp, y = 20.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
            Text(
                text = "ORIENTATION",
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = 0.dp, y = (-20).dp),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
            Text(
                text = orientation.toString(),
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = 0.dp, y = 20.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
            StopButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(x = 0.dp, y = (-100).dp),
                onClick = {
                    viewModel.onEvent(MainScreenEvent.SaveJump)
                }
            )
        } else {
            Text(
                text = "Click to measure jump",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(x = 0.dp, y = 60.dp)
            )
            StartButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(x = 0.dp, y = (-100).dp),
                onClick = {
                    viewModel.onEvent(MainScreenEvent.StartJumpMeter)
                }
            )
        }
    }

    //TODO add dialog with info how to measure a jump
}

