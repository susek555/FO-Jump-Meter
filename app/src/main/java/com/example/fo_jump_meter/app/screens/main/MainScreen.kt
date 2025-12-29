package com.example.fo_jump_meter.app.screens.main

import android.media.MediaPlayer
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fo_jump_meter.R
import java.util.Locale

@Composable
fun MainScreen(
    startSensorsService: () -> Unit,
    stopSensorsService: () -> Unit,
    displayRecordsScreen: () -> Unit,
    viewModel: MainViewModel
) {
    val isServiceRunning by viewModel.isSensorsServiceOn.collectAsState()
    val jumpData by viewModel.jumpDataFlow.collectAsState()
    val isCountdownRunning by viewModel.isCountdownRunning.collectAsState()
    val remainingTime by viewModel.remainingTime.collectAsState()

    //sound
    val context = LocalContext.current
    val player = remember {
        MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI)
    }
    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                UiEvent.PlaySound -> {
                    if (player.isPlaying) player.seekTo(0)
                    player.start()
                }
            }
        }
    }

    LaunchedEffect(isServiceRunning) {
        if(isServiceRunning) {
            startSensorsService()
        } else {
            stopSensorsService()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Button(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = displayRecordsScreen
        ){
            Text("My records")
        }

        if (isCountdownRunning) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Prepare yourself and the phone",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Jump starts in...",
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = remainingTime.toString(),
                    style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
                )
            }
        } else if (isServiceRunning) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DataDisplayItem(
                    label = "HEIGHT",
                    value = jumpData[0],
                    unit = "m",
                    isMainMetric = true
                )

                Spacer(modifier = Modifier.height(30.dp))

                DataDisplayItem(
                    label = "VELOCITY",
                    value = jumpData[1],
                    unit = "m/s"
                )

                Spacer(modifier = Modifier.height(20.dp))

                DataDisplayItem(
                    label = "ACCELERATION",
                    value = jumpData[2],
                    unit = "m/s²"
                )
            }

            StopButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 50.dp),
                onClick = {
                    viewModel.onEvent(MainScreenEvent.SaveJump)
                }
            )
        } else {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ready to Jump?",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Click button below to start measurement",
                    color = Color.Gray
                )
            }

            StartButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 50.dp),
                onClick = {
                    viewModel.onEvent(MainScreenEvent.StartJumpMeter)
                }
            )
        }
    }
}

@Composable
fun DataDisplayItem(
    label: String,
    value: Float,
    unit: String,
    isMainMetric: Boolean = false
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = TextStyle(
                fontWeight = FontWeight.Light,
                fontSize = if (isMainMetric) 20.sp else 14.sp,
                color = Color.Gray
            )
        )
        Text(
            text = String.format(Locale.US, "%.2f %s", value, unit),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = if (isMainMetric) 48.sp else 24.sp
            )
        )
    }
}