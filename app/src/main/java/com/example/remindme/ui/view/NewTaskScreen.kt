package com.example.remindme.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.remindme.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: TaskViewModel, onGoToList: () -> Unit, onGoToOverview: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    // Date picker state
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Add Task", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Task Name")},
            modifier = Modifier
                .fillMaxWidth()
                .testTag("Task Name Field")
        )
        OutlinedTextField(
            value = desc,
            onValueChange = { desc = it },
            label = { Text("Description (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        // Button to select due date
        Button(onClick = { showDatePicker = true }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Select Due Date")
        }

        // Show selected date
        datePickerState.selectedDateMillis?.let {
            val formatted = java.text.SimpleDateFormat("EEE, dd MMM yyyy").format(it)
            Text("Due Date: $formatted", style = MaterialTheme.typography.bodyMedium)
        }

        // DatePicker Dialog
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        // Add Task Button
        Button(
            modifier = Modifier.testTag("Add Task Button"),
            onClick = {
                if (name.isNotBlank()) {
                    val due = datePickerState.selectedDateMillis ?: -1 // handle -1
                    val now = System.currentTimeMillis()
                    viewModel.addTask(name, desc, dateDue = due, timeDue = now + 3_600_000) // dummy due in 1 day, reminder in 1 hour
                    name = ""
                    desc = ""
                    showDatePicker = false
                }
            },
        ) {
            Text("Add Task")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = onGoToOverview) {
            Text("Go to Overview")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
