package com.example.remindme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.remindme.ui.MainScreen
import com.example.remindme.ui.view.OverviewScreen
import com.example.remindme.ui.view.TaskListScreen
import com.example.remindme.viewmodel.TaskViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
//    viewModel: TaskViewModel,
    modifier: Modifier = Modifier
) {
    val viewModel: TaskViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = "main",
        modifier = modifier
    ) {
        composable("main") {
            MainScreen(
                viewModel,
                onGoToList = { navController.navigate("list") },
                onGoToOverview = { navController.navigate("overview") }
            )
        }
        composable("list") {
            TaskListScreen(
                viewModel,
                onGoToMain = { navController.navigate("main") },
                onGoToOverview = { navController.navigate("overview") }
            )
        }
        composable("overview") {
            OverviewScreen(
                viewModel,
                onGoToList = { navController.navigate("list") },
                onGoToMain = { navController.navigate("main") }
            )
        }
    }
}
