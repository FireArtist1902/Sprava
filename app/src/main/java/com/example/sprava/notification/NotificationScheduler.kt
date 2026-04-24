package com.example.sprava.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.ui.autofill.ContentDataType
import com.example.sprava.models.DateTask
import java.time.ZoneId

object NotificationScheduler{
    fun schedule(context: Context, task: DateTask){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val timeInMillis = task.startDate!!.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        if(timeInMillis <= System.currentTimeMillis()) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            if (!alarmManager.canScheduleExactAlarms()){
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
                return
            }
        }

        val intent = Intent(context, NotificationReceiver::class.java).apply{
            putExtra("title", task.text)
            putExtra("message", task.description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            if(alarmManager.canScheduleExactAlarms()){
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
        }
    }

    fun cancel(context: Context, id: Int){
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, id, intent, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        if(pendingIntent != null){
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }
    }
}