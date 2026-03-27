package com.example.sprava.database.viewmodels

import com.example.sprava.database.repositories.DateTaskRepository
import com.example.sprava.models.DateTask
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Date

class DateTaskViewModel (
    private val repository: DateTaskRepository
): ViewModel(){
    val dateTasks= repository.tasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addtask(text: String, description: String, startDate: LocalDateTime, endDate: LocalDateTime){
        viewModelScope.launch {
            repository.addTask(
                DateTask(text = text, description = description, startDate = startDate, endDate = endDate)
            )
        }
    }

    fun deleteTask(task: DateTask){
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}