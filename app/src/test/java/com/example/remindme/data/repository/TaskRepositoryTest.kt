package com.example.remindme.data.repository

import com.example.remindme.data.NewTask
import com.example.remindme.data.TaskQueries
import com.example.remindme.data.Task_table
import com.example.remindme.ui.viewmodel.TaskViewModel
import io.mockk.coVerify
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
private lateinit var taskRepository: TaskRepository
private lateinit var taskQueries: TaskQueries

@OptIn(ExperimentalCoroutinesApi::class)
class TaskRepositoryTest {
    @Before
    fun setup() {
        Dispatchers.setMain(com.example.remindme.data.repository.testDispatcher)
        taskQueries = mockk(relaxed = true)
        taskRepository = TaskRepository(taskQueries)
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
        taskRepository.addTask(task)
        testScheduler.advanceUntilIdle()
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

        taskRepository.updateTask(task)
        testScheduler.advanceUntilIdle()
        coVerify {
            taskQueries.updateTask(
                id = id,
                name = name,
                description = description,
                dateDue = dateDue,
                timeDue = timeDue
            )
        }
    }

    @Test
    fun deleteTask_calls_taskQueries_deleteTask() = runTest {
        val id = 1L
        val name = "Task to be deleted"
        val description = "Desc"
        val dateDue = 98765L
        val timeDue = 12345L
        val task = Task_table(id, name, description, dateDue, timeDue)
        taskRepository.deleteTask(task)
        testScheduler.advanceUntilIdle()
        coVerify { taskQueries.deleteTask(id) }
    }
}