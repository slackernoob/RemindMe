package com.example.remindme.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.remindme.data.NewTask
import com.example.remindme.data.TaskQueries
import com.example.remindme.data.Task_table
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskQueries: TaskQueries
) {
    fun getAllTasks(): Flow<List<Task_table>> {
        return taskQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
    }

    suspend fun addTask(task: NewTask) {
        taskQueries.insertTask(
            task.name,
            task.description,
            task.dateDue,
            task.timeDue
        )
    }

    suspend fun updateTask(task: Task_table) {
        taskQueries.updateTask(
            id = task.id,
            name = task.name,
            description = task.description,
            dateDue = task.dateDue,
            timeDue = task.timeDue
        )
    }

    suspend fun deleteTask(task: Task_table) {
        taskQueries.deleteTask(task.id)
    }
}