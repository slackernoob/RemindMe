package com.example.remindme.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert suspend fun insert(task: Task)
    @Update suspend fun update(task: Task)
    @Delete suspend fun delete(task: Task)

    @Query("SELECT * FROM task_table ORDER BY dateDue ASC")
    fun getAllTasks(): Flow<List<Task>>
}