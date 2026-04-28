package com.example.sprava.navigation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import com.example.sprava.models.DateTask
import com.example.sprava.models.Task
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sprava.R
import com.example.sprava.database.viewmodels.DateTaskViewModel
import com.example.sprava.database.viewmodels.TaskViewModel
import com.example.sprava.notification.NotificationReceiver
import com.example.sprava.notification.NotificationScheduler.schedule
import kotlinx.serialization.StringFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCreationScreen(navController: NavController, taskViewModel: TaskViewModel, dateTaskViewModel: DateTaskViewModel){
    val taskName = remember { mutableStateOf("") }
    val taskDescription = remember { mutableStateOf("") }
    val datePickerState = rememberDatePickerState()
    val startDate = remember { mutableLongStateOf(0) }
    val endDate = remember { mutableLongStateOf(0) }
    val isDateTask = remember { mutableStateOf(false) }
    val showStartDateDialog = remember { mutableStateOf(false) }
    val showEndDateDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = 0,
        initialMinute = 0,
        is24Hour = true
    )
    var showStartTimeDialog by remember { mutableStateOf(false) }
    var showEndTimeDialog by remember { mutableStateOf(false) }

    startDate.longValue = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(stringResource(R.string.task_creation))},
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.DarkGray,
                    titleContentColor = Color.LightGray,),
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        containerColor = Color.Gray
    ) {
        LazyColumn(modifier = Modifier.padding(it).padding(10.dp)) {
            item{
                TextField(
                    value = taskName.value,
                    textStyle = TextStyle(fontSize = 25.sp),
                    onValueChange = {value -> taskName.value = value},
                    label = {Text(stringResource(R.string.task_name))}
                )
                TextField(
                    value = taskDescription.value,
                    textStyle = TextStyle(fontSize = 25.sp),
                    onValueChange = {value -> taskDescription.value = value},
                    label = {Text(stringResource(R.string.task_desc))}
                )
                Row(modifier = Modifier.clickable(onClick = {
                    isDateTask.value = !isDateTask.value
                })){
                    Checkbox(
                        checked = isDateTask.value,
                        onCheckedChange = { isDateTask.value = it },
                    )
                    Text(stringResource(R.string.add_time_boundaries), fontSize = 28.sp, modifier = Modifier.padding(4.dp))
                }
                if(isDateTask.value){
                    Column {
                        Row {
                            TextButton(onClick = { showStartDateDialog.value = true }) {
                                Text(stringResource(R.string.select_start_date))
                            }
                            Text(Date(startDate.longValue).toString(), fontSize = 15.sp)
                        }
                        TextButton(onClick = {showStartTimeDialog = true}) {
                            Text(stringResource(R.string.select_start_time))
                        }
                        if(showStartTimeDialog){
                            AlertDialog(
                                onDismissRequest = {showStartTimeDialog = false},
                                confirmButton = {
                                    TextButton(onClick = {
                                        var tempDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(startDate.longValue), ZoneOffset.UTC)
                                        tempDate = tempDate.with(LocalTime.of(timePickerState.hour, timePickerState.minute))
                                        startDate.longValue = tempDate.toInstant(ZoneOffset.UTC).toEpochMilli()
                                        showStartTimeDialog = false
                                        timePickerState.minute = 0
                                        timePickerState.hour = 0
                                    }) { Text(stringResource(R.string.ok))}
                                },
                                dismissButton = {
                                    TextButton(onClick = {showStartTimeDialog = false}) { Text(stringResource(R.string.cancel))}
                                },
                                text = {
                                    TimePicker(state = timePickerState)
                                }
                            )
                        }
                        if (showStartDateDialog.value) {
                            DatePickerDialog(
                                onDismissRequest = { showStartDateDialog.value = false },
                                confirmButton = {
                                    TextButton(onClick = {
                                        startDate.longValue = datePickerState.selectedDateMillis!!
                                        if(startDate.longValue >= LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()){
                                            showStartDateDialog.value = false
                                        }
                                    }) { Text(stringResource(R.string.ok)) }
                                }
                            ) { DatePicker(state = datePickerState) }
                        }
                        Row {
                            TextButton(onClick = { showEndDateDialog.value = true }) {
                                Text(stringResource(R.string.select_end_date))
                            }
                            Text(Date(endDate.longValue).toString(), fontSize = 15.sp)
                        }
                        TextButton(onClick = {showEndTimeDialog = true}) {
                            Text(stringResource(R.string.select_end_time))
                        }

                        if(showEndTimeDialog){
                            AlertDialog(
                                onDismissRequest = {showEndTimeDialog = false},
                                confirmButton = {
                                    TextButton(onClick = {
                                        var tempDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(startDate.longValue), ZoneOffset.UTC)
                                        tempDate = tempDate.with(LocalTime.of(timePickerState.hour, timePickerState.minute))
                                        endDate.longValue = tempDate.toInstant(ZoneOffset.UTC).toEpochMilli()
                                        showEndTimeDialog = false
                                    }) {Text(stringResource(R.string.ok)) }
                                },
                                dismissButton = {
                                    TextButton(onClick = {showEndTimeDialog = false}) {Text(stringResource(R.string.cancel)) }
                                },
                                text = {
                                    TimePicker(state = timePickerState)
                                }
                            )
                        }

                        if (showEndDateDialog.value) {
                            DatePickerDialog(
                                onDismissRequest = { showEndDateDialog.value = false },
                                confirmButton = {
                                    TextButton(onClick = {
                                        endDate.longValue = datePickerState.selectedDateMillis!!;
                                        if(endDate.longValue >= LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()){
                                            showEndDateDialog.value = false
                                        }
                                    }) { Text(stringResource(R.string.ok)) }
                                }
                            ) { DatePicker(state = datePickerState) }
                        }
                    }
                }
                Text(text = taskName.value, fontSize = 22.sp)
                Text(text = taskDescription.value, fontSize = 22.sp)
                Button(onClick = {
                    if(taskName.value != "" && taskDescription.value != "")
                    {
                        if(isDateTask.value) {
                            dateTaskViewModel.addtask(
                                taskName.value,
                                taskDescription.value,
                                LocalDateTime.ofInstant(
                                    Instant.ofEpochMilli(startDate.longValue),
                                    ZoneOffset.UTC
                                ),
                                LocalDateTime.ofInstant(
                                    Instant.ofEpochMilli(endDate.longValue),
                                    ZoneOffset.UTC
                                )
                            )

                            dateTaskViewModel.scheduleByName(context, taskName.value)
                        } else{
                            taskViewModel.addTask(taskName.value, taskDescription.value)
                        }
                        navController.popBackStack()
                    }
                }) {Text(stringResource(R.string.enter)) }
            }
        }
    }
}
