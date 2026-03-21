package com.example.sprava.navigation


import androidx.compose.foundation.clickable
import com.example.sprava.models.DateTask
import com.example.sprava.models.Task
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sprava.database.viewmodels.DateTaskViewModel
import com.example.sprava.database.viewmodels.TaskViewModel
import kotlinx.serialization.StringFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

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

    startDate.longValue = Date().time
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Task creation")},
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
        Column(modifier = Modifier.padding(it)) {
            TextField(
                value = taskName.value,
                textStyle = TextStyle(fontSize = 25.sp),
                onValueChange = {value -> taskName.value = value},
                label = {Text("Task name")}
            )
            TextField(
                value = taskDescription.value,
                textStyle = TextStyle(fontSize = 25.sp),
                onValueChange = {value -> taskDescription.value = value},
                label = {Text("Task description")}
            )
            Row(modifier = Modifier.clickable(onClick = {
                isDateTask.value = !isDateTask.value
            })){
                Checkbox(
                    checked = isDateTask.value,
                    onCheckedChange = { isDateTask.value = it },
                )
                Text("Додати часові межі", fontSize = 28.sp, modifier = Modifier.padding(4.dp))
            }
            if(isDateTask.value){
                Column {
                    Row {
                        TextButton(onClick = { showStartDateDialog.value = true }) {
                            Text("Вибрати дату початку завдання")
                        }
                        Text(Date(startDate.longValue).toString(), fontSize = 15.sp)
                    }
                    TextButton(onClick = {showStartTimeDialog = true}) {
                        Text("Ввести час початку завдання")
                    }
                    if(showStartTimeDialog){
                        AlertDialog(
                            onDismissRequest = {showStartTimeDialog = false},
                            confirmButton = {
                                TextButton(onClick = {
                                    val tempDate = Date(startDate.longValue)
                                    tempDate.hours = timePickerState.hour
                                    tempDate.minutes = timePickerState.minute
                                    startDate.longValue = tempDate.time
                                    showStartTimeDialog = false
                                    timePickerState.minute = 0
                                    timePickerState.hour = 0
                                }) { Text("ok")}
                            },
                            dismissButton = {
                                TextButton(onClick = {showStartTimeDialog = false}) { Text("Відміна")}
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
                                    if(Date(startDate.longValue) > Date()){
                                        showStartDateDialog.value = false
                                    }
                                }) { Text("Ok") }
                            }
                        ) { DatePicker(state = datePickerState) }
                    }
                    Row {
                        TextButton(onClick = { showEndDateDialog.value = true }) {
                            Text("Вибрати дату кінця завдання")
                        }
                        Text(Date(endDate.longValue).toString(), fontSize = 15.sp)
                    }
                    TextButton(onClick = {showEndTimeDialog = true}) {
                        Text("ввести час кінця завдання")
                    }

                    if(showEndTimeDialog){
                        AlertDialog(
                            onDismissRequest = {showEndTimeDialog = false},
                            confirmButton = {
                                TextButton(onClick = {
                                    val tempDate = Date(endDate.longValue)
                                    tempDate.hours = timePickerState.hour
                                    tempDate.minutes = timePickerState.minute
                                    endDate.longValue = tempDate.time
                                    showEndTimeDialog = false
                                }) {Text("Ok") }
                            },
                            dismissButton = {
                                TextButton(onClick = {showEndTimeDialog = false}) {Text("Відміна") }
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
                                    showEndDateDialog.value = false
                                }) { Text("Ok") }
                            }
                        ) { DatePicker(state = datePickerState) }
                    }
                }
            }
            Text(text = taskName.value, fontSize = 22.sp)
            Text(text = taskDescription.value, fontSize = 22.sp)
            Button(onClick = {
                if(taskName.value != "" && taskDescription.value != "" && !isDateTask.value)
                {
                    taskViewModel.addTask(taskName.value, taskDescription.value)
                    navController.popBackStack();
                }
                if(isDateTask.value){
                    dateTaskViewModel.addtask(taskName.value, taskDescription.value, Date(startDate.longValue), Date(endDate.longValue))
                    navController.popBackStack()
                }
            }) {Text("Ввести дані") }
        }

    }
}
