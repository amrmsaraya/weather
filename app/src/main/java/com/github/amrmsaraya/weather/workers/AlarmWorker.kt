package com.github.amrmsaraya.weather.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
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
import com.github.amrmsaraya.weather.presentation.ui.MainActivity
import com.github.amrmsaraya.weather.repositories.DataStoreRepo
import java.util.*


const val CHANNEL_ID = "com.github.amrmsaraya.weather.alert"

class AlarmWorker(private val context: Context, private val params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private var notificationManager: NotificationManager? = null

    override suspend fun doWork(): Result {

        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(CHANNEL_ID, "Alert", "Weather Alerts")

        val database = WeatherDatabase.getInstance(context)
        val location = database.locationDao().getCurrentLocation()
        val locale = readDataStore("language")
        val alarmId = inputData.getString("id") ?: ""
        val type = inputData.getString("type") ?: "Unknown"

        when (locale) {
            "English" -> setLocale("en")
            "Arabic" -> setLocale("ar")
        }

        return try {
            val alerts =
                database.weatherDao()
                    .getLocationWeather(location.lat, location.lon).alerts
            when (type) {
                "system" -> if (DataStoreRepo(context).readDataStore("notifications") == "Enable") {
                    if (!alerts.isNullOrEmpty()) {
                        displayNotification(alerts[0].event, alerts[0].description)
                    }

                }
                "custom" -> {
                    if (alarmId.isNotEmpty()) {
                        val alarm = database.alarmDao().getAlarm(UUID.fromString(alarmId))
                        when (alarm.type) {
                            "Notification", "إشعار" -> {
                                if (alerts.isNullOrEmpty()) {
                                    displayNotification(
                                        context.getString(R.string.weather_alert),
                                        context.getString(R.string.weather_is_fine)
                                    )
                                } else {
                                    if (alarm.start in alerts[0].start.toLong() * 1000..alerts[0].end.toLong() * 1000) {
                                        if (alerts[0].description.isEmpty()) {
                                            displayNotification(
                                                alerts[0].event,
                                                "No Description provided"
                                            )
                                        } else {
                                            displayNotification(
                                                alerts[0].event,
                                                alerts[0].description
                                            )
                                        }
                                    }
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
                                        bundle.putString("language", locale)
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
                                        if (alarm.start in alerts[0].start.toLong() * 1000..alerts[0].end.toLong() * 1000) {
                                            val bundle = Bundle()
                                            bundle.putString("event", alerts[0].event)
                                            bundle.putString("language", locale)
                                            if (alerts[0].description.isEmpty()) {
                                                bundle.putString(
                                                    "description",
                                                    "No Description provided"
                                                )
                                            } else {
                                                bundle.putString(
                                                    "description",
                                                    alerts[0].description
                                                )
                                            }

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                context.startForegroundService(
                                                    Intent(
                                                        context,
                                                        AlarmService::class.java
                                                    ).putExtras(
                                                        bundle
                                                    )
                                                )
                                            } else {
                                                context.startService(
                                                    Intent(
                                                        context,
                                                        AlarmService::class.java
                                                    ).putExtras(
                                                        bundle
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        database.alarmDao().delete(alarm)
                        database.close()
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
        val notificationId = 1
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

    private suspend fun readDataStore(key: String): String? {
        return DataStoreRepo(context).readDataStore(key)
    }

    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val res: Resources = context.resources
        val config: Configuration = res.configuration
        config.setLocale(locale)
        res.updateConfiguration(config, res.displayMetrics)
    }
}
