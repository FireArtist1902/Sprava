package com.example.sprava.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sprava.R
import com.example.sprava.database.viewmodels.DateTaskViewModel
import com.example.sprava.database.viewmodels.TaskViewModel
import com.example.sprava.models.DateTask
import com.example.sprava.models.Task
import com.example.sprava.notification.NotificationScheduler
import com.example.sprava.notification.NotificationScheduler.schedule
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(taskId: Int?, isDate: Boolean?, navController: NavController, defaultTask: TaskViewModel, dateTask: DateTaskViewModel){
    if(taskId != null && isDate != null){
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {Text(stringResource(R.string.edit_task))},
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.DarkGray,
                        titleContentColor = Color.LightGray),
                    navigationIcon = {
                        IconButton(onClick = {navController.popBackStack()}) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            containerColor = Color.Gray
        ) {
            if(!isDate){
                val currentTask = defaultTask.tasks.collectAsState().value.find { task -> task.id == taskId }
                val newTaskText = remember { mutableStateOf(currentTask!!.text) }
                val newTaskDesc = remember { mutableStateOf(currentTask!!.description) }
                Column(modifier = Modifier.padding(it)) {
                    TextField(
                        value = newTaskText.value,
                        onValueChange = { newText -> newTaskText.value = newText},
                        textStyle = TextStyle(fontSize = 22.sp)
                    )
                    TextField(
                        value = newTaskDesc.value,
                        onValueChange = { newText -> newTaskDesc.value = newText},
                        textStyle = TextStyle(fontSize = 22.sp)
                    )
                    Button(
                        onClick = {defaultTask.updateTask(Task(taskId, newTaskText.value, newTaskDesc.value)); navController.popBackStack()}
                    ) { Text(stringResource(R.string.enter)) }
                }
            } else
            {
                val currentTask = dateTask.dateTasks.collectAsState().value.find { task -> task.id == taskId }
                val newTaskText = remember { mutableStateOf(currentTask!!.text) }
                val newTaskDesc = remember { mutableStateOf(currentTask!!.description) }
                val newStartDate = remember { mutableStateOf(currentTask!!.startDate) }
                val newEndDate = remember { mutableStateOf(currentTask!!.endDate) }
                val showStartTimeDialog = remember { mutableStateOf(false) }
                val showEndTimeDialog = remember { mutableStateOf(false) }
                val showStartDateDialog = remember { mutableStateOf(false) }
                val showEndDateDialog = remember { mutableStateOf(false) }
                val datePickerState = rememberDatePickerState()
                val timePickerState = rememberTimePickerState(
                    initialHour = 0,
                    initialMinute = 0,
                    is24Hour = true
                )
                val context = LocalContext.current
                Column(modifier = Modifier.padding(it)) {
                    TextField(
                        value = newTaskText.value,
                        onValueChange = { newText -> newTaskText.value = newText},
                        textStyle = TextStyle(fontSize = 22.sp)
                    )
                    TextField(
                        value = newTaskDesc.value,
                        onValueChange = { newText -> newTaskDesc.value = newText},
                        textStyle = TextStyle(fontSize = 22.sp)
                    )

                    TextButton(onClick = {showStartDateDialog.value = true}) {
                        Text(text = newStartDate.value.toString())
                    }
                    if (newEndDate.value!! > LocalDateTime.now())
                    {
                        TextButton(onClick = {showEndDateDialog.value = true}) {
                            Text(text = newEndDate.value.toString())
                        }
                    }

                    if(showStartDateDialog.value){
                        DatePickerDialog(
                            onDismissRequest = {showStartDateDialog.value = false},
                            confirmButton = {
                                TextButton(onClick = {
                                    val hours = newStartDate.value!!.hour
                                    val minute = newStartDate.value!!.minute
                                    newStartDate.value = LocalDateTime.ofInstant(Instant.ofEpochMilli(datePickerState.selectedDateMillis!!), ZoneOffset.UTC)
                                    newStartDate.value = newStartDate.value!!.with(LocalTime.of(hours, minute))
                                    if(newStartDate.value!! > LocalDateTime.now()){
                                        showStartDateDialog.value = false
                                        showStartTimeDialog.value = true
                                    }
                                }) {
                                    Text("Ok")
                                }
                            }
                        ) {DatePicker(state = datePickerState)}
                    }

                    if(showStartTimeDialog.value){
                        timePickerState.hour = newStartDate.value!!.hour
                        timePickerState.minute = newStartDate.value!!.minute
                        AlertDialog(
                            onDismissRequest = {showStartTimeDialog.value = false},
                            confirmButton = {
                                TextButton(onClick = {
                                    var tempDate = newStartDate.value
                                    tempDate = tempDate!!.with(LocalTime.of(timePickerState.hour, timePickerState.minute))
                                    newStartDate.value = tempDate
                                    showStartTimeDialog.value = false
                                }) {
                                    Text("Ok")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = {showStartTimeDialog.value = false}) { Text("Cancel") }
                            },
                            text = {
                                TimePicker(state = timePickerState)
                            }
                        )
                    }

                    if(showEndDateDialog.value){
                        DatePickerDialog(
                            onDismissRequest = {showEndDateDialog.value = false},
                            confirmButton = {
                                TextButton(onClick = {
                                    val hours = newEndDate.value!!.hour
                                    val minute = newEndDate.value!!.minute
                                    newEndDate.value = LocalDateTime.ofInstant(Instant.ofEpochMilli(datePickerState.selectedDateMillis!!), ZoneOffset.UTC)
                                    newEndDate.value = newEndDate.value!!.with(LocalTime.of(hours, minute))
                                    if(newEndDate.value!! > LocalDateTime.now()){
                                        showEndDateDialog.value = false
                                        showEndTimeDialog.value = true
                                    }
                                }) {
                                    Text("Ok")
                                }
                            }
                        ) {DatePicker(state = datePickerState)}
                    }

                    if(showEndTimeDialog.value){
                        timePickerState.hour = newEndDate.value!!.hour
                        timePickerState.minute = newEndDate.value!!.minute
                        AlertDialog(
                            onDismissRequest = {showEndTimeDialog.value = false},
                            confirmButton = {
                                TextButton(onClick = {
                                    var tempDate = newEndDate.value
                                    tempDate = tempDate!!.with(LocalTime.of(timePickerState.hour, timePickerState.minute))
                                    newEndDate.value = tempDate
                                    showEndTimeDialog.value = false
                                }) {
                                    Text("Ok")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = {showEndTimeDialog.value = false}) { Text("Cancel") }
                            },
                            text = {
                                TimePicker(state = timePickerState)
                            }
                        )
                    }

                    Button(
                        onClick = {dateTask.updateTask(
                            DateTask(
                                taskId,
                                newTaskText.value,
                                newTaskDesc.value,
                                newStartDate.value,
                                newEndDate.value
                            )
                        );dateTask.scheduleById(context, taskId)
                            navController.popBackStack()}
                    ) { Text(stringResource(R.string.enter)) }
                }
            }
        }
    } else{
        navController.popBackStack()
    }
}