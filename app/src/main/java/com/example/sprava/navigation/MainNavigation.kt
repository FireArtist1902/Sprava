package com.example.sprava.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.*
import com.example.sprava.database.viewmodels.DateTaskViewModel
import com.example.sprava.database.viewmodels.TaskViewModel


@Composable
fun MainNavigation(taskViewModel: TaskViewModel, dateTaskViewModel: DateTaskViewModel){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ){
        composable(Routes.HOME){
            HomeScreen(navController, taskViewModel, dateTaskViewModel)
        }

        composable(Routes.CREATION){
            TaskCreationScreen(navController, taskViewModel, dateTaskViewModel)
        }

        composable(Routes.SCHEDULE){
            ScheduleScreen(navController, dateTaskViewModel)
        }
        composable(Routes.SETTINGS){
            SettingsScreen(navController)
        }
    }
}