package com.github.amrmsaraya.weather.presenter.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.local.WeatherDatabase
import com.github.amrmsaraya.weather.data.models.Alerts
import com.github.amrmsaraya.weather.data.models.Current
import com.github.amrmsaraya.weather.data.models.WeatherAnimation
import com.github.amrmsaraya.weather.databinding.FragmentHomeBinding
import com.github.amrmsaraya.weather.repositories.WeatherRepo
import com.github.amrmsaraya.weather.presenter.adapters.DailyAdapter
import com.github.amrmsaraya.weather.presenter.adapters.HourlyAdapter
import com.github.amrmsaraya.weather.presenter.viewModel.SharedViewModel
import com.github.amrmsaraya.weather.presenter.viewModel.WeatherViewModel
import com.github.amrmsaraya.weather.utils.WeatherViewModelFactory
import com.github.matteobattilana.weather.PrecipType
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)
        dataStore = requireContext().createDataStore("settings")

        val dao = WeatherDatabase.getInstance(requireActivity().application).weatherDao()
        val repo = WeatherRepo(dao)
        val factory = WeatherViewModelFactory(requireContext(), repo)
        weatherViewModel =
            ViewModelProvider(requireActivity(), factory).get(WeatherViewModel::class.java)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        sharedViewModel.actionBarTitle.value = "Giza, Egypt"
        sharedViewModel.currentFragment.value = "Home"
        sharedViewModel.actionBarVisibility.value = "Show"

        binding.tvDate.text =
            SimpleDateFormat("E, dd MMM", Locale.US).format(Date(System.currentTimeMillis()))

        weatherViewModel.getApiWeather(29.999307, 31.184922).observe(requireActivity(), Observer {
            if (it != null) {
                weatherViewModel.deleteAll()
                if (it.alerts == null) {
                    it.alerts = listOf(Alerts())
                }
                weatherViewModel.insert(it)
            }
        })

        weatherViewModel.getCachedWeather(roundDouble(29.999307), roundDouble(31.184922))
            .observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    val hourlyAdapter = binding.rvHourly.adapter as HourlyAdapter
                    val dailyAdapter = binding.rvDaily.adapter as DailyAdapter
                    // Show Current Data
                    showCurrentData(it.current)
                    // Send sunrise and sunset time to Adapter
                    hourlyAdapter.setSunriseAndSunset(it.daily)
                    // Add List to Hourly Adapter
                    hourlyAdapter.submitList(it.hourly.subList(0, 23))
                    // Add List to Daily Adapter
                    dailyAdapter.submitList(it.daily.subList(1, it.daily.size - 1))
                }
            })

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
        sharedViewModel.weatherAnimation.value = WeatherAnimation(animationType, emissionRate)

    }

    private fun roundDouble(double: Double): Double {
        return "%.4f".format(double).toDouble()
    }

    private suspend fun readDataStore(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]

    }

}
