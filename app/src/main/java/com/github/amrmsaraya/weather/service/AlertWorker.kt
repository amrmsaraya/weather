package com.github.amrmsaraya.weather.service

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.work.Worker
import androidx.work.WorkerParameters

class AlertWorker(
    private val context: Context,
    private val params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(context, AlertService::class.java))
        } else {
            context.startService(Intent(context, AlertService::class.java))
        }
        return Result.success()
    }
}
