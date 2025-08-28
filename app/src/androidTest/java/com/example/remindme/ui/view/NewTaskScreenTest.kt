package com.example.remindme.ui.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.remindme.viewmodel.TaskViewModel
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.ZoneId

@RunWith(AndroidJUnit4::class)
class NewTaskScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: TaskViewModel

    @Before
    fun setup() {
        viewModel = mockk(relaxed = true)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun select_due_date_button_shows_datePicker() {
        composeTestRule.setContent {
            NewTaskScreen(
                viewModel,
                onGoToOverview = { }
            )
        }
        composeTestRule.onNodeWithTag("Select Due Date Button").performClick()
        composeTestRule.onNodeWithTag("Date Picker").assertExists()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun selected_date_displayed_correctly() {
        composeTestRule.setContent {
            val testState = rememberDatePickerState(
                initialSelectedDateMillis = LocalDate.of(2025, 12, 5)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            )
            NewTaskScreen(
                viewModel,
                onGoToOverview = {},
                datePickerState = testState // override datepicker state
            )
        }
        composeTestRule.onNodeWithText("Due Date: Fri, 05 Dec 2025").assertExists()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun add_task_button_calls_taskViewmodel_addTask() {
        composeTestRule.setContent {
            NewTaskScreen(
                viewModel,
                onGoToOverview = { }
            )
        }
        composeTestRule.onNodeWithTag("Task Name Field")
            .performTextInput("Test Task")
        composeTestRule.onNodeWithTag("Description Field")
            .performTextInput("Test Description")
        composeTestRule.onNodeWithTag("Add Task Button").performClick()


        coVerify { viewModel.addTask(
            "Test Task",
            "Test Description",
            -1,
            -1 - 3_600_000
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun add_task_without_name_shows_error_message() {
        composeTestRule.setContent {
            NewTaskScreen(
                viewModel,
                onGoToOverview = { }
            )
        }
        // Ensure task name is empty
        composeTestRule.onNodeWithTag("Task Name Field").performTextInput("")
        // Click AddTask button
        composeTestRule.onNodeWithTag("Add Task Button").performClick()
        // Assert warning text appears
        composeTestRule.onNodeWithText("Task name cannot be empty").assertExists()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun newTaskScreen_defaultDatePickerState_branch_covered() {
        composeTestRule.setContent {
            // Do NOT pass datePickerState → default branch runs
            NewTaskScreen(
                viewModel,
                onGoToOverview = {}
            )
        }

        // Minimal interaction to trigger Compose and remember execution
        composeTestRule.onNodeWithTag("Task Name Field").performTextInput("X")
    }

}