package com.example.remindme

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.remindme.data.Task
import com.example.remindme.data.TaskDao
import com.example.remindme.di.DatabaseModule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
@RunWith(AndroidJUnit4::class)
class TaskListScreenTestBasic {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // Fake DAO for injection
    @BindValue
    @JvmField
    val fakeTaskDao: TaskDao = mockk(relaxed = true)

    // Simple flow for tasks
    private val fakeTasksFlow = MutableStateFlow<List<Task>>(emptyList())

    @Before
    fun setup() {
        hiltRule.inject()
        every { fakeTaskDao.getAllTasks() } returns fakeTasksFlow
    }

    @Test
    fun displayScreen() {
        val fakeTasks = listOf(
            Task(id = 1, name = "Task 1", dateDue = 123L, timeDue = null),
            Task(id = 2, name = "Task 2", dateDue = 456L)
        )
        fakeTasksFlow.value = fakeTasks

        composeTestRule.onNodeWithTag("Task Name Field").performTextInput("New task")
        composeTestRule.onNodeWithTag("Add Task Button").performClick()
        coVerify { fakeTaskDao.insert(match { it.name == "New task" }) }

        // Navigate to TaskListScreen
        composeTestRule.onNodeWithText("Tasks").performClick()
        composeTestRule.onNodeWithText("Edit").performClick()
        composeTestRule.onNodeWithTag("Task Name").performTextClearance()
        composeTestRule.onNodeWithTag("Task Name").performTextInput("Updated task")
        composeTestRule.onNodeWithText("Description").performTextInput("Updated desc")
        composeTestRule.onNodeWithText("Save").performClick()
        coVerify {
            fakeTaskDao.update(
                match { it.name == "Updated task" && it.description == "Updated desc" }
            )
        }
        composeTestRule.onNodeWithText("Delete").onChildAt(2).performClick()
        coVerify { fakeTaskDao.delete(any()) }

    }
}