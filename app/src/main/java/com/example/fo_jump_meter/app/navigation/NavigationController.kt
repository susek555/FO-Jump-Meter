package com.example.fo_jump_meter.app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fo_jump_meter.app.screens.main.MainScreen
import com.example.fo_jump_meter.app.screens.main.MainViewModel
import com.example.fo_jump_meter.app.screens.records.RecordsScreen
import com.example.fo_jump_meter.app.screens.records.RecordsViewModel
import com.example.fo_jump_meter.app.screens.singleJump.SingleJumpScreen
import com.example.fo_jump_meter.app.screens.singleJump.SingleJumpViewModel

@Composable
fun NavigationController(
    startSensorsService: () -> Unit,
    stopSensorsService: () -> Unit,
    innerPadding: PaddingValues
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "MainScreen",
        modifier = Modifier.padding(innerPadding)
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
                },
                displaySingleJumpScreen = { jump ->
                    navController.navigate("SingleJumpScreen/${jump.id}")
                }
            )
        }

        composable("SingleJumpScreen/{jumpId}", arguments = listOf(navArgument("jumpId") { type =
            NavType.LongType })) { navBackStackEntry ->
            SingleJumpScreen(
                viewModel = hiltViewModel<SingleJumpViewModel>(navBackStackEntry),
                displayRecordsScreen = {
                    navController.navigateUp()
                }
            )
        }
    }

}