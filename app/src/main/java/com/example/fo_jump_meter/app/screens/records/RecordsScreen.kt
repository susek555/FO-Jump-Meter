package com.example.fo_jump_meter.app.screens.records

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fo_jump_meter.app.database.types.Jump
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(
    displayMainScreen: () -> Unit,
    displaySingleJumpScreen: (jump: Jump) -> Unit,
    viewModel: RecordsViewModel
){
    val jumps by viewModel.jumps.collectAsState()
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Records") },
                navigationIcon = {
                    IconButton(onClick = displayMainScreen) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Main screen"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (jumps.isEmpty()) {
            Box(
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
                contentAlignment = Alignment.Center
            )
            {
                Text(
                    text = "No jumps yet",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        } else {
            Column(modifier = Modifier.padding(innerPadding))  {
                LazyColumn {
                    items(jumps) { jump ->
                        Card(onClick = {displaySingleJumpScreen(jump)}, modifier = Modifier.padding(8.dp).fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                val dateString = formatter.format(jump.date)
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(
                                        text = dateString,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    IconButton(onClick = { viewModel.deleteJump(jump) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete"
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    verticalAlignment = androidx.compose.ui.Alignment.Bottom
                                ) {
                                    Text(
                                        text = "%.2f".format(jump.height),
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                    )
                                    Text(
                                        text = "m",
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )

                                    Spacer(modifier = Modifier.width(24.dp))
                                    Text(
                                        text = "%.3f".format(jump.airTime / 1000.0),
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Text(
                                        text = "s",
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(bottom = 4.dp),
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}