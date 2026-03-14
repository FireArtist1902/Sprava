package com.example.sprava.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "dateTask")
data class DateTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val description: String,
    val startDate: Date?,
    val endDate: Date?
)