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
            val database = WeatherDatabase.getInstance(context)
            val location = database.locationDao().getCurrentLocation()
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
                    lang = lang
                )

            // Update Database
            if (response is WeatherRepo.ResponseState.Success) {
                // Delete old current weather data
                WeatherRepo(context, database.weatherDao()).deleteCurrent()
                // Insert the new current weather data
                WeatherRepo(context, database.weatherDao()).insert(response.weatherResponse)
            }

            val alerts = database.weatherDao()
                .getLocationWeather(location.lat, location.lon).alerts
            var workId: UUID = UUID.randomUUID()

            if (!alerts.isNullOrEmpty()) {
                val alertStart = alerts[0].start.toLong() * 1000
                setOneTimeWorkRequest(
                    alerts[0].event,
                    alerts[0].description,
                    alertStart,
                )
            }

            Log.i("myTag", "Weather has been successfully updated")
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
    ) {
        val data = Data.Builder()
            .putString("event", event)
            .putString("description", description)
            .putString("type", "system")
            .build()

        var timeToTrigger = startTime - 7200000
        if (startTime - 7200000 <= System.currentTimeMillis() && startTime > System.currentTimeMillis()) {
            timeToTrigger = startTime
        }


        val alarmWorkRequest = OneTimeWorkRequest.Builder(AlarmWorker::class.java)
            .setInputData(data)
            .setInitialDelay(getDelayToPass(timeToTrigger), TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniqueWork(
            "systemAlarm",
            ExistingWorkPolicy.REPLACE,
            alarmWorkRequest
        )

        Log.i("myTag", "systemAlarm has been scheduled")
    }

    private fun getDelayToPass(timeToTrigger: Long): Long {
        return (timeToTrigger - timeToTrigger % 60000 - System.currentTimeMillis())
    }
}
