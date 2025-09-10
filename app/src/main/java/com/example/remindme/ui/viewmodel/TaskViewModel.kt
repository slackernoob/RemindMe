package com.example.remindme.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.remindme.data.NewTask
import com.example.remindme.data.TaskQueries
import com.example.remindme.data.Task_table
import com.example.remindme.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

//class TaskViewModel(private val dao: TaskDao) : ViewModel() {
@HiltViewModel
class TaskViewModel @Inject constructor(
//    private val dao: TaskDao
    private val repository: TaskRepository,
//    private val taskQueries: TaskQueries
) : ViewModel() {
//    val tasks = dao.getAllTasks().stateIn(
//        viewModelScope,
//        SharingStarted.WhileSubscribed(5000),
//        emptyList()
//    )
//
//    fun addTask(task: Task) {
//        viewModelScope.launch {
//            dao.insert(task)
//        }
//    }
//
//    fun deleteTask(task: Task) {
//        viewModelScope.launch {
//            dao.delete(task)
//        }
//    }
//
//    fun updateTask(updatedTask: Task) {
//        viewModelScope.launch {
//            dao.update(updatedTask)
//        }
//    }
    val tasks = repository.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTask(task: NewTask) {
        viewModelScope.launch {
            repository.addTask(task)
        }
    }

    fun updateTask(task: Task_table) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: Task_table) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
//    val tasks = taskQueries.selectAll()
//        .asFlow()
//        .mapToList(Dispatchers.IO)   // returns Flow<List<TaskTable>>
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
//
//    fun addTask(name: String, description: String?, dateDue: Long, timeDue: Long?) {
//        viewModelScope.launch {
//            taskQueries.insertTask(name, description, dateDue, timeDue)
//        }
//    }
//
//    fun updateTask(task: Task_table) {
//        viewModelScope.launch {
//            taskQueries.updateTask(
//                name = task.name,
//                description = task.description,
//                dateDue = task.dateDue,
//                timeDue = task.timeDue,
//                id = task.id
//            )
//        }
//    }
//
//    fun deleteTask(task: Task_table) {
//        viewModelScope.launch {
//            taskQueries.deleteTask(task.id)
//        }
//    }
}
