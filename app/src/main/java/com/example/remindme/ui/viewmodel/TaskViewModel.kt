package com.example.remindme.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remindme.data.Task
import com.example.remindme.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

//class TaskViewModel(private val dao: TaskDao) : ViewModel() {
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val dao: TaskDao
) : ViewModel() {
    val tasks = dao.getAllTasks().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(task)
        }
    }

//    fun addTask(task: Task?) {
//        viewModelScope.launch {
//            dao.insert(task)
//        }
//    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(task)
        }
    }

    fun updateTask(updatedTask: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(updatedTask)
        }
    }
}
