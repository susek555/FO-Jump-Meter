package com.example.fo_jump_meter.app.screens.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun RoundButton(
    modifier: Modifier,
    onClick: () -> Unit,
    longClick: Boolean = false,
    content: @Composable () -> Unit,
    backgroundColor: Color,
    loadingColor: Color = Color.Gray,
    holdDuration: Int = 1000
) {
    var isPressed by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(isPressed) {
        if (isPressed && longClick) {
            progress = 0f
            val startTime = System.currentTimeMillis()
            while (isPressed && progress < 1f) {
                val elapsed = System.currentTimeMillis() - startTime
                progress = (elapsed.toFloat() / holdDuration).coerceIn(0f, 1f)
                delay(16)
            }
            if (progress >= 1f) onClick()
        }
    }

    Box(
        modifier = modifier
            .size(150.dp)
            .background(backgroundColor, CircleShape)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { if (!longClick) onClick() },
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        content()

        if (longClick && isPressed) {
            Canvas(
                modifier = Modifier.size(150.dp)
            ) {
                drawArc(
                    color = loadingColor,
                    startAngle = -90f,
                    sweepAngle = progress * 360f,
                    useCenter = false,
                    style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }
    }
}

@Composable
fun StartButton(
    modifier: Modifier,
    onClick: () -> Unit
) {
    RoundButton(
        onClick = onClick,
        modifier = modifier,
        content = {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Start Jump Meter",
                modifier = Modifier.size(50.dp),
                tint = Color.Black
            )
        },
        backgroundColor = Color.Green
    )
}

@Composable
fun StopButton(
    modifier: Modifier,
    onClick: () -> Unit
) {
    RoundButton(
        onClick = onClick,
        longClick = true,
        modifier = modifier,
        content = {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Stop Jump Meter",
                modifier = Modifier.size(50.dp),
                tint = Color.Black
            )
        },
        backgroundColor = Color.Red
    )
}