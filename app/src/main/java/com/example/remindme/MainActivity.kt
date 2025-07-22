package com.example.remindme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remindme.data.TaskDatabase
import com.example.remindme.ui.MainScreen
import com.example.remindme.viewmodel.TaskViewModel
import com.example.remindme.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = TaskDatabase.getDatabase(applicationContext)
        val factory = TaskViewModelFactory(db.taskDao())

        setContent {
            val viewModel: TaskViewModel = viewModel(factory = factory)
            MainScreen(viewModel)
        }
    }
}