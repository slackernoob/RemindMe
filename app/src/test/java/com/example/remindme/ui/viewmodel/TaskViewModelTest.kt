package com.example.remindme.viewmodel

import com.example.remindme.data.Task
import com.example.remindme.data.TaskDao
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var dao: TaskDao
    private lateinit var viewModel: TaskViewModel

    @Before
    fun setup() {
        // Override Main dispatcher
        Dispatchers.setMain(testDispatcher)

        dao = mockk()
        every { dao.getAllTasks() } returns flowOf(emptyList())

        viewModel = TaskViewModel(dao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset Main dispatcher
    }

    @Test
    fun `addTask calls dao insert`() = runTest(testDispatcher) {
        val taskName = "Test Task"
        val taskDesc = "Description"
        val dateDue = 123456789L
        val timeDue = 987654321L

        coEvery { dao.insert(any()) } just Runs

        viewModel.addTask(taskName, taskDesc, dateDue, timeDue)

        // Advance until all coroutines finish
        testScheduler.advanceUntilIdle()

        coVerify { dao.insert(match {
            it.name == taskName && it.description == taskDesc &&
                    it.dateDue == dateDue && it.timeDue == timeDue
        }) }
    }

    @Test
    fun `deleteTask calls dao delete`() = runTest(testDispatcher) {
        val task = Task(name = "Test", dateDue = 123L)
        coEvery { dao.delete(any()) } just Runs

        viewModel.deleteTask(task)
        testScheduler.advanceUntilIdle()

        coVerify { dao.delete(task) }
    }

    @Test
    fun `updateTask calls dao update`() = runTest(testDispatcher) {
        val task = Task(name = "Update", dateDue = 456L)
        coEvery { dao.update(any()) } just Runs

        viewModel.updateTask(task)
        testScheduler.advanceUntilIdle()

        coVerify { dao.update(task) }
    }
}
