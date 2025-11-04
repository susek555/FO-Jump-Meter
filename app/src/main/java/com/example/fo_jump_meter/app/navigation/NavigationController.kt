package com.example.fo_jump_meter.app.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fo_jump_meter.app.screens.main.MainScreen
import com.example.fo_jump_meter.app.screens.main.MainViewModel
import com.example.fo_jump_meter.app.screens.records.RecordsScreen
import com.example.fo_jump_meter.app.screens.records.RecordsViewModel
import com.example.fo_jump_meter.app.screens.singleJump.SingleJumpScreen
import com.example.fo_jump_meter.app.screens.singleJump.SingleJumpViewModel

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
                viewModel = hiltViewModel<MainViewModel>(navBackStackEntry),
                startSensorsService = startSensorsService,
                stopSensorsService = stopSensorsService,
                displayRecordsScreen = {
                    navController.navigate("RecordsScreen")
                }
            )
        }

        composable("RecordsScreen") { navBackStackEntry ->
            RecordsScreen(
                viewModel = hiltViewModel<RecordsViewModel>(navBackStackEntry),
                displayMainScreen = {
                    navController.navigateUp()
                }
                //TODO pass displaySingleJumpScreen
            )
        }

        composable("SingleJumpScreen") { navBackStackEntry ->
            SingleJumpScreen(
                viewModel = hiltViewModel<SingleJumpViewModel>(navBackStackEntry),
                displayRecordsScreen = {
                    navController.navigateUp()
                }
            )
        }
    }

}