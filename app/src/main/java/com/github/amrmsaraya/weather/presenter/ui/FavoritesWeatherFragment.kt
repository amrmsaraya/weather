package com.github.amrmsaraya.weather.presenter.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.local.WeatherDatabase
import com.github.amrmsaraya.weather.data.models.Alerts
import com.github.amrmsaraya.weather.data.models.Current
import com.github.amrmsaraya.weather.data.models.WeatherAnimation
import com.github.amrmsaraya.weather.databinding.FragmentHomeBinding
import com.github.amrmsaraya.weather.presenter.adapters.DailyAdapter
import com.github.amrmsaraya.weather.presenter.adapters.HourlyAdapter
import com.github.amrmsaraya.weather.presenter.viewModel.SharedViewModel
import com.github.amrmsaraya.weather.presenter.viewModel.WeatherViewModel
import com.github.amrmsaraya.weather.repositories.WeatherRepo
import com.github.amrmsaraya.weather.utils.SharedViewModelFactory
import com.github.amrmsaraya.weather.utils.WeatherViewModelFactory
import com.github.matteobattilana.weather.PrecipType
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class FavoritesWeatherFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)

        val weatherDao = WeatherDatabase.getInstance(requireActivity().application).weatherDao()
        val weatherRepo = WeatherRepo(weatherDao)
        val weatherFactory = WeatherViewModelFactory(weatherRepo)
        val sharedFactory = SharedViewModelFactory(requireContext())

        weatherViewModel =
            ViewModelProvider(requireActivity(), weatherFactory).get(WeatherViewModel::class.java)
        sharedViewModel =
            ViewModelProvider(requireActivity(), sharedFactory).get(SharedViewModel::class.java)

        sharedViewModel.setCurrentFragment("FavoriteWeather")
        sharedViewModel.setActionBarTitle(sharedViewModel.clickedFavoriteLocation.value.name)

        // SwipeRefresh
        binding.swipeRefresh.setProgressBackgroundColorSchemeColor(Color.parseColor("#FF313131"))
        binding.swipeRefresh.setColorSchemeColors(
            Color.parseColor("#FFFF00e4"),
            Color.parseColor("#FF6011F4"),
        )
        binding.swipeRefresh.setOnRefreshListener {
            lifecycleScope.launchWhenStarted {
                weatherViewModel.getLiveWeather(
                    sharedViewModel.clickedFavoriteLocation.value.lat,
                    sharedViewModel.clickedFavoriteLocation.value.lon,
                    lang = sharedViewModel.langUnit.value
                )
            }
        }

        binding.tvDate.text =
            SimpleDateFormat("E, dd MMM", Locale.US).format(Date(System.currentTimeMillis()))

        weatherViewModel.getLiveWeather(
            sharedViewModel.clickedFavoriteLocation.value.lat,
            sharedViewModel.clickedFavoriteLocation.value.lon
        )

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHourly.layoutManager = layoutManager
        binding.rvHourly.adapter = HourlyAdapter(requireContext(), sharedViewModel)

        binding.rvDaily.layoutManager = LinearLayoutManager(context)
        binding.rvDaily.adapter = DailyAdapter(requireContext(), sharedViewModel)

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

        return binding.root
    }

    // show current weather data
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showCurrentData(current: Current) {
        val animationType: PrecipType
        var emissionRate = 100f
        var temp = current.temp

        when (sharedViewModel.tempUnit.value) {
            "Celsius" -> {
                binding.tvTempUnit.text = "°C"
            }
            "Kelvin" -> {
                temp += 273.15
                binding.tvTempUnit.text = "°K"
            }
            "Fahrenheit" -> {
                temp = (temp * 1.8) + 32
                binding.tvTempUnit.text = "°F"
            }
        }

        when (sharedViewModel.windUnit.value) {
            "Meter / Sec" -> binding.tvWindSpeed.text = "${current.wind_speed} m/s"
            "Mile / Hour" -> binding.tvWindSpeed.text =
                "${"%.2f".format(current.wind_speed * 2.236936)} mph"
        }

        binding.tvTemp.text = temp.roundToInt().toString()
        binding.tvDescription.text =
            current.weather[0].description.capitalize(Locale.ROOT)
        binding.tvPressure.text = "${current.pressure} hpa"
        binding.tvHumidity.text = "${current.humidity} %"
        binding.tvClouds.text = "${current.clouds} %"
        binding.tvUltraviolet.text = current.uvi.toString()
        binding.tvVisibility.text = "${current.visibility} m"

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
        val weatherResponse = weatherViewModel.getCachedLocationWeather(
            sharedViewModel.clickedFavoriteLocation.value.lat,
            sharedViewModel.clickedFavoriteLocation.value.lon
        )
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
}
