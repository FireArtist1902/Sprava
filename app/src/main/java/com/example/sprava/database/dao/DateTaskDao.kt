package com.example.sprava.database.dao

import com.example.sprava.models.DateTask
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DateTaskDao {
    @Query("select * from dateTask")
    fun getAllDateTasks(): Flow<List<DateTask>>

    @Insert
    suspend fun insert(task: DateTask)

    @Delete
    suspend fun delete(task: DateTask)
}