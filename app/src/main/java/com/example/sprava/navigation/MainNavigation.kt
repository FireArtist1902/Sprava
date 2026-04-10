package com.example.sprava.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.sprava.database.viewmodels.DateTaskViewModel
import com.example.sprava.database.viewmodels.TaskViewModel


@RequiresApi(Build.VERSION_CODES.O)
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

        composable(Routes.EDIT + "/{taskId}/{isDate}",
            arguments = listOf(
                navArgument("taskId") {type = NavType.IntType},
                navArgument("isDate") {type = NavType.BoolType}

            )) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("taskId")
            val isDate = backStackEntry.arguments?.getBoolean("isDate")
            EditScreen(id, isDate, navController, taskViewModel, dateTaskViewModel)
        }
    }
}