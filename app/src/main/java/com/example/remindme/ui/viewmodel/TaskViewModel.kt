package com.example.remindme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remindme.data.Task
import com.example.remindme.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun addTask(name: String, desc: String?, dateDue: Long, timeDue: Long?) {
        viewModelScope.launch {
//            val description = if (desc == null) null else desc
//            val dueTime = if (timeDue == null) null else timeDue
//            dao.insert(Task(name = name, description = description, dateDue = dateDue, timeDue = dueTime))
            dao.insert(Task(name = name, description = desc, dateDue = dateDue, timeDue = timeDue))

//            val task = when {
//                desc == null && timeDue == null -> Task(10, name, null, dateDue, null)
//                desc == null && timeDue != null -> Task(11, name, null, dateDue, timeDue)
//                desc != null && timeDue == null -> Task(12, name, desc, dateDue, null)
//                else -> Task(0, name, desc, dateDue, timeDue)
//            }
//
//            dao.insert(task)
        }
    }

    // Only in testing
//    suspend fun addTaskSync(name: String, desc: String?, dateDue: Long, timeDue: Long?) {
//        dao.insert(Task(name = name, description = desc, dateDue = dateDue, timeDue = timeDue))
//    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            dao.delete(task)
        }
    }

    fun updateTask(updatedTask: Task) {
        viewModelScope.launch {
            dao.update(updatedTask)
        }
    }
}
