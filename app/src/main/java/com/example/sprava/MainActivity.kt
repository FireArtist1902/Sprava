package com.example.sprava

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.sprava.database.databases.AppDatabase
import com.example.sprava.database.databases.MIGRATION_1_2
import com.example.sprava.database.repositories.DateTaskRepository
import com.example.sprava.database.repositories.TaskRepository
import com.example.sprava.database.viewmodels.DateTaskViewModel
import com.example.sprava.database.viewmodels.TaskViewModel
import com.example.sprava.navigation.MainNavigation
import com.example.sprava.ui.theme.SpravaTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database = AppDatabase.getInstance(applicationContext)
        val taskRepository = TaskRepository(database.taskDao())
        val dateTaskRepository = DateTaskRepository(database.dateTaskDao())

        val taskViewModel = TaskViewModel(taskRepository)
        val dateTaskViewModel = DateTaskViewModel(dateTaskRepository)
        setContent {
            MainNavigation(taskViewModel, dateTaskViewModel)
        }
    }
}