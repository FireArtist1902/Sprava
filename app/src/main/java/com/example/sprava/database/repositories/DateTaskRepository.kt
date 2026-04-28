package com.example.sprava.database.repositories

import com.example.sprava.database.dao.DateTaskDao
import com.example.sprava.models.DateTask

class DateTaskRepository (
    private val dao: DateTaskDao
){
    val tasks = dao.getAllDateTasks()

    suspend fun addTask(task: DateTask){
        dao.insert(task)
    }

    suspend fun updateTask(task: DateTask){
        dao.update(task)
    }

    suspend fun deleteTask(task: DateTask){
        dao.delete(task)
    }

    suspend fun  getTaskByName(name: String): DateTask?{
       return dao.getTaskByName(name)
    }

    suspend fun getTask(id: Int): DateTask?{
        return dao.getTask(id)
    }
}