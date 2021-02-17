package com.github.amrmsaraya.weather.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import com.github.amrmsaraya.weather.data.local.WeatherDatabase
import com.github.amrmsaraya.weather.repositories.DataStoreRepo
import com.github.amrmsaraya.weather.repositories.WeatherRepo
import java.util.*
import java.util.concurrent.TimeUnit


class UpdateWeatherWorker(private val context: Context, private val params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private val workManager = WorkManager.getInstance(context.applicationContext)

    override suspend fun doWork(): Result {
        return try {
            Log.i("myTag", "UpdateWeather Worker Started")
            val database = WeatherDatabase.getInstance(context)
            val location = database.locationDao().getCurrentLocation()
            var alarms = database.alarmDao().getAlarmList()
            val lang = when (DataStoreRepo(context).readDataStore("language")) {
                "English" -> "en"
                "Arabic" -> "ar"
                else -> "en"
            }

            // Get weather from API
            val response =
                WeatherRepo(context, database.weatherDao()).getLive(
                    location.lat,
                    location.lon,
                    lang
                )

            // Update Database
            if (response is WeatherRepo.ResponseState.Success) {
                // Delete old current weather data
                WeatherRepo(context, database.weatherDao()).deleteCurrent()
                // Insert the new current weather data
                WeatherRepo(context, database.weatherDao()).insert(response.weatherResponse)
            }

            // Delete outdated alarms
            alarms.forEach { alarm ->
                if (alarm.end < System.currentTimeMillis()) {
                    if (alarm.isNotified) {
                        workManager.cancelWorkById(alarm.workId)
                    }
                    database.alarmDao().delete(alarm)
                }
            }

            // Update alarm list
            alarms = database.alarmDao().getAlarmList()

            val alerts = database.weatherDao()
                .getLocationWeather(location.lat, location.lon).alerts
            var workId: UUID = UUID.randomUUID()

            if (alerts.isNullOrEmpty()) {
                alarms.forEach { alarm ->
                    // If alarm is enabled and hasn't been notified before
                    if (alarm.isChecked && !alarm.isNotified) {
                        when (alarm.type) {
                            "Alarm" -> workId = setOneTimeWorkRequest(
                                "Weather Alert",
                                "Every thing is ok, no alerts for the scheduled time period",
                                alarm.start,
                                "alarm"
                            )
                            "Notification" -> workId = setOneTimeWorkRequest(
                                "Weather Alert",
                                "Every thing is ok, no alerts for the scheduled time period",
                                alarm.start,
                                "alarm_notification"
                            )
                        }
                        alarm.isNotified = true
                        alarm.workId = workId
                        database.alarmDao().update(alarm)
                    }
                }
            } else {
                alerts.forEach { alert ->
                    val alertStart = alert.start.toLong() * 1000
                    val alertEnd = alert.end.toLong() * 1000

                    if (DataStoreRepo(context).readDataStore("notification") == "true") {
                        setOneTimeWorkRequest(
                            alert.event,
                            alert.description,
                            alertStart,
                            "default_notification"
                        )
                    }

                    // If alarm is time within alert time and enabled and hasn't been notified before
                    alarms.forEach { alarm ->
                        if ((alarm.start in alertStart..alertEnd || alarm.end in alertStart..alertEnd)
                            && alarm.isChecked && !alarm.isNotified
                        ) {
                            when (alarm.type) {
                                "Alarm" -> workId = setOneTimeWorkRequest(
                                    alert.event,
                                    alert.description,
                                    alertStart,
                                    "alarm"
                                )
                                "Notification" -> workId = setOneTimeWorkRequest(
                                    alert.event,
                                    alert.description,
                                    alertStart,
                                    "alarm_notification"
                                )
                            }
                            alarm.isNotified = true
                            alarm.workId = workId
                            database.alarmDao().update(alarm)
                        }
                    }
                }
            }
            Log.i("myTag", "UpdateWeather Worker Done")
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun setOneTimeWorkRequest(
        event: String,
        description: String,
        startTime: Long,
        type: String
    ): UUID {
        val data = Data.Builder()
            .putString("event", event)
            .putString("description", description)
            .putString("type", type)
            .build()

        // Calculate triggering time
        val currentTime = System.currentTimeMillis()
        var specificTimeToTrigger = startTime - 7200000
        if (startTime - 7200000 <= currentTime && startTime > currentTime) {
            specificTimeToTrigger = startTime
        }
        val delayToPass = specificTimeToTrigger - currentTime

        val alarmWorkRequest = OneTimeWorkRequest.Builder(AlarmWorker::class.java)
            .setInputData(data)
            .setInitialDelay(delayToPass, TimeUnit.MILLISECONDS)
            .build()

        when (type) {
            "default_notification" -> workManager.enqueueUniqueWork(
                "alarmWorkRequest",
                ExistingWorkPolicy.REPLACE,
                alarmWorkRequest
            )
            "alarm_notification", "alarm" -> workManager.enqueue(alarmWorkRequest)
        }

        Log.i("myTag", "Enqueued Done")
        return alarmWorkRequest.id
    }
}
