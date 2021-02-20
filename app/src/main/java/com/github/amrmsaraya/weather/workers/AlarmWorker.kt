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
import com.github.amrmsaraya.weather.data.local.WeatherDatabase
import com.github.amrmsaraya.weather.presenter.ui.MainActivity
import com.github.amrmsaraya.weather.repositories.DataStoreRepo
import java.util.*


private const val CHANNEL_ID = "com.github.amrmsaraya.weather.alert"

class AlarmWorker(private val context: Context, private val params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private var notificationManager: NotificationManager? = null

    override suspend fun doWork(): Result {

        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(CHANNEL_ID, "Alert", "Weather Alerts")

        val database = WeatherDatabase.getInstance(context)
        val location = database.locationDao().getCurrentLocation()

        val alarmId = inputData.getString("id") ?: ""
        val type = inputData.getString("type") ?: "Unknown"
        val event = inputData.getString("type") ?: "Unknown"
        val description = inputData.getString("type") ?: "Unknown"

        return try {
            when (type) {
                "system" -> if (DataStoreRepo(context).readDataStore("notifications") == "Enable") {
                    displayNotification(event, description)
                }
                "custom" -> {
                    if (alarmId.isNotEmpty()) {
                        val alarm = database.alarmDao().getAlarm(UUID.fromString(alarmId))
                        val alerts =
                            database.weatherDao()
                                .getLocationWeather(location.lat, location.lon).alerts

                        when (alarm.type) {
                            "Notification", "إشعار" -> {
                                if (alerts.isNullOrEmpty()) {
                                    displayNotification(
                                        context.getString(R.string.weather_alert),
                                        context.getString(R.string.weather_is_fine)
                                    )
                                } else {
                                    displayNotification(
                                        alerts[0].event,
                                        alerts[0].sender_name
                                    )
                                }
                            }

                            "Alarm", "منبه" -> {
                                if (!Settings.canDrawOverlays(context)) {
                                    val permissionIntent = Intent(
                                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:" + context.packageName)
                                    )
                                    permissionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(permissionIntent)
                                } else {
                                    if (alerts.isNullOrEmpty()) {
                                        val bundle = Bundle()
                                        bundle.putString(
                                            "event",
                                            context.getString(R.string.weather_alert)
                                        )
                                        bundle.putString(
                                            "description",
                                            context.getString(R.string.weather_is_fine)
                                        )
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            context.startForegroundService(
                                                Intent(context, AlarmService::class.java).putExtras(
                                                    bundle
                                                )
                                            )
                                        } else {
                                            context.startService(
                                                Intent(context, AlarmService::class.java).putExtras(
                                                    bundle
                                                )
                                            )
                                        }

                                    } else {
                                        val bundle = Bundle()
                                        bundle.putString("event", event)
                                        bundle.putString("description", description)
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            context.startForegroundService(
                                                Intent(context, AlarmService::class.java).putExtras(
                                                    bundle
                                                )
                                            )
                                        } else {
                                            context.startService(
                                                Intent(context, AlarmService::class.java).putExtras(
                                                    bundle
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        database.alarmDao().delete(alarm)

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
