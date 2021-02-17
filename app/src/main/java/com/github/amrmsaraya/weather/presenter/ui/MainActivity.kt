package com.github.amrmsaraya.weather.presenter.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.models.WeatherAnimation
import com.github.amrmsaraya.weather.databinding.ActivityMainBinding
import com.github.amrmsaraya.weather.presenter.viewModel.SharedViewModel
import com.github.amrmsaraya.weather.utils.SharedViewModelFactory
import com.github.matteobattilana.weather.PrecipType
import kotlinx.coroutines.flow.collect
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var dataStore: DataStore<Preferences>
    private var currentFragment = ""
    private var mapStatus = ""

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Restore Default Theme
        setTheme(R.style.Theme_Weather)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        dataStore = applicationContext.createDataStore("settings")

        lifecycleScope.launchWhenStarted {
            getLocationProvider()
        }

        // Set Custom ActionBar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setBackgroundDrawable(getDrawable(R.drawable.gray_square))

        navController = findNavController(R.id.fragment)
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharedFactory = SharedViewModelFactory(baseContext)

        sharedViewModel = ViewModelProvider(this, sharedFactory).get(SharedViewModel::class.java)

        lifecycleScope.launchWhenStarted {
            sharedViewModel.isPermissionGranted.collect {
                if (!it) {
                    sharedViewModel.setPermissionGranted(true)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            sharedViewModel.actionBarTitle.collect {
                binding.tvTitle.text = it
            }
        }

        lifecycleScope.launchWhenStarted {
            sharedViewModel.actionBarVisibility.collect {
                when (it) {
                    true -> supportActionBar?.show()
                    false -> supportActionBar?.hide()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            sharedViewModel.currentFragment.collect {
                currentFragment = it
                when (it) {
                    "Home" -> binding.navView.setCheckedItem(R.id.navHome)
                    "Favorites" -> binding.navView.setCheckedItem(R.id.navFavorites)
                    "Alerts" -> binding.navView.setCheckedItem(R.id.navAlerts)
                    "Settings" -> binding.navView.setCheckedItem(R.id.navSettings)
                    "FavoriteWeather" -> binding.navView.setCheckedItem(R.id.navFavorites)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            sharedViewModel.mapStatus.collect {
                mapStatus = it
            }
        }

        lifecycleScope.launchWhenStarted {
            sharedViewModel.weatherAnimation.collect {
                binding.wvWeatherView.apply {
                    setWeatherData(it.type)
                    emissionRate = it.emissionRate
                    speed = it.speed
                    angle = it.angel
                    fadeOutPercent = it.fadeOutPercent
                }
            }
        }

        binding.navView.setNavigationItemSelectedListener {
            binding.drawerLayout.closeDrawers()
            when (it.itemId) {
                R.id.navHome -> navController.navigate(R.id.homeFragment)
                R.id.navFavorites -> navController.navigate(R.id.favoritesFragment)
                R.id.navAlerts -> navController.navigate(R.id.alertsFragment)
                R.id.navSettings -> navController.navigate(R.id.settingsFragment)
            }
            true
        }
    }

    private suspend fun getLocationProvider() {
        binding.constrainLayout.visibility = View.GONE
        if (sharedViewModel.readDataStore("location").isNullOrEmpty()) {
            var locationProvider = getString(R.string.gps)
            val locationDialog =
                AlertDialog.Builder(this@MainActivity)
                    .setTitle(getString(R.string.location_provider))
                    .setIcon(R.drawable.location)
                    .setSingleChoiceItems(
                        arrayOf(getString(R.string.gps), getString(R.string.map)),
                        0
                    ) { _, which ->
                        when (which) {
                            0 -> locationProvider = "GPS"
                            1 -> locationProvider = "Map"
                        }
                    }
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->
                        if (locationProvider == "Map") {
                            navController.navigate(R.id.mapsFragment)
                            binding.constrainLayout.visibility = View.VISIBLE
                        } else {
                            lifecycleScope.launchWhenStarted {
                                sharedViewModel.saveDataStore("location", "GPS")
                                navController.navigate(R.id.homeFragment)
                                binding.constrainLayout.visibility = View.VISIBLE
                            }
                        }
                    }
                    .setCancelable(false)
                    .create()
            locationDialog.show()
        } else {
            binding.constrainLayout.visibility = View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        lifecycleScope.launchWhenStarted {
            if (binding.drawerLayout.isDrawerOpen(binding.navView)) {
                binding.drawerLayout.closeDrawers()
            } else if (currentFragment == "Map" && mapStatus == "Current" && sharedViewModel.readDataStore(
                    "location"
                ).isNullOrEmpty()
            ) {
                getLocationProvider()
            } else if (currentFragment == "FavoriteWeather") {
                sharedViewModel.setWeatherAnimation(WeatherAnimation(PrecipType.CLEAR, 100f))
                super.onBackPressed()
            } else {
                super.onBackPressed()
            }
        }
    }
}

