package com.example.sprava.database.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.AndroidViewModel
import com.example.sprava.database.repositories.DateTaskRepository
import com.example.sprava.models.DateTask
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sprava.notification.NotificationScheduler
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

    fun updateTask(task: DateTask){
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: DateTask){
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun scheduleByName(context: Context, name: String){
        viewModelScope.launch {
            val task = repository.getTaskByName(name)
            if(task != null){
                NotificationScheduler.schedule(context, task)
            } else{
                Log.e("DEBUG", "Задача $name не знайдена у базі")
            }
        }
    }
}