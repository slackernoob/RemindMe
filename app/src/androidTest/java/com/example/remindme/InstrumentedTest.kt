//package com.example.remindme
//
//import androidx.compose.ui.test.junit4.createAndroidComposeRule
//import androidx.compose.ui.test.onNodeWithContentDescription
//import androidx.compose.ui.test.onNodeWithTag
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.performClick
//import androidx.compose.ui.test.performTextClearance
//import androidx.compose.ui.test.performTextInput
//import androidx.room.Room
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.espresso.Espresso.pressBack
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.example.remindme.data.TaskDao
//import com.example.remindme.data.TaskDatabase
//import com.example.remindme.di.DatabaseModule
//import dagger.hilt.android.testing.BindValue
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
//import dagger.hilt.android.testing.UninstallModules
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@HiltAndroidTest
//@UninstallModules(DatabaseModule::class)
//@RunWith(AndroidJUnit4::class)
//class InstrumentedTest {
//
//    @get:Rule
//    var hiltRule = HiltAndroidRule(this)
//
//    @get:Rule(order = 1)
//    val composeTestRule = createAndroidComposeRule<MainActivity>()
//
//    // Bind a real in-memory DB TaskDao
//    @BindValue
//    @JvmField
//    val taskDao: TaskDao = Room.inMemoryDatabaseBuilder(
//        ApplicationProvider.getApplicationContext(),
//        TaskDatabase::class.java
//    ).allowMainThreadQueries().build().taskDao()
//
//    @Before
//    fun setup() {
//        hiltRule.inject()
//    }
//
//    @Test
//    fun instrumentedTest() {
//        // Add task
//        composeTestRule.onNodeWithTag("Task Name Field").performTextInput("Sample task")
//
//        composeTestRule.onNodeWithTag("Select Due Date Button").performClick()
//        composeTestRule.onNodeWithText("Cancel").performClick()
//        composeTestRule.onNodeWithTag("Select Due Date Button").performClick()
//        pressBack()
//
//        composeTestRule.onNodeWithTag("Select Due Date Button").performClick()
//        composeTestRule.onNodeWithContentDescription("Switch to text input mode")
//            .performClick()
//        composeTestRule.onNodeWithText("Date").performTextInput("10/17/2025")
//        composeTestRule.onNodeWithText("OK").performClick()
//        composeTestRule.onNodeWithTag("Add Task Button").performClick()
//        // Check if added task displayed in task list
//        composeTestRule.onNodeWithTag("Navigation Bar Tasks").performClick()
//        composeTestRule.onNodeWithText("Sample task").assertExists()
//        // Edit task
//        composeTestRule.onNodeWithText("Edit").performClick()
//        composeTestRule.onNodeWithTag("Task Name").performTextClearance()
//        composeTestRule.onNodeWithTag("Task Name").performTextInput("Updated task")
//        composeTestRule.onNodeWithText("Description").performTextInput("Updated desc")
//        composeTestRule.onNodeWithText("Save").performClick()
//        // Check if edited task is displayed in task list
//        composeTestRule.onNodeWithText("Updated task").assertExists()
//        // Go back to NewTaskScreen
//        composeTestRule.onNodeWithTag("Navigation Bar New Task").performClick()
//        // Go to OverviewScreen
//        composeTestRule.onNodeWithText("Go to Overview").performClick()
//        // Verify total number of tasks is correct
//        composeTestRule.onNodeWithText("Total number of tasks = 1").assertExists()
//        // Go to NewTaskScreen from OverviewScreen
//        composeTestRule.onNodeWithText("Go to Main").performClick()
//        // Go back to OverviewScreen
//        composeTestRule.onNodeWithText("Go to Overview").performClick()
//        // Go to TaskListScreen from OverviewScreen
//        composeTestRule.onNodeWithText("Go to List").performClick()
//        // Delete task
//        composeTestRule.onNodeWithText("Delete").performClick()
//        // Check that task is deleted successfully
//        composeTestRule.onNodeWithText("Updated task").assertDoesNotExist()
//    }
//
//    @Test
//    fun newTaskAddTaskWithAndWithoutDate() {
//        composeTestRule.onNodeWithTag("Task Name Field").performTextInput("Sample task 2")
//        composeTestRule.onNodeWithTag("Add Task Button").performClick()
//
//        composeTestRule.onNodeWithTag("Task Name Field").performTextInput("Sample task")
//
//        composeTestRule.onNodeWithTag("Select Due Date Button").performClick()
//        composeTestRule.onNodeWithContentDescription("Switch to text input mode")
//            .performClick()
//        composeTestRule.onNodeWithText("Date").performTextInput("10/17/2025")
//        composeTestRule.onNodeWithText("OK").performClick()
//        composeTestRule.onNodeWithTag("Add Task Button").performClick()
//
//
//    }
//}