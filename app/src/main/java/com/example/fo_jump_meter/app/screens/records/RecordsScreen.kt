package com.example.fo_jump_meter.app.screens.records

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat

@Composable
fun RecordsScreen(
    displayMainScreen: () -> Unit,
//    displaySingleJumpScreen: () -> Unit,
    viewModel: RecordsViewModel
){
    val jumps by viewModel.jumps.collectAsState()
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
    Column  {
        Text(text = "Records")
        LazyColumn {
            items(jumps) { jump ->
                Card(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val dateString = formatter.format(jump.date)
                        Text(text = "Date: $dateString")
                        Row() {
                            Text(text = "Height: %.2fm".format(jump.height))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Air time: %.3fs".format(jump.airTime / 1000.0))
                        }
                    }
                }
            }
        }
    }
}