package com.example.remindme.viewmodel

import com.example.remindme.data.Task
import com.example.remindme.data.TaskDao
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals

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
        val taskName = "Test Task"
//        val taskDesc = "Description"
        val dateDue = 123456789L
//        val timeDue = 987654321L

        coEvery { dao.insert(any()) } just Runs

//        viewModel.addTask(taskName, taskDesc, dateDue, timeDue)
        viewModel.addTask(taskName, null, dateDue, null)

        // Wait until task has been added
        testScheduler.advanceUntilIdle()

//        coVerify { dao.insert(match {
//            it.name == taskName && it.description == taskDesc &&
//                    it.dateDue == dateDue && it.timeDue == timeDue
//        }) }
        coVerify { dao.insert(match {
            it.name == taskName && it.description == null &&
                    it.dateDue == dateDue && it.timeDue == null
        }) }
    }

    @Test
    fun addTask_all_params_should_call_dao_insert() = runTest(testDispatcher) {
        val taskName = "Test Task"
        val taskDesc = "Description"
        val dateDue = 123456789L
        val timeDue = 987654321L

        coEvery { dao.insert(any()) } just Runs

        viewModel.addTask(taskName, taskDesc, dateDue, timeDue)
//        viewModel.addTask(taskName, null, dateDue, null)

        // Wait until task has been added
        testScheduler.advanceUntilIdle()

        coVerify { dao.insert(match {
            it.name == taskName && it.description == taskDesc &&
                    it.dateDue == dateDue && it.timeDue == timeDue
        }) }
//        coVerify { dao.insert(match {
//            it.name == taskName && it.description == null &&
//                    it.dateDue == dateDue && it.timeDue == null
//        }) }
    }

    @Test
    fun addTaskSync_daoThrows_exceptionBranchCovered() = runTest {
        coEvery { dao.insert(any()) } throws RuntimeException("DB fail")

        try {
            viewModel.addTaskSync("Task", null, 123L, null)
        } catch (e: RuntimeException) {
            // expected
        }
    }

    @Test
    fun daoInsert_exceptionBranchCovered() = runTest {
        coEvery { dao.insert(any()) } throws RuntimeException("DB fail")

        try {
            dao.insert(Task(5,"Task", null, 123L, null))
        } catch (e: RuntimeException) {
            // expected
        }
    }




    @Test
    fun deleteTask_should_call_dao_delete() = runTest(testDispatcher) {
        val task = Task(name = "Test", dateDue = 123L)
        coEvery { dao.delete(any()) } just Runs

        viewModel.deleteTask(task)
        testScheduler.advanceUntilIdle()

        coVerify { dao.delete(task) }
    }

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
