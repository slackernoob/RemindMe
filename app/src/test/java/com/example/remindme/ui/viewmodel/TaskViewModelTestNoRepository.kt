package com.example.remindme.ui.viewmodel

/*
import com.example.remindme.data.NewTask
import com.example.remindme.data.TaskQueries
import com.example.remindme.data.Task_table
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

private val testDispatcher = StandardTestDispatcher()
//private lateinit var taskRepository: TaskRepository
private lateinit var taskQueries: TaskQueries
private lateinit var viewModel: TaskViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTestNoRepository {
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
//        taskRepository = mockk(relaxed = true)
//        viewModel = TaskViewModel(taskRepository)
        taskQueries = mockk(relaxed = true) {
            every { selectAll() } returns mockk()
        }
        viewModel = TaskViewModel(taskQueries)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset back to Android Main dispatcher
    }

    @Test
    fun addTask_calls_taskQueries_insertTask() = runTest {
        val name = "Test Task"
        val description = "Description"
        val dateDue = 123456L
        val timeDue = 654321L

        // mock insertTask call
//        coEvery { taskQueries.insertTask(name, description, dateDue, timeDue) } returns QueryResult.Value(1L)
        val task = NewTask(name, description, dateDue, timeDue)
//        viewModel.addTask(task)
        viewModel.addTask(name, description, dateDue, timeDue)
        testScheduler.advanceUntilIdle()
//        coVerify { taskRepository.addTask(task) }
        coVerify { taskQueries.insertTask(name, description, dateDue, timeDue) }
    }

    @Test
    fun updateTask_calls_taskQueries_updateTask() = runTest {
        val id = 1L
        val name = "Updated Task"
        val description = "Desc"
        val dateDue = 98765L
        val timeDue = 12345L
        val task = Task_table(id, name, description, dateDue, timeDue)

        viewModel.updateTask(task)
        testScheduler.advanceUntilIdle()
        coVerify { taskQueries.updateTask(name, description, dateDue, timeDue, id) }
    }

    @Test
    fun deleteTask_calls_taskQueries_deleteTask() = runTest {
        val id = 1L
        val name = "Task to be deleted"
        val description = "Desc"
        val dateDue = 98765L
        val timeDue = 12345L
        val task = Task_table(id, name, description, dateDue, timeDue)
        viewModel.deleteTask(task)
        testScheduler.advanceUntilIdle()
        coVerify { taskQueries.deleteTask(id) }
    }
}

//
//import com.example.remindme.data.Task
//import com.example.remindme.data.TaskDao
//import io.mockk.Runs
//import io.mockk.coEvery
//import io.mockk.coVerify
//import io.mockk.every
//import io.mockk.just
//import io.mockk.mockk
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.flowOf
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.test.StandardTestDispatcher
//import kotlinx.coroutines.test.TestScope
//import kotlinx.coroutines.test.resetMain
//import kotlinx.coroutines.test.runTest
//import kotlinx.coroutines.test.setMain
//import org.junit.After
//import org.junit.Assert.assertEquals
//import org.junit.Before
//import org.junit.Test
//import kotlin.test.assertFailsWith
//
//@OptIn(ExperimentalCoroutinesApi::class)
//class TaskViewModelTest {
//
//    // Test dispatcher instead of Android's Main thread
//    private val testDispatcher = StandardTestDispatcher()
//    private val testScope = TestScope(testDispatcher) // for immed exec
//
//    private lateinit var dao: TaskDao
//    private lateinit var viewModel: TaskViewModel
//
//    @Before
//    fun setup() {
//        // Override Main dispatcher with a test dispatcher
//        Dispatchers.setMain(testDispatcher)
//
//        dao = mockk(relaxed = true) // ignores unstubbed calls
//        every { dao.getAllTasks() } returns flowOf(emptyList())
//
//        viewModel = TaskViewModel(dao)
//    }
//
//    // Prevent unexpected errors for other tests, aka "test pollution"
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain() // reset back to Android Main dispatcher
//    }
//
//    @Test
//    fun addTask_with_empty_string() = runTest(testDispatcher) {
//        val task = Task(
//            name = "test",
//            description = "",
//            dateDue = -123456,
//            timeDue = -1
//        )
//
//        coEvery { dao.insert(any()) } just Runs
//
//        viewModel.addTask(task)
//
//        coVerify { dao.insert(task) }
//
//    }
//
//    @Test
//    fun addTask_should_call_dao_insert() = runTest(testDispatcher) {
//        val taskName = "Test Task"
//        val taskDesc = "Description"
//        val dateDue = 123456789L
//        val timeDue = 987654321L
//
//        val task1 = Task(
//            name = taskName,
//            description = taskDesc,
//            dateDue = dateDue,
//            timeDue = timeDue
//        )
//        val task2 = Task(
//            name = taskName,
//            description = null,
//            dateDue = dateDue,
//            timeDue = null
//        )
//        val task3 = Task(
//            name = taskName,
//            description = taskDesc,
//            dateDue = dateDue,
//            timeDue = null
//        )
//        val task4 = Task(
//            name = taskName,
//            description = null,
//            dateDue = dateDue,
//            timeDue = timeDue
//        )
//        val task2a = Task(
//            name = taskName,
//            dateDue = dateDue,
//        )
//        val task3a = Task(
//            name = taskName,
//            description = taskDesc,
//            dateDue = dateDue,
//        )
//        val task4a = Task(
//            name = taskName,
//            dateDue = dateDue,
//            timeDue = timeDue
//        )
//        val task5 = Task(
//            id = 5,
//            name = taskName,
//            description = taskDesc,
//            dateDue = dateDue,
//            timeDue = timeDue
//        )
//        val task6 = Task(
//            id = 6,
//            name = taskName,
//            description = null,
//            dateDue = dateDue,
//            timeDue = null
//        )
//        val task7 = Task(
//            id = 7,
//            name = taskName,
//            description = taskDesc,
//            dateDue = dateDue,
//            timeDue = null
//        )
//        val task8 = Task(
//            id = 8,
//            name = taskName,
//            description = null,
//            dateDue = dateDue,
//            timeDue = timeDue
//        )
//
//        coEvery { dao.insert(any()) } just Runs
//
//        viewModel.addTask(task1)
//        viewModel.addTask(task2)
//        viewModel.addTask(task2a)
//        viewModel.addTask(task3)
//        viewModel.addTask(task3a)
//        viewModel.addTask(task4)
//        viewModel.addTask(task4a)
//        viewModel.addTask(task5)
//        viewModel.addTask(task6)
//        viewModel.addTask(task7)
//        viewModel.addTask(task8)
//
//        // Wait until task has been added
//        testScheduler.advanceUntilIdle()
//
//        coVerify { dao.insert(task1) }
//        coVerify { dao.insert(task2) }
//        coVerify { dao.insert(task2a) }
//        coVerify { dao.insert(task3) }
//        coVerify { dao.insert(task3a) }
//        coVerify { dao.insert(task4) }
//        coVerify { dao.insert(task4a) }
//        coVerify { dao.insert(task5) }
//        coVerify { dao.insert(task6) }
//        coVerify { dao.insert(task7) }
//        coVerify { dao.insert(task8) }
//    }
//
//    @Test
//    fun add_mockked_Task_should_call_dao_insert() = runTest() {
//        val taskName = "Test Task"
//        val taskDesc = "Description"
//        val dateDue = 123456789L
//        val timeDue = 987654321L
//
//        val task1 = Task(
//            id = 1,
//            name = taskName,
//            description = taskDesc,
//            dateDue = dateDue,
//            timeDue = timeDue
//        )
//        val task2 = Task(
//            id = 1,
//            name = taskName,
//            description = taskDesc,
//            dateDue = dateDue,
//            timeDue = timeDue
//        )
//
//        viewModel.addTask(task1)
//        viewModel.addTask(task2)
//    }
////    fun nullMaker() : Task? {
////        return null
////    }
////    @Test(expected = NullPointerException::class)
////    fun addNullTask_call_dao_insert() = runTest(testDispatcher) {
////        val taskName = "Test Task"
////        val taskDesc = "Description"
////        val dateDue = 123456789L
////        val timeDue = 987654321L
////        coEvery { dao.insert(any()) } just Runs
////        viewModel.addTask(Task(
////            id = 1,
////            name = taskName,
////            description = taskDesc,
////            dateDue = dateDue,
////            timeDue = timeDue
////        ))
////        viewModel.addTask(nullMaker())
////
////        // Wait until task has been added
////        testScheduler.advanceUntilIdle()
////    }
//
//    @Test
//    fun daoInsert_exceptionBranchCovered() = runTest {
//        coEvery { dao.insert(any()) } throws RuntimeException("DB fail")
//
//        val ex = assertFailsWith<RuntimeException> {
//            dao.insert(Task(5, "Task", null, 123L, null))
//        }
//
//        assertEquals("DB fail", ex.message)
////        try {
////            dao.insert(Task(5,"Task", null, 123L, null))
////        } catch (e: RuntimeException) {
////            // expected
////        }
//    }
//
//
//
//    @Test
//    fun deleteTask_should_call_dao_delete() = runTest(testDispatcher) {
//        val taskName = "Test Task"
//        val taskDesc = "Description"
//        val dateDue = 123456789L
//        val timeDue = 987654321L
//        val task1 = Task(
//            name = taskName,
//            description = taskDesc,
//            dateDue = dateDue,
//            timeDue = timeDue
//        )
//        val task2 = Task(
//            name = taskName,
//            description = null,
//            dateDue = dateDue,
//            timeDue = null
//        )
//        val task3 = Task(
//            name = taskName,
//            description = null,
//            dateDue = dateDue,
//            timeDue = timeDue
//        )
//        val task4 = Task(
//            name = taskName,
//            description = taskDesc,
//            dateDue = dateDue,
//            timeDue = null
//        )
//
////        val task = Task(name = "Test", dateDue = 123L)
//        coEvery { dao.delete(any()) } just Runs
//
//        viewModel.deleteTask(task1)
//        viewModel.deleteTask(task2)
//        viewModel.deleteTask(task3)
//        viewModel.deleteTask(task4)
//        testScheduler.advanceUntilIdle()
//
//        coVerify { dao.delete(task1) }
//        coVerify { dao.delete(task2) }
//        coVerify { dao.delete(task3) }
//        coVerify { dao.delete(task4) }
//    }
//
//    @Test
//    fun updateTask_should_call_dao_update() = runTest(testDispatcher) {
//        val task = Task(name = "Update", dateDue = 456L)
//        coEvery { dao.update(any()) } just Runs
//
//        viewModel.updateTask(task)
//        testScheduler.advanceUntilIdle()
//
//        coVerify { dao.update(task) }
//    }
//
//    @Test
//    fun getAllTasks_should_return_flow_of_tasks() = runTest(testDispatcher) {
//        val fakeTasks = listOf(
//            Task(id = 1, name = "Task 1", dateDue = 123L, timeDue = null),
//            Task(id = 2, name = "Task 2", dateDue = 456L)
//        )
//        every { dao.getAllTasks() } returns flowOf(fakeTasks)
//
//        // Re-initialize viewModel in order to pick up new mocked flow
//        viewModel = TaskViewModel(dao)
//
//        var result : List<Task>? = null
//        val job = launch {viewModel.tasks.collect { result = it}}
//        testScheduler.advanceUntilIdle()
//        assertEquals(fakeTasks, result)
//
//        job.cancel()
//    }
//}
*/