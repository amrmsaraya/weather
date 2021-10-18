package com.github.amrmsaraya.weather.service

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.domain.model.forecast.Alert
import com.github.amrmsaraya.weather.domain.usecase.alert.DeleteAlert
import com.github.amrmsaraya.weather.domain.usecase.alert.GetAlert
import com.github.amrmsaraya.weather.domain.usecase.forecast.GetCurrentForecast
import com.github.amrmsaraya.weather.domain.util.Response
import com.github.amrmsaraya.weather.util.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class AlertWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val getCurrentForecast: GetCurrentForecast,
    private val getAlert: GetAlert,
    private val deleteAlert: DeleteAlert,
) : CoroutineWorker(context, params) {

    private val notificationHelper = NotificationHelper(context)

    override suspend fun doWork(): Result {

        var alerts = listOf<Alert>()
        val bundle = Bundle()
        val intent = Intent(context, AlertService::class.java)
        val alert = getAlert.execute(id.toString())

        when (val response = getCurrentForecast.execute()) {
            is Response.Success -> alerts = response.result.alerts
            is Response.Error -> response.result?.let {
                alerts = it.alerts
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

        deleteAlert.execute(id.toString())
        return Result.success()
    }
}
