package com.example.sprava.database.dao

import com.example.sprava.models.Task
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("select * from tasks")
    fun getAllTasks(): Flow<List<Task>>

    @Update
    suspend fun update(task: Task)

    @Insert
    suspend fun insert(task: Task)

    @Delete
    suspend fun delete(task: Task)
}