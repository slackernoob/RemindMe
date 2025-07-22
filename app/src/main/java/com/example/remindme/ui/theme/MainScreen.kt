package com.example.remindme.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.remindme.data.Task
import com.example.remindme.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    // Date picker state
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Task Name") })
        OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description (Optional)") })

        // Date Due Button
        Button(onClick = { showDatePicker = true }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Select Due Date")
        }

        // Show selected date text
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
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Add Task")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tasks.size) { index ->
                val task = tasks[index]
                TaskCard(task = task, onDelete = { viewModel.deleteTask(it) })
            }
        }
    }
}

@Composable
fun TaskCard(task: Task, onDelete: (Task) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(task.name, style = MaterialTheme.typography.titleMedium)
            Text(task.description ?: " ", style = MaterialTheme.typography.bodyMedium)

            val formattedDate = if (task.dateDue != -1L) {
                java.text.SimpleDateFormat("EEE, dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date(task.dateDue))
            } else {
                "No due date set"
            }

            Text(formattedDate, style = MaterialTheme.typography.bodyMedium)
            Row {
                Button(onClick = { onDelete(task) }) {
                    Text("Delete")
                }
            }
        }
    }
}
