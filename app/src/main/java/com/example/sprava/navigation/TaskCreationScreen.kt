package com.example.sprava.navigation

import DateTask
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.serialization.StringFormat
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCreationScreen(navController: NavController){
    val taskName = remember { mutableStateOf("") }
    val taskDescription = remember { mutableStateOf("") }
    val datePickerState = rememberDatePickerState()
    val startDate = remember { mutableLongStateOf(0) }
    val endDate = remember { mutableLongStateOf(0) }
    val isDateTask = remember { mutableStateOf(false) }
    val showStartDateDialog = remember { mutableStateOf(false) }
    val showEndDateDialog = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Task creation")},
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.DarkGray,
                    titleContentColor = Color.LightGray,),
                navigationIcon = {
                    IconButton(onClick = {navController.navigate(Routes.HOME)}) {
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
            Row{
                Checkbox(
                    checked = isDateTask.value,
                    onCheckedChange = { isDateTask.value = it },
                )
                Text("Додати часові межі", fontSize = 28.sp, modifier = Modifier.padding(4.dp))
            }
            if(isDateTask.value){
                Row {
                    TextButton(onClick = {showStartDateDialog.value = true}, ) {
                        Text("Вибрати дату початку завдання")
                    }
                }
                if(showStartDateDialog.value){
                    DatePickerDialog(
                        onDismissRequest = {showStartDateDialog.value = false},
                        confirmButton = {
                            TextButton(onClick = {
                                startDate.longValue = datePickerState.selectedDateMillis!!;
                                showStartDateDialog.value = false
                            }) { Text("Ok")}
                        }
                    ) {DatePicker(state = datePickerState) }
                }
                Row {
                    TextButton(onClick = {showEndDateDialog.value = true}) {
                        Text("Вибрати дату кінця завдання")
                    }
                }
                if(showEndDateDialog.value){
                    DatePickerDialog(
                        onDismissRequest = {showEndDateDialog.value = false},
                        confirmButton = {
                            TextButton(onClick = {
                                endDate.longValue = datePickerState.selectedDateMillis!!;
                                showEndDateDialog.value = false
                            }) { Text("Ok")}
                        }
                    ) {DatePicker(state = datePickerState) }
                }
            }
            Text(text = taskName.value, fontSize = 22.sp)
            Text(text = taskDescription.value, fontSize = 22.sp)
            Button(onClick = {
            }) {Text("Ввести дані") }
        }
    }
}