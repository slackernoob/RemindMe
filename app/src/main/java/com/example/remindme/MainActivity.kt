package com.example.remindme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.remindme.data.TaskDatabase
import com.example.remindme.viewmodel.TaskViewModel
import com.example.remindme.viewmodel.TaskViewModelFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = TaskDatabase.getDatabase(applicationContext)
        val factory = TaskViewModelFactory(db.taskDao())

        setContent {
//            val viewModel: TaskViewModel = viewModel(factory = factory)
            val viewModel: TaskViewModel = hiltViewModel()
            val navController = rememberNavController()
            RemindMeApp(navController, viewModel)
        }
    }
}