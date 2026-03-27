package com.example.sprava.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sprava.database.viewmodels.DateTaskViewModel
import com.example.sprava.database.viewmodels.TaskViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, taskViewModel: TaskViewModel, dateTaskViewModel: DateTaskViewModel) {
    val tasks by taskViewModel.tasks.collectAsState()
    val dateTasks by dateTaskViewModel.dateTasks.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
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
        containerColor = Color.Gray,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .padding(10.dp)) {

            Text("Не датовані завдання:", fontSize = 22.sp)
            LazyColumn{
                items(tasks){ item ->
                    val isDone = remember { mutableStateOf(false) }
                    Column {
                        Row(
                            modifier = Modifier
                                .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
                                .fillMaxWidth()
                                .height(40.dp)
                        ) {
                            Text(
                                text = item.text,
                                fontSize = 22.sp,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(4.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Checkbox(
                                checked = isDone.value,
                                onCheckedChange = {
                                    scope.launch {
                                        if (!isDone.value) {
                                            val result = snackbarHostState.showSnackbar(
                                                "Видалити завдання?",
                                                actionLabel = "Видалити",
                                                withDismissAction = true,
                                                duration = SnackbarDuration.Short
                                            )
                                            when (result) {
                                                SnackbarResult.ActionPerformed -> {
                                                    taskViewModel.deleteTask(item)
                                                }

                                                SnackbarResult.Dismissed -> {
                                                    isDone.value = true
                                                }
                                            }
                                        } else {
                                            isDone.value = !isDone.value
                                        }
                                    }
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            Text("Датовані завдання:", fontSize = 22.sp)
            LazyColumn {
                items(dateTasks){item ->
                    val isDone = remember { mutableStateOf(false) }
                    val dateText = item.startDate?.let {
                        if(it < LocalDateTime.now()) {
                            " Початок: ${item.startDate.dayOfMonth}.${item.startDate.monthValue + 1}.${item.startDate.year}"
                        } else item.endDate?.let { it1 ->
                            if(it1 > LocalDateTime.now()){
                                " Кінець: ${item.endDate.dayOfMonth}.${item.endDate.monthValue + 1}.${item.endDate.year}"
                            }else{
                                " Почато: ${item.startDate.dayOfMonth}.${item.startDate.monthValue + 1}.${item.startDate.year}"
                            }
                        }
                    }.toString()
                    Column {
                        Row(
                            modifier = Modifier
                                .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
                                .fillMaxWidth()
                                .height(40.dp)
                        ) {
                            Text(
                                text = item.text,
                                fontSize = 22.sp,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(4.dp)
                            )
                            Text(text = dateText)
                            Spacer(modifier = Modifier.weight(1f))
                            Checkbox(
                                checked = isDone.value,
                                onCheckedChange = {
                                    scope.launch {
                                        if (!isDone.value) {
                                            val result = snackbarHostState.showSnackbar(
                                                "Видалити завдання?",
                                                actionLabel = "Видалити",
                                                withDismissAction = true,
                                                duration = SnackbarDuration.Short
                                            )
                                            when (result) {
                                                SnackbarResult.ActionPerformed -> {
                                                    dateTaskViewModel.deleteTask(item)
                                                }
                                                SnackbarResult.Dismissed -> {
                                                    isDone.value = true
                                                }
                                            }
                                        } else {
                                            isDone.value = !isDone.value
                                        }
                                    }
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}