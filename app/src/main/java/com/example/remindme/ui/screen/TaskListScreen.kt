package com.example.remindme.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.remindme.data.Task
import com.example.remindme.viewmodel.TaskViewModel


@Composable
fun TaskListScreen() {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    var editingTask by remember { mutableStateOf<Task?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("All Tasks", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 16.dp))

        LazyColumn {
            items(tasks.size) { index ->
                val task = tasks[index]
                TaskCard(
                    task = task,
                    onDelete = { viewModel.deleteTask(it) },
                    onEdit = { editingTask = it }
                )
            }
        }

        if (editingTask != null) {
            var editName by remember { mutableStateOf(editingTask!!.name) }
            var editDesc by remember { mutableStateOf(editingTask!!.description ?: "") }
            val editDateState = rememberDatePickerState(initialSelectedDateMillis = editingTask!!.dateDue.takeIf { it != -1L })

            AlertDialog(
                onDismissRequest = { editingTask = null },
                confirmButton = {
                    TextButton(onClick = {
                        val newDue = editDateState.selectedDateMillis ?: -1
                        viewModel.updateTask(editingTask!!.copy(name = editName, description = editDesc, dateDue = newDue))
                        editingTask = null
                    }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { editingTask = null }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Edit Task") },
                text = {
                    Column {
                        OutlinedTextField(value = editName, onValueChange = { editName = it }, label = { Text("Task Name") })
                        OutlinedTextField(value = editDesc, onValueChange = { editDesc = it }, label = { Text("Description") })
                        DatePicker(state = editDateState)
                    }
                }
            )
        }
    }
}

@Composable
fun TaskCard(task: Task, onDelete: (Task) -> Unit, onEdit: (Task) -> Unit) {
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

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { onEdit(task) },
//                    modifier = Modifier.weight(1f)
                ) {
                    Text("Edit")
                }
            }
        }
    }
}