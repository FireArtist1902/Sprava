package com.example.sprava.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.sprava.database.databases.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action == Intent.ACTION_BOOT_COMPLETED){
            val db = AppDatabase.getInstance(context)

            CoroutineScope(Dispatchers.IO).launch {
                val currentTime = System.currentTimeMillis()
                val tasks = db.dateTaskDao().getFutureNotifications(currentTime)

                tasks.forEach { task ->
                    NotificationScheduler.schedule(
                        context = context,
                        task
                    )
                }
            }
        }
    }
}