package com.example.remindme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import com.example.remindme.ui.view.OverviewScreen
import com.example.remindme.ui.view.TaskListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            // can run here due to context but not in the ResponsiveContainer Screen.
            // dynamic collectAsState
            // updates every recomposition
            val windowInfo by WindowInfoTracker.getOrCreate(this)
                .windowLayoutInfo(this)
                .collectAsState(initial = null)
            if (windowInfo == null) {
                // render nothing
            } else {
                val foldingFeature =
                    windowInfo?.displayFeatures?.find { it is FoldingFeature } as? FoldingFeature

                val navController = rememberNavController()
                ResponsiveContainer(
                    content = {RemindMeApp(navController)},
                    extraContents = listOf(
                        { OverviewScreen(
                            onGoToList = {navController.navigate("list")},
                            onGoToMain = {navController.navigate("main")}
                        ) },
                        {TaskListScreen()}
                    ),
                    foldingFeature = foldingFeature
                )
            }

        }
    }
}