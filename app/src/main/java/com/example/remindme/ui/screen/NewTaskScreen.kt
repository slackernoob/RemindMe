package com.example.remindme.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.remindme.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: TaskViewModel) {
//    val tasks by viewModel.tasks.collectAsState()
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
//    var editingTask by remember { mutableStateOf<Task?>(null) }

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
            modifier = Modifier.fillMaxWidth()
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

        Spacer(modifier = Modifier.height(16.dp))
    }
}
