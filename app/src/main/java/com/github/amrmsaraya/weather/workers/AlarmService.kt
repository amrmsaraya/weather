package com.github.amrmsaraya.weather.workers

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.databinding.DialogAlarmBinding

class AlarmService : Service() {
    private lateinit var binding: DialogAlarmBinding
    private lateinit var windowManager: WindowManager
    private lateinit var params: WindowManager.LayoutParams
    private lateinit var mediaPlayer: MediaPlayer

    override fun onBind(intent: Intent?): IBinder? {
        throw  UnsupportedOperationException("Not yet implemented");
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(applicationContext),
            R.layout.dialog_alarm,
            null,
            false
        )

        val event = intent?.getStringExtra("event") ?: "Unknown"
        val description = intent?.getStringExtra("description") ?: "Unknown"

        binding.tvAlarmTitle.text = event
        binding.tvAlarmDescription.text = description
        binding.btnAlarmDismiss.setOnClickListener {
            mediaPlayer.stop()
            windowManager.removeView(binding.root)
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
}
