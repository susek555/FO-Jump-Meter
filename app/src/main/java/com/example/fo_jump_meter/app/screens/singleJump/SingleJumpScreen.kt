package com.example.fo_jump_meter.app.screens.singleJump

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleJumpScreen(
    displayRecordsScreen: () -> Unit,
    viewModel: SingleJumpViewModel
){
    val jump by viewModel.jump.collectAsState()
    val chartPoints by viewModel.chartPoints.collectAsState()
    val selectedType by viewModel.chartType.collectAsState()
    val types = ChartType.entries

    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Jump") },
                navigationIcon = {
                    IconButton(onClick = displayRecordsScreen) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Main screen"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (jump == null) {
            CircularProgressIndicator()
        } else {
            Column(
                modifier = Modifier.padding(innerPadding).fillMaxWidth().padding(16.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Text(
                    text = formatter.format(jump!!.date),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Height,
                        contentDescription = "Height",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "%.2fm".format(jump!!.height),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(24.dp))
                    Icon(
                        imageVector = Icons.Default.Timelapse,
                        contentDescription = "Timelapse",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "%.3fs".format(jump!!.airTime / 1000.0),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                TabRow(selectedTabIndex = selectedType.ordinal) {
                    types.forEach { type ->
                        Tab(
                            selected = type == selectedType,
                            onClick = { viewModel.changeChartType(type) },
                            text = { Text(text = type.name) },
                        )
                    }
                }
                JumpChart(
                    points = chartPoints,
                    modifier = Modifier.fillMaxWidth().height(300.dp)
                )
            }
        }
    }
}
