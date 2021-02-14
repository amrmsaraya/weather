package com.github.amrmsaraya.weather.presenter.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.local.WeatherDatabase
import com.github.amrmsaraya.weather.data.models.Alerts
import com.github.amrmsaraya.weather.data.models.Current
import com.github.amrmsaraya.weather.data.models.Location
import com.github.amrmsaraya.weather.data.models.WeatherAnimation
import com.github.amrmsaraya.weather.databinding.FragmentHomeBinding
import com.github.amrmsaraya.weather.presenter.adapters.DailyAdapter
import com.github.amrmsaraya.weather.presenter.adapters.HourlyAdapter
import com.github.amrmsaraya.weather.presenter.viewModel.LocationViewModel
import com.github.amrmsaraya.weather.presenter.viewModel.SharedViewModel
import com.github.amrmsaraya.weather.presenter.viewModel.WeatherViewModel
import com.github.amrmsaraya.weather.repositories.LocationRepo
import com.github.amrmsaraya.weather.repositories.WeatherRepo
import com.github.amrmsaraya.weather.utils.LocationViewModelFactory
import com.github.amrmsaraya.weather.utils.WeatherViewModelFactory
import com.github.matteobattilana.weather.PrecipType
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var geocoder: Geocoder
    private var addresses = mutableListOf<Address>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermissionCode = 2
    private var lon = 0.0
    private var lat = 0.0

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)
        dataStore = requireContext().createDataStore("settings")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        geocoder = Geocoder(context, Locale.getDefault())


        // Dao
        val weatherDao = WeatherDatabase.getInstance(requireActivity().application).weatherDao()
        val locationDao = WeatherDatabase.getInstance(requireActivity().application).locationDao()

        // Repo
        val weatherRepo = WeatherRepo(weatherDao)
        val locationRepo = LocationRepo(locationDao)

        // Factory
        val weatherFactory = WeatherViewModelFactory(weatherRepo)
        val locationFactory = LocationViewModelFactory(locationRepo)

        // ViewModels
        weatherViewModel =
            ViewModelProvider(requireActivity(), weatherFactory).get(WeatherViewModel::class.java)
        locationViewModel =
            ViewModelProvider(requireActivity(), locationFactory).get(LocationViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        sharedViewModel.setCurrentFragment("Home")
        sharedViewModel.setActionBarVisibility("Show")

        getCachedLocation()


        // SwipeRefresh
        binding.swipeRefresh.setProgressBackgroundColorSchemeColor(Color.parseColor("#FF313131"))
        binding.swipeRefresh.setColorSchemeColors(
            Color.parseColor("#FFFF00e4"),
            Color.parseColor("#FF6011F4"),
        )
        binding.swipeRefresh.setOnRefreshListener {
            lifecycleScope.launchWhenStarted {
                if (readDataStore("location") == "GPS") {
                    getLocationFromGPS()
                }
            }
            weatherViewModel.getLiveWeather(lat, lon)
        }

        binding.tvDate.text =
            SimpleDateFormat("E, dd MMM", Locale.US).format(Date(System.currentTimeMillis()))

        lifecycleScope.launchWhenStarted {
            if (readDataStore("location") == "GPS") {
                getLocationFromGPS()
            }
            weatherViewModel.getLiveWeather(lat, lon)
        }

        // Get Retrofit Response
        lifecycleScope.launchWhenStarted {
            weatherViewModel.weatherResponse.collect {
                when (it) {
                    is WeatherRepo.ResponseState.Success -> {
                        if (it.weatherResponse.alerts == null) {
                            it.weatherResponse.alerts = listOf(Alerts())
                        }
                        weatherViewModel.insert(it.weatherResponse)
                        getCachedWeather()
                    }
                    is WeatherRepo.ResponseState.Error ->
                        Snackbar.make(
                            binding.root,
                            it.message,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    else -> Unit
                }
                binding.swipeRefresh.isRefreshing = false
                weatherViewModel.weatherResponse.value = WeatherRepo.ResponseState.Empty
            }
        }

        // Get Cached Weather
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            weatherViewModel.getAllCachedWeather().collect {
                getCachedWeather()
            }
        }

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvHourly.layoutManager = layoutManager
        binding.rvHourly.adapter = context?.let { HourlyAdapter(it) }

        binding.rvDaily.layoutManager = LinearLayoutManager(context)
        binding.rvDaily.adapter = context?.let { DailyAdapter(it) }

        return binding.root
    }


    // show current weather data
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showCurrentData(current: Current) {
        val animationType: PrecipType
        var emissionRate = 100f

        binding.tvTemp.text = current.temp.roundToInt().toString()
        binding.tvDescription.text =
            current.weather[0].description.capitalize(Locale.ROOT)
        binding.tvPressure.text = "${current.pressure} hpa"
        binding.tvHumidity.text = "${current.humidity} %"
        binding.tvWindSpeed.text = "${current.wind_speed} m/s"
        binding.tvClouds.text = "${current.clouds} %"
        binding.tvUltraviolet.text = current.uvi.toString()
        binding.tvVisibility.text = "${current.humidity} m"

        when (current.weather[0].main) {
            "Clear" -> {
                animationType = PrecipType.CLEAR
                if (Calendar.getInstance().timeInMillis / 1000 in current.sunrise until current.sunset) {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.clear_day))
                } else {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.clear_night))
                }
            }

            "Clouds" -> {
                animationType = PrecipType.CLEAR
                if (Calendar.getInstance().timeInMillis / 1000 in current.sunrise until current.sunset) {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.cloudy_day))
                } else {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.cloudy_night))
                }
            }

            "Drizzle" -> {
                animationType = PrecipType.RAIN
                emissionRate = 25f
                if (Calendar.getInstance().timeInMillis / 1000 in current.sunrise until current.sunset) {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.rainy_day))
                } else {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.rainy_night))
                }
            }

            "Rain" -> {
                animationType = PrecipType.RAIN
                emissionRate = 100f
                if (Calendar.getInstance().timeInMillis / 1000 in current.sunrise until current.sunset) {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.rainy_day))
                } else {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.rainy_night))
                }
            }

            "Snow" -> {
                animationType = PrecipType.SNOW
                binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.snow))
            }
            "Thunderstorm" -> {
                animationType = PrecipType.RAIN
                emissionRate = 50f
                binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.storm))
            }
            else -> {
                animationType = PrecipType.CLEAR
                if (Calendar.getInstance().timeInMillis / 1000 in current.sunrise until current.sunset) {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.foggy_day))
                } else {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.foggy_night))
                }
            }
        }
        sharedViewModel.setWeatherAnimation(WeatherAnimation(animationType, emissionRate))

    }

    private suspend fun getCachedWeather() {
        val weatherResponse = weatherViewModel.getCachedLocationWeather(lat, lon)
        if (weatherResponse != null) {
            val hourlyAdapter = binding.rvHourly.adapter as HourlyAdapter
            val dailyAdapter = binding.rvDaily.adapter as DailyAdapter
            // Show Current Data
            showCurrentData(weatherResponse.current)
            // Send sunrise and sunset time to Adapter
            hourlyAdapter.setSunriseAndSunset(weatherResponse.daily)
            // Add List to Hourly Adapter
            hourlyAdapter.submitList(weatherResponse.hourly.subList(0, 23))
            // Add List to Daily Adapter
            dailyAdapter.submitList(
                weatherResponse.daily.subList(
                    1,
                    weatherResponse.daily.size - 1
                )
            )
        }
    }

    private fun getCachedLocation() {
        lifecycleScope.launchWhenStarted {
            try {
                locationViewModel.getLocation(1).collect {
                    weatherViewModel.getLiveWeather(it.lat, it.lon)
                    lat = it.lat
                    lon = it.lon
                    sharedViewModel.setActionBarTitle(it.name)
                }
            } catch (e: Exception) {
                locationViewModel.insert(
                    Location(
                        roundDouble(lat),
                        roundDouble(lon),
                        "Current",
                        1
                    )
                )
                locationViewModel.getLocation(1).collect {
                    weatherViewModel.getLiveWeather(it.lat, it.lon)
                    lat = it.lat
                    lon = it.lon
                    sharedViewModel.setActionBarTitle(it.name)
                }
            }
        }
    }

    private fun roundDouble(double: Double): Double {
        return "%.4f".format(double).toDouble()
    }

    private suspend fun saveDataStore(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun readDataStore(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]

    }

    private fun getLocationFromGPS() {
        if (checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
            return
        }
        fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
            null
        ).addOnSuccessListener {
            if (it != null) {
                addresses = geocoder.getFromLocation(
                    it.latitude,
                    it.longitude,
                    1
                )
                val city = if (addresses[0].locality.isNullOrEmpty()) {
                    addresses[0].adminArea
                } else {
                    addresses[0].locality
                }
                locationViewModel.insert(
                    Location(
                        roundDouble(it.latitude),
                        roundDouble(it.longitude),
                        city,
                        1
                    )
                )
                weatherViewModel.getLiveWeather(
                    roundDouble(it.latitude),
                    roundDouble(it.longitude),
                )
            } else {
                Snackbar.make(binding.root, "Failed to get Location", Snackbar.LENGTH_SHORT).show()

            }
        }.addOnFailureListener {
            Snackbar.make(binding.root, "Failed to get location", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationFromGPS()
                lifecycleScope.launchWhenStarted {
                    if (readDataStore("location").isNullOrEmpty()) {
                        lifecycleScope.launchWhenStarted {
                            saveDataStore("location", "GPS")
                            saveDataStore("language", "English")
                            saveDataStore("temperature", "Celsius")
                            saveDataStore("windSpeed", "Meter / Sec")
                            Log.i("myTag", "Default Settings have been created!")
                        }
                    }
                }

            } else {
                Snackbar.make(binding.root, "Permission Denied", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}
