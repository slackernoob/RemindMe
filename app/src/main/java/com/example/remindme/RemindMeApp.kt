package com.example.remindme

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.remindme.ui.MainScreen
import com.example.remindme.ui.view.OverviewScreen
import com.example.remindme.ui.view.TaskListScreen
import com.example.remindme.viewmodel.TaskViewModel


@Composable
fun RemindMeApp(navController: NavHostController, viewModel: TaskViewModel) {
    val items = listOf("New Task", "Tasks")
    val routes = listOf("main", "list")
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, label ->
                    val icon = when (label) {
                        "New Task" -> Icons.Default.Add
                        "Tasks" -> Icons.AutoMirrored.Filled.List
                        else -> Icons.AutoMirrored.Filled.List
                    }

                    NavigationBarItem(
                        selected = currentRoute == routes[index],
                        onClick = { navController.navigate(routes[index]) }, // Navigate to selected route
                        label = { Text(label) },
                        icon = { Icon(icon, contentDescription = label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost( // Navigation graph in Compose form
            navController = navController,
            startDestination = "main",
            modifier = Modifier.padding(padding)
        ) {
            // Each composable() call is a screen ->
            // No fragments or XML involved as screens are just Composables
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
}