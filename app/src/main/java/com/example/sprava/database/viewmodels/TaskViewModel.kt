package com.example.sprava.database.viewmodels


import com.example.sprava.models.Task
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sprava.database.repositories.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository: TaskRepository
): ViewModel() {
    val tasks = repository.tasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTask(text: String, description: String){
        viewModelScope.launch (Dispatchers.IO){
            repository.addTask(
                Task(text = text, description = description)
            )
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task)
        }
    }
}