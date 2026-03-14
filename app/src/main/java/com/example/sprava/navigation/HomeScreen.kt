package com.example.sprava.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(topBar = {
        TopAppBar(
            title = {Text("MainScreen", fontSize = 28.sp)},
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.DarkGray,
                titleContentColor = Color.LightGray)
        )
    },
        bottomBar = {
        BottomAppBar(
            containerColor = Color.DarkGray,
            contentColor = Color.LightGray
        ) {
            IconButton(onClick = { navController.navigate(Routes.SCHEDULE) }) {
                Icon(Icons.Filled.DateRange, contentDescription = "Schedule")
            }
            Spacer(Modifier.weight(1f))
            IconButton(onClick = {}, enabled = false) {
                Icon(Icons.Filled.Home, contentDescription = "Main")
            }
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { navController.navigate(Routes.SETTINGS) }) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings")
            }
        }
    },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Routes.CREATION) }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        containerColor = Color.Gray) {
        Text("SomeMainScreenContent", fontSize = 28.sp, modifier = Modifier.padding(it))
    }
}