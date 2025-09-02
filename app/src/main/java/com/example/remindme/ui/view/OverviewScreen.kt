package com.example.remindme.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.remindme.ui.viewmodel.TaskViewModel


@Composable
fun OverviewScreen(
    viewModel: TaskViewModel = hiltViewModel(),
    onGoToList: () -> Unit,
    onGoToMain: () -> Unit
) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Total number of tasks = ${tasks.size}",
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onGoToList) {
            Text("Go to List")
        }
        Button(onClick = onGoToMain) {
            Text("Go to Main")
        }


    }
}