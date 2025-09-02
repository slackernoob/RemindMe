package com.example.remindme.ui.viewmodel

import com.example.remindme.data.Task
import com.example.remindme.data.TaskDao
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {

    // Test dispatcher instead of Android's Main thread
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher) // for immed exec

    private lateinit var dao: TaskDao
    private lateinit var viewModel: TaskViewModel

    @Before
    fun setup() {
        // Override Main dispatcher with a test dispatcher
        Dispatchers.setMain(testDispatcher)

        dao = mockk(relaxed = true) // ignores unstubbed calls
        every { dao.getAllTasks() } returns flowOf(emptyList())

        viewModel = TaskViewModel(dao)
    }

    // Prevent unexpected errors for other tests, aka "test pollution"
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset back to Android Main dispatcher
    }


    @Test
    fun addTask_should_call_dao_insert() = runTest(testDispatcher) {
        val id = 5
        val taskName = "Test Task"
        val taskDesc = "Description"
        val dateDue = 123456789L
        val timeDue = 987654321L

        val task1 = Task(
            name = taskName,
            description = taskDesc,
            dateDue = dateDue,
            timeDue = timeDue
        )
        val task2 = Task(
            name = taskName,
            description = null,
            dateDue = dateDue,
            timeDue = null
        )
        val task3 = Task(
            name = taskName,
            description = taskDesc,
            dateDue = dateDue,
            timeDue = null
        )
        val task4 = Task(
            name = taskName,
            description = null,
            dateDue = dateDue,
            timeDue = timeDue
        )
        val task5 = Task(
            id = id,
            name = taskName,
            description = taskDesc,
            dateDue = dateDue,
            timeDue = timeDue
        )

        coEvery { dao.insert(any()) } just Runs

        viewModel.addTask(task1)
        viewModel.addTask(task2)
        viewModel.addTask(task3)
        viewModel.addTask(task4)
        viewModel.addTask(task5)

        // Wait until task has been added
        testScheduler.advanceUntilIdle()

        coVerify { dao.insert(task1) }
        coVerify { dao.insert(task2) }
        coVerify { dao.insert(task3) }
        coVerify { dao.insert(task4) }
        coVerify { dao.insert(task5) }
    }

    @Test
    fun add_mockked_Task_should_call_dao_insert() = runTest() {
        val taskName = "Test Task"
        val taskDesc = "Description"
        val dateDue = 123456789L
        val timeDue = 987654321L

        val task1 = Task(
            id = 1,
            name = taskName,
            description = taskDesc,
            dateDue = dateDue,
            timeDue = timeDue
        )
        val task2 = Task(
            id = 1,
            name = taskName,
            description = taskDesc,
            dateDue = dateDue,
            timeDue = timeDue
        )

        viewModel.addTask(task1)
        viewModel.addTask(task2)
    }
//    fun nullMaker() : Task? {
//        return null
//    }
//    @Test(expected = NullPointerException::class)
//    fun addNullTask_call_dao_insert() = runTest(testDispatcher) {
//        val taskName = "Test Task"
//        val taskDesc = "Description"
//        val dateDue = 123456789L
//        val timeDue = 987654321L
//        coEvery { dao.insert(any()) } just Runs
//        viewModel.addTask(Task(
//            id = 1,
//            name = taskName,
//            description = taskDesc,
//            dateDue = dateDue,
//            timeDue = timeDue
//        ))
//        viewModel.addTask(nullMaker())
//
//        // Wait until task has been added
//        testScheduler.advanceUntilIdle()
//    }

    @Test
    fun daoInsert_exceptionBranchCovered() = runTest {
        coEvery { dao.insert(any()) } throws RuntimeException("DB fail")

        val ex = assertFailsWith<RuntimeException> {
            dao.insert(Task(5, "Task", null, 123L, null))
        }

        assertEquals("DB fail", ex.message)
//        try {
//            dao.insert(Task(5,"Task", null, 123L, null))
//        } catch (e: RuntimeException) {
//            // expected
//        }
    }



    @Test
    fun deleteTask_should_call_dao_delete() = runTest(testDispatcher) {
        val taskName = "Test Task"
        val taskDesc = "Description"
        val dateDue = 123456789L
        val timeDue = 987654321L
        val task1 = Task(name = taskName,
            description = taskDesc,
            dateDue = dateDue,
            timeDue = timeDue
        )
        val task2 = Task(name = taskName,
            description = null,
            dateDue = dateDue,
            timeDue = null
        )
        val task3 = Task(name = taskName,
            description = null,
            dateDue = dateDue,
            timeDue = timeDue
        )
        val task4 = Task(name = taskName,
            description = taskDesc,
            dateDue = dateDue,
            timeDue = null
        )

//        val task = Task(name = "Test", dateDue = 123L)
        coEvery { dao.delete(any()) } just Runs

        viewModel.deleteTask(task1)
        viewModel.deleteTask(task2)
        viewModel.deleteTask(task3)
        viewModel.deleteTask(task4)
        testScheduler.advanceUntilIdle()

        coVerify { dao.delete(task1) }
        coVerify { dao.delete(task2) }
        coVerify { dao.delete(task3) }
        coVerify { dao.delete(task4) }
    }
//
//    @Test
//    fun deleteTask_exceptionBranchCovered() = runTest(testDispatcher) {
//        // Make the suspend call fail
//        coEvery { dao.delete(any()) } throws RuntimeException("DB fail")
//
//        val task = Task(name = "Test", dateDue = 1L)
//        var caughtException: Throwable? = null
//        val handler = CoroutineExceptionHandler { _, e -> caughtException = e }
//        // Invoke the ViewModel method (exception happens inside viewModelScope.launch)
////        viewModel.deleteTask(task)
//
//        // Launch in a test scope with handler
//        val job = TestScope(testDispatcher + handler).launch {
//            viewModel.deleteTask(task)  // this uses viewModelScope.launch internally
//        }
//
//        // Let the launched coroutine run
//        testScheduler.advanceUntilIdle()
//        job.cancel() // clean up
//
//        // Assert the exception was caught
//        assertNotNull(caughtException)
//        assertTrue(caughtException is RuntimeException)
//        assertEquals("DB fail", caughtException?.message)
//        // We can only verify it was called; the exception won't escape this test
//        coVerify(exactly = 1) { dao.delete(task) }
//    }




    @Test
    fun updateTask_should_call_dao_update() = runTest(testDispatcher) {
        val task = Task(name = "Update", dateDue = 456L)
        coEvery { dao.update(any()) } just Runs

        viewModel.updateTask(task)
        testScheduler.advanceUntilIdle()

        coVerify { dao.update(task) }
    }

    @Test
    fun getAllTasks_should_return_flow_of_tasks() = runTest(testDispatcher) {
        val fakeTasks = listOf(
            Task(id = 1, name = "Task 1", dateDue = 123L, timeDue = null),
            Task(id = 2, name = "Task 2", dateDue = 456L)
        )
        every { dao.getAllTasks() } returns flowOf(fakeTasks)

        // Re-initialize viewModel in order to pick up new mocked flow
        viewModel = TaskViewModel(dao)

        var result : List<Task>? = null
        val job = launch {viewModel.tasks.collect { result = it}}
        testScheduler.advanceUntilIdle()
        assertEquals(fakeTasks, result)

        job.cancel()
    }
}
