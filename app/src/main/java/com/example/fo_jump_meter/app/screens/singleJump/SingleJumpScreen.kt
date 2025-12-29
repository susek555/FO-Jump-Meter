package com.example.fo_jump_meter.app.screens.singleJump

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.Button
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
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fo_jump_meter.app.dialogFactory.Dialog
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleJumpScreen(
    displayRecordsScreen: () -> Unit,
    viewModel: SingleJumpViewModel
){
    val jump by viewModel.jump.collectAsState()
    val chartPoints by viewModel.chartPoints.collectAsState()
    val work by viewModel.work.collectAsState()
    val selectedType by viewModel.chartType.collectAsState()
    val types = ChartType.entries

    //dialog
    val isInputWeightDialogOpen by viewModel.isInputWeightDialogOpen.collectAsState()

    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Jump Details") },
                navigationIcon = {
                    IconButton(onClick = displayRecordsScreen) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (jump == null) {
            Column(
                modifier = Modifier.padding(innerPadding).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- DATE ---
                Text(
                    text = formatter.format(jump!!.date),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(24.dp))

                // --- MAIN STATS (Height & Time) ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Height
                    Icon(
                        imageVector = Icons.Default.Height,
                        contentDescription = "Height",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "%.2fm".format(jump!!.height),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.width(32.dp))

                    // AirTime
                    Icon(
                        imageVector = Icons.Default.Timelapse,
                        contentDescription = "Timelapse",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "%.3fs".format(jump!!.airTime / 1000.0),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- CHART TABS ---
                TabRow(selectedTabIndex = selectedType.ordinal) {
                    types.forEach { type ->
                        Tab(
                            selected = type == selectedType,
                            onClick = { viewModel.changeChartType(type) },
                            text = { Text(text = type.name) },
                        )
                    }
                }

                // --- CHART ---
                JumpChart(
                    points = chartPoints,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(vertical = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // --- SECONDARY STATS (Weight & Work) ---
                if (jump!!.weight.toInt() != 0) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(
                            icon = Icons.Default.FitnessCenter,
                            value = "${jump!!.weight}kg",
                            label = "Weight"
                        )

                        VerticalDivider(
                            modifier = Modifier.height(40.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )

                        StatItem(
                            icon = Icons.Default.Bolt,
                            value = "%.0fJ".format(work ?: 0f),
                            label = "Legs Work"
                        )
                    }
                } else {
                    // --- BUTTON IF NO WEIGHT ---
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Add weight to calculate work",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.inputWeight() }
                        ) {
                            Icon(Icons.Default.FitnessCenter, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Enter weight")
                        }
                    }
                }
            }
        }
    }

    if(isInputWeightDialogOpen){
        Dialog(viewModel.isInputWeightDialogConfig!!)
    }
}

@Composable
private fun StatItem(
    icon: ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}