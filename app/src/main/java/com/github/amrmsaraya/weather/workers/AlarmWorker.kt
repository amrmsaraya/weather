package com.github.amrmsaraya.weather.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.presenter.ui.MainActivity


private const val CHANNEL_ID = "com.github.amrmsaraya.weather.alert"

class AlarmWorker(private val context: Context, private val params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private var notificationManager: NotificationManager? = null

    override suspend fun doWork(): Result {
        Log.i("myTag", "Alarm Worker Started")

        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(CHANNEL_ID, "Alert", "Weather Alerts")

        val event = inputData.getString("event") ?: "Unknown"
        val description = inputData.getString("description") ?: "Unknown"
        val type = inputData.getString("type") ?: "Unknown"

        return try {
            when (type) {
                "default_notification", "alarm_notification" -> displayNotification(
                    event,
                    description
                )
                "alarm" -> {
                    if (!Settings.canDrawOverlays(context)) {
                        val permissionIntent = Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + context.packageName)
                        )
                        permissionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(permissionIntent)
                    } else {
                        val bundle = Bundle()
                        bundle.putString("event", event)
                        bundle.putString("description", description)
                        context.startService(
                            Intent(context, AlarmService::class.java).putExtras(
                                bundle
                            )
                        )
                    }
                }
                else -> Unit
            }
            Log.i("myTag", "Alarm Worker Done")
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun displayNotification(title: String, description: String) {
        val notificationId = 120
        val tapResultIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            tapResultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(R.drawable.cloud)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(description)
            )
            .build()
        notificationManager?.notify(notificationId, notification)
    }

    private fun createNotificationChannel(id: String, name: String, channelDescription: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance).apply {
                description = channelDescription
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }
}
