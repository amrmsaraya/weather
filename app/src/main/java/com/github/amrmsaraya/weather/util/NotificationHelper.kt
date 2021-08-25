package com.github.amrmsaraya.weather.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.github.amrmsaraya.weather.R


class NotificationHelper(private val context: Context) {

    private val channelId = context.packageName
    private val channelName = "Weather"
    val manager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        manager.createNotificationChannel(channel)
    }

    fun getNotification(event: String, description: String): Notification {
        return NotificationCompat.Builder(context.applicationContext, channelId).apply {
            setContentTitle(event)
            setContentText(description)
            setAutoCancel(true)
            setSilent(true)
            setSmallIcon(R.drawable.cloud)
        }.build()
    }
}
