package com.example.fo_jump_meter.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fo_jump_meter.app.screens.main.MainScreen
import com.example.fo_jump_meter.app.screens.records.RecordsScreen
import com.example.fo_jump_meter.app.screens.singleJump.SingleJumpScreen

@Composable
fun NavigationController(
    startSensorsService: () -> Unit,
    stopSensorsService: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "MainScreen"
    ){
        composable("MainScreen") { navBackStackEntry ->
            MainScreen(
                //TODO pass params
            )
        }

        composable("RecordsScreen") { navBackStackEntry ->
            RecordsScreen(
                //TODO pass params
            )
        }

        composable("SingleJumpScreen") { navBackStackEntry ->
            SingleJumpScreen(
                //TODO pass params
            )
        }
    }

}