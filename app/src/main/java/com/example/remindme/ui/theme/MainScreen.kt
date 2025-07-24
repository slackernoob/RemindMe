package com.example.remindme.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    var editingTask by remember { mutableStateOf<Task?>(null) }

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
//            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Add Task")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
//
//        LazyColumn {
//            items(tasks.size) { index ->
//                val task = tasks[index]
//                TaskCard(
//                    task = task,
//                    onDelete = { viewModel.deleteTask(it) },
//                    onEdit = { editingTask = it }
//                )
//            }
//        }
//
//        if (editingTask != null) {
//            var editName by remember { mutableStateOf(editingTask!!.name) }
//            var editDesc by remember { mutableStateOf(editingTask!!.description ?: "") }
//            val editDateState = rememberDatePickerState(initialSelectedDateMillis = editingTask!!.dateDue.takeIf { it != -1L })
//
//            AlertDialog(
//                onDismissRequest = { editingTask = null },
//                confirmButton = {
//                    TextButton(onClick = {
//                        val newDue = editDateState.selectedDateMillis ?: -1
//                        viewModel.updateTask(editingTask!!.copy(name = editName, description = editDesc, dateDue = newDue))
//                        editingTask = null
//                    }) {
//                        Text("Save")
//                    }
//                },
//                dismissButton = {
//                    TextButton(onClick = { editingTask = null }) {
//                        Text("Cancel")
//                    }
//                },
//                title = { Text("Edit Task") },
//                text = {
//                    Column {
//                        OutlinedTextField(value = editName, onValueChange = { editName = it }, label = { Text("Task Name") })
//                        OutlinedTextField(value = editDesc, onValueChange = { editDesc = it }, label = { Text("Description") })
//                        DatePicker(state = editDateState)
//                    }
//                }
//            )
//        }
//
//    }
//}
//
//@Composable
//fun TaskCard(task: Task, onDelete: (Task) -> Unit, onEdit: (Task) -> Unit) {
//    Card(
//        modifier = Modifier.fillMaxWidth().padding(4.dp)
//    ) {
//        Column(modifier = Modifier.padding(8.dp)) {
//            Text(task.name, style = MaterialTheme.typography.titleMedium)
//            Text(task.description ?: " ", style = MaterialTheme.typography.bodyMedium)
//
//            val formattedDate = if (task.dateDue != -1L) {
//                java.text.SimpleDateFormat("EEE, dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date(task.dateDue))
//            } else {
//                "No due date set"
//            }
//
//            Text(formattedDate, style = MaterialTheme.typography.bodyMedium)
//            Row {
//                Button(onClick = { onDelete(task) }) {
//                    Text("Delete")
//                }
//
//                Spacer(modifier = Modifier.width(8.dp))
//
//                Button(
//                    onClick = { onEdit(task) },
////                    modifier = Modifier.weight(1f)
//                ) {
//                    Text("Edit")
//                }
//            }
//        }
//    }
//}
