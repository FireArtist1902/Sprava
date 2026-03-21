package com.example.sprava.navigation

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sprava.database.viewmodels.DateTaskViewModel
import com.example.sprava.database.viewmodels.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(navController: NavController, dateTaskViewModel: DateTaskViewModel){
    Scaffold(topBar = {
        TopAppBar(
            title = {Text("ScheduleScreen", fontSize = 28.sp)},
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.DarkGray,
                titleContentColor = Color.LightGray)
        )
    },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.DarkGray,
                contentColor = Color.LightGray
            ) {
                IconButton(onClick = {  }, enabled = false) {
                    Icon(Icons.Filled.DateRange, contentDescription = "Schedule")
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {navController.navigate(Routes.HOME)}) {
                    Icon(Icons.Filled.Home, contentDescription = "Main")
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { navController.navigate(Routes.SETTINGS) }) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings")
                }
            }
        },
        containerColor = Color.Gray) {paddingValues ->
        val daysOfWeek = listOf<String>("Понеділок", "Вівторок", "Середа", "Четвер", "П'ятниця", "Субота", "Неділя")
        LazyRow(modifier = Modifier.padding(paddingValues)) {
            items(daysOfWeek){ item->
                Column(modifier = Modifier.width(140.dp).padding(vertical = 10.dp).padding(horizontal = 5.dp).background(color = Color.LightGray,shape = RoundedCornerShape(10.dp)).padding(10.dp).fillMaxHeight()) {
                    Text(item, fontSize = 22.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.fillMaxWidth().height(1.dp).background(color = Color.Gray, shape = RoundedCornerShape(1.dp)))
                }
            }
        }
    }
}