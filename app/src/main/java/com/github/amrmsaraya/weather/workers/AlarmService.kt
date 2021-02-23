package com.github.amrmsaraya.weather.workers

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.databinding.DialogAlarmBinding
import java.util.*

class AlarmService : Service() {
    private lateinit var binding: DialogAlarmBinding
    private lateinit var windowManager: WindowManager
    private lateinit var params: WindowManager.LayoutParams
    private lateinit var mediaPlayer: MediaPlayer
    private var wakeLock: PowerManager.WakeLock? = null

    override fun onBind(intent: Intent?): IBinder? {
        throw  UnsupportedOperationException("Not yet implemented");
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val event = intent?.getStringExtra("event") ?: "Unknown"
        val description = intent?.getStringExtra("description") ?: "Unknown"
        val locale = intent?.getStringExtra("language") ?: "Unknown"
        when (locale) {
            "English" -> setLocale("en")
            "Arabic" -> setLocale("ar")
        }

        val notification = NotificationCompat.Builder(baseContext, CHANNEL_ID)
            .setContentTitle(event)
            .setContentText(description)
            .setSmallIcon(R.drawable.cloud)
            .setNotificationSilent()
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(description)
            )
            .build()
        startForeground(1, notification)


        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
                    acquire(10 * 60 * 1000L)
                }
            }

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(applicationContext),
            R.layout.dialog_alarm,
            null,
            false
        )

        binding.tvAlarmTitle.text = event
        binding.tvAlarmDescription.text = description
        binding.btnAlarmDismiss.setOnClickListener {
            mediaPlayer.stop()
            windowManager.removeView(binding.root)
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
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

        // Specify the view position
        params.gravity =
            Gravity.TOP + Gravity.CENTER_HORIZONTAL
        params.x = 0
        params.y = 25

        // Add the view to the window
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.addView(binding.root, params)

        return super.onStartCommand(intent, flags, startId)
    }

    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val res: Resources = resources
        val config: Configuration = res.configuration
        config.setLocale(locale)
        res.updateConfiguration(config, res.displayMetrics)
    }
}
