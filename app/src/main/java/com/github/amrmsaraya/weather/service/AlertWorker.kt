package com.github.amrmsaraya.weather.service

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.models.forecast.Alert
import com.github.amrmsaraya.weather.domain.repository.AlertRepo
import com.github.amrmsaraya.weather.domain.repository.ForecastRepo
import com.github.amrmsaraya.weather.util.NotificationHelper
import com.github.amrmsaraya.weather.util.Response
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class AlertWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val forecastRepo: ForecastRepo,
    private val alertRepo: AlertRepo
) : CoroutineWorker(context, params) {

    val notificationHelper = NotificationHelper(context)

    override suspend fun doWork(): Result {

        var alerts = listOf<Alert>()
        val bundle = Bundle()
        val intent = Intent(context, AlertService::class.java)
        val alert = alertRepo.getAlert(id.toString())

        when (val response = forecastRepo.getCurrentForecast()) {
            is Response.Success -> alerts = response.result.first().alerts
            is Response.Error -> response.result?.let {
                alerts = it.first().alerts
            }
        }

        when (alert.isAlarm) {
            true -> {
                if (alerts.isNotEmpty()) {
                    bundle.putString("event", alerts[0].event)
                    bundle.putString("description", alerts[0].description)
                } else {
                    bundle.putString("event", context.getString(R.string.weather_alert))
                    bundle.putString("description", context.getString(R.string.weather_is_fine))
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent.putExtras(bundle))
                } else {
                    context.startService(intent.putExtras(bundle))
                }
            }
            false -> {
                val notification = notificationHelper.getNotification(
                    event = if (alerts.isNotEmpty()) {
                        alerts[0].event
                    } else {
                        context.getString(R.string.weather_alert)
                    },
                    description = if (alerts.isNotEmpty()) {
                        alerts[0].description
                    } else {
                        context.getString(R.string.weather_is_fine)
                    },
                )
                notificationHelper.manager.notify(1, notification)
            }
        }


        alertRepo.delete(id.toString())
        return Result.success()
    }
}
