package com.example.sprava.database.databases

import com.example.sprava.database.Converters
import com.example.sprava.models.DateTask
import com.example.sprava.models.Task
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.sprava.database.dao.DateTaskDao
import com.example.sprava.database.dao.TaskDao

@Database(
    entities = [Task::class, DateTask::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase(){
    abstract fun taskDao(): TaskDao
    abstract fun dateTaskDao(): DateTaskDao
}

val MIGRATION_1_2 = object : Migration(1, 2){
    override fun migrate(db: SupportSQLiteDatabase) {

    }
}