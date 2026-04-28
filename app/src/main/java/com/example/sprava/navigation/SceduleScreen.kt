package com.example.sprava.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sprava.R
import com.example.sprava.database.viewmodels.DateTaskViewModel
import com.example.sprava.models.DateTask
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(navController: NavController, dateTaskViewModel: DateTaskViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.schedule_screen), fontSize = 28.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.DarkGray,
                    titleContentColor = Color.LightGray
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.DarkGray,
                contentColor = Color.LightGray
            ) {
                IconButton(onClick = { }, enabled = false) {
                    Icon(Icons.Filled.DateRange, contentDescription = "Schedule")
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { navController.navigate(Routes.HOME) }) {
                    Icon(Icons.Filled.Home, contentDescription = "Main")
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { navController.navigate(Routes.SETTINGS) }) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings")
                }
            }
        },
        containerColor = Color.Gray
    ) { paddingValues ->
        val dateTasks by dateTaskViewModel.dateTasks.collectAsState()
        val daysOfWeek =
            listOf(stringResource(R.string.monday),
                stringResource(R.string.tuesday),
                stringResource(R.string.wednesday),
                stringResource(R.string.thursday),
                stringResource(R.string.friday),
                stringResource(R.string.saturday),
                stringResource(R.string.sunday))
        val today = LocalDateTime.now()
        val mondayDate = today.minusDays(today.dayOfWeek.value.toLong())
        val sundayDate = today.plusDays((7 - today.dayOfWeek.value).toLong())
        val weekTasks = mutableListOf<Pair<Int, List<DateTask>>>()
        for(day in daysOfWeek){
            val index = daysOfWeek.indexOf(day)
            weekTasks.add(Pair(index,dateTasks.filter { dateTask -> dateTask.startDate != null && dateTask.startDate in mondayDate..sundayDate && dateTask.startDate.dayOfWeek.value == index + 1}))
        }

        LazyRow(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            items(daysOfWeek) { item ->
                Column(
                    modifier = Modifier
                        .width(145.dp)
                        .padding(vertical = 10.dp)
                        .padding(horizontal = 5.dp)
                        .background(color = Color.LightGray, shape = RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {

                    Text(
                        item,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(
                        Modifier.fillMaxWidth().height(1.dp)
                            .background(color = Color.Gray, shape = RoundedCornerShape(1.dp))
                    )

                    LazyColumn {
                        items(weekTasks) { pairTask ->

                            if(pairTask.first == daysOfWeek.indexOf(item)){
                                for(task in pairTask.second)
                                {
                                    Text(
                                        task.text,
                                        fontSize = 22.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(top = 10.dp).fillMaxWidth().background(color = Color(173 , 173, 173), shape = RoundedCornerShape(10.dp))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}