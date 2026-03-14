package com.example.sprava

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.sprava.database.databases.AppDatabase
import com.example.sprava.database.repositories.DateTaskRepository
import com.example.sprava.database.repositories.TaskRepository
import com.example.sprava.database.viewmodels.DateTaskViewModel
import com.example.sprava.database.viewmodels.TaskViewModel
import com.example.sprava.navigation.MainNavigation
import com.example.sprava.ui.theme.SpravaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "tasks-db"
        ).build()

        val taskRepository = TaskRepository(database.taskDao())
        val dateTaskRepository = DateTaskRepository(database.dateTaskDao())

        val taskViewModel = TaskViewModel(taskRepository)
        val dateTaskViewModel = DateTaskViewModel(dateTaskRepository)
        setContent {
            MainNavigation(taskViewModel, dateTaskViewModel)
        }
    }
}