package com.github.amrmsaraya.weather

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.github.amrmsaraya.weather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.parseColor("#FF1F1F1F")
        window.navigationBarColor = Color.parseColor("#FF1F1F1F")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
}
