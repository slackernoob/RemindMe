package com.example.remindme.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.remindme.core.utils.getDueDateWithNullCheck
import com.example.remindme.core.utils.getFormattedDueDateText
import com.example.remindme.core.utils.getTimeDueWithDateDue
import com.example.remindme.data.Task
import com.example.remindme.ui.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskScreen(
    viewModel: TaskViewModel = hiltViewModel(),
    onGoToOverview: () -> Unit,
) {
    var datePickerState = rememberDatePickerState()
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var showWarning by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Add Task",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(all = 16.dp)
                .testTag("Add Task Title")
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {


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
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("Description Field")
            )

            // Button to select due date
            Button(
                onClick = { showDatePicker = true },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .testTag("Select Due Date Button")
            ) {
                Text("Select Due Date")
            }

            Text(
                text = getFormattedDueDateText(datePickerState.selectedDateMillis),
                style = MaterialTheme.typography.bodyMedium
            )

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
                    },
                    modifier = Modifier.testTag("Date Picker")
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            // Add Task Button
            Button(
                modifier = Modifier.testTag("Add Task Button"),
                onClick = {
                    if (name.isNotBlank()) {
                        val dateDue = getDueDateWithNullCheck(datePickerState.selectedDateMillis)
                        val timeDue = getTimeDueWithDateDue(dateDue)
                        val task = Task(
                            name = name,
                            description = desc,
                            dateDue = dateDue,
                            timeDue = timeDue
                        )
                        viewModel.addTask(task)
                        name = ""
                        desc = ""
                        datePickerState.selectedDateMillis = null
                        showDatePicker = false
                    } else {
                        // show warning for 2 seconds
                        showWarning = true
                    }
                },
            ) {
                Text("Add Task")
            }

            if (showWarning) {
                Text(
                    "Task name cannot be empty",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // Auto-hide after 2 seconds
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(2000)
                    showWarning = false
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = onGoToOverview) {
                Text("Go to Overview")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }


}
