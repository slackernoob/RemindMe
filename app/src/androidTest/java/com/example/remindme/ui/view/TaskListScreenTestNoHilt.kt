package com.example.remindme.ui.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.remindme.data.Task
import com.example.remindme.viewmodel.TaskViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskListScreenTestNoHilt {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: TaskViewModel

    private val fakeTasksFlow = MutableStateFlow<List<Task>>(emptyList())

    @Before
    fun setup() {
        viewModel = mockk(relaxed = true)
        every { viewModel.tasks } returns fakeTasksFlow
    }

    // Test that all the tasks are able to be displayed correctly
    @Test
    fun displays_tasks_correctly() {
        val fakeTasks = listOf(
            Task(id = 1, name = "Task 1", dateDue = 123L, timeDue = null),
            Task(id = 2, name = "Task 2", dateDue = 456L)
        )

        every { viewModel.tasks } returns flowOf(fakeTasks).stateIn(
            scope = CoroutineScope(Dispatchers.Unconfined),
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

        composeTestRule.setContent {
            TaskListScreen(
                viewModel,
            )
        }

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Task 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Task 2").assertIsDisplayed()
    }

    // Test delete button calls viewmodel's deleteTask
    @Test
    fun delete_button_calls_deleteTask() {

        // Populating task list with a task such that the task UI shows up
        val task = Task(id = 1, name = "Task 1", dateDue = 123L, timeDue = null)
        fakeTasksFlow.value = listOf(task)

        composeTestRule.setContent {
            TaskListScreen(viewModel)
        }

        composeTestRule.onNodeWithText("Delete").performClick()

        coVerify { viewModel.deleteTask(task) }
    }

    // Test edit button opens edit dialog, and clicking date opens date picker dialog
    @Test
    fun edit_button_opens_edit_dialog() {

        // Populating task list with a task such that the task UI shows up
        val task = Task(id = 1, name = "Task 1", dateDue = 123L, timeDue = null)
        fakeTasksFlow.value = listOf(task)

        every { viewModel.tasks } returns flowOf(listOf(task)).stateIn(
            scope = CoroutineScope(Dispatchers.Unconfined),
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

        composeTestRule.setContent {
            TaskListScreen(viewModel)
        }

        composeTestRule.onNodeWithText("Edit").performClick()
        composeTestRule.onNodeWithText("Edit Task").assertIsDisplayed()

        composeTestRule.onNodeWithTag("Date Picker").performClick()
        composeTestRule.onNodeWithText("Select date").assertIsDisplayed()
    }

    // Test clicking edit button calls viewmodel's updateTask
    @Test
    fun edit_button_calls_viewModel_updateTask() {

        // Populating task list with a task such that the task UI shows up
        val task = Task(id = 1, name = "Task 1", dateDue = 123L, timeDue = null)
        fakeTasksFlow.value = listOf(task)

        every { viewModel.tasks } returns flowOf(listOf(task)).stateIn(
            scope = CoroutineScope(Dispatchers.Unconfined),
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

        composeTestRule.setContent {
            TaskListScreen(viewModel)
        }

        composeTestRule.onNodeWithText("Edit").performClick()
        composeTestRule.onNodeWithTag("Task Name").performTextClearance()
        composeTestRule.onNodeWithTag("Task Name").performTextInput("Updated task")
        composeTestRule.onNodeWithText("Save").performClick()

        coVerify {
            viewModel.updateTask(match { it.name == "Updated task"})
        }
    }

}