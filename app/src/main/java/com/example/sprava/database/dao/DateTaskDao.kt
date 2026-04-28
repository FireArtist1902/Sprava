package com.example.sprava.database.dao

import com.example.sprava.models.DateTask
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DateTaskDao {
    @Query("select * from dateTask")
    fun getAllDateTasks(): Flow<List<DateTask>>

    @Query("select * from dateTask where startDate > :currentTime")
    suspend fun getFutureNotifications(currentTime: Long): List<DateTask>

    @Query("select * from dateTask where text = :name limit 1")
    suspend fun getTaskByName(name: String): DateTask?

    @Query("select * from dateTask where id = :id")
    suspend fun getTask(id: Int): DateTask?

    @Update
    suspend fun update(task: DateTask)

    @Insert
    suspend fun insert(task: DateTask)

    @Delete
    suspend fun delete(task: DateTask)
}