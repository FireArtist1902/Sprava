package com.example.sprava.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.*


@Composable
fun MainNavigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ){
        composable(Routes.HOME){
            HomeScreen(navController)
        }

        composable(Routes.CREATION){
            TaskCreationScreen(navController)
        }

        composable(Routes.SCHEDULE){
            ScheduleScreen(navController)
        }
        composable(Routes.SETTINGS){
            SettingsScreen(navController)
        }
    }
}