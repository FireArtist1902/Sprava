package com.example.sprava.database.repositories

import com.example.sprava.database.dao.TaskDao
import com.example.sprava.models.Task

class TaskRepository (
    private val dao: TaskDao
){
    val tasks = dao.getAllTasks()

    suspend fun addTask(task: Task){
        dao.insert(task)
    }

    suspend fun updateTask(task: Task){
        dao.update(task)
    }

    suspend fun deleteTask(task: Task){
        dao.delete(task)
    }
}