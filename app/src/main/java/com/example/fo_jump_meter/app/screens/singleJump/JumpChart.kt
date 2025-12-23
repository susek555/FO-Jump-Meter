package com.example.fo_jump_meter.app.screens.singleJump

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun JumpChart(
    points: List<ChartPoint>,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(modifier = modifier) {
        if (points.isEmpty()) return@Canvas

        val padding = 16.dp.toPx()
        val drawableWidth = size.width - 2 * padding
        val drawableHeight = size.height - 2 *padding
        val minX = points.minOf { it.x }
        val maxX = points.maxOf { it.x }
        val minY = points.minOf { it.y }
        val maxY = points.maxOf { it.y }

        val deltaX = if (maxX - minX == 0f) 1f else maxX - minX
        val deltaY = if (maxY - minY == 0f) 1f else maxY - minY

        val path = Path()
        points.forEachIndexed {
            index, point ->
            val x = padding + (point.x - minX) / deltaX * drawableWidth
            val y = padding + (drawableHeight - (point.y - minY) / deltaY * drawableHeight)
            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

    }
}