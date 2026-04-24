package com.example.sprava.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.ui.autofill.ContentType
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent){
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "tasks_channel"

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, "Нагадування", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(intent.getStringExtra("title")?: "Задача")
            .setContentText(intent.getStringExtra("message")?: "За роботу")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        manager.notify(intent.hashCode(), notification)
    }
}