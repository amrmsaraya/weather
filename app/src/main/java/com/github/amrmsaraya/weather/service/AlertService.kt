package com.github.amrmsaraya.weather.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.domain.repository.ForecastRepo
import com.github.amrmsaraya.weather.util.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class AlertService : Service() {

    private lateinit var view: View
    private lateinit var windowManager: WindowManager
    private lateinit var params: WindowManager.LayoutParams
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var notificationHelper: NotificationHelper

    override fun onBind(intent: Intent?): IBinder? {
        throw  UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        notificationHelper = NotificationHelper(this)

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val screenWidth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager.maximumWindowMetrics.bounds.width()
        } else {
            windowManager.defaultDisplay.width
        }

        val event = intent?.getStringExtra("event") ?: "Unknown"
        val description = intent?.getStringExtra("description") ?: "Unknown"

        val notification = notificationHelper.getNotification(event, description)

        startForeground(1, notification)

        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = layoutInflater.inflate(R.layout.alert, null)

        val title = view.findViewById<TextView>(R.id.tvTitle)
        title.text = event
        view.findViewById<TextView>(R.id.tvDescription).text = description
        view.findViewById<Button>(R.id.btnDismiss).setOnClickListener {
            mediaPlayer.stop()
            windowManager.removeView(view)
            stopForeground(true)
            stopSelf()
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        params = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        } else {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        }

//         Specify the view position
        params.gravity = Gravity.TOP + Gravity.CENTER_HORIZONTAL
        params.x = 0
        params.y = 25

        view.minimumWidth = screenWidth - screenWidth / 10
        title.width = view.minimumWidth - screenWidth / 20

        windowManager.addView(view, params)

        return super.onStartCommand(intent, flags, startId)
    }

}
