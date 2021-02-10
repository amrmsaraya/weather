package com.github.amrmsaraya.weather.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.adapters.DailyAdapter
import com.github.amrmsaraya.weather.adapters.HourlyAdapter
import com.github.amrmsaraya.weather.api.RetrofitInstance
import com.github.amrmsaraya.weather.api.WeatherService
import com.github.amrmsaraya.weather.databinding.FragmentHomeBinding
import com.github.amrmsaraya.weather.models.Current
import com.github.amrmsaraya.weather.models.WeatherResponse
import com.github.matteobattilana.weather.PrecipType
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var retrofitService: WeatherService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)
        MainActivity.binding.navView.setCheckedItem(R.id.navHome)
        MainActivity.binding.tvTitle.text = "Giza, Egypt"

        val formatter = SimpleDateFormat("E, dd MMM")
        val date = formatter.format(Date(System.currentTimeMillis()))
        binding.tvDate.text = date

        retrofitService = RetrofitInstance.getRetrofitInstance().create(WeatherService::class.java)
        getWeatherData()

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvHourly.layoutManager = layoutManager
        binding.rvHourly.adapter = context?.let { HourlyAdapter(it) }

        binding.rvDaily.layoutManager = LinearLayoutManager(context)
        binding.rvDaily.adapter = context?.let { DailyAdapter(it) }

        return binding.root
    }


    // get weather data from API
    private fun getWeatherData() {
        val responseLiveData: LiveData<Response<WeatherResponse>> = liveData {
            val response = retrofitService.getWeather(
                29.999307,
                31.184922,
                "minutely",
                "metric",
                "en",
                getString(R.string.open_weather_map_app_id)
            )
            emit(response)
        }

        responseLiveData.observe(viewLifecycleOwner, Observer {
            val responseBody = it.body()
            if (responseBody != null) {
                showCurrentData(responseBody.current)
                val hourlyAdapter = binding.rvHourly.adapter as HourlyAdapter
                hourlyAdapter.setSunriseAndSunset(responseBody.daily)
                hourlyAdapter.submitList(responseBody.hourly.subList(0, 23))

                val dailyAdapter = binding.rvDaily.adapter as DailyAdapter
                dailyAdapter.submitList(responseBody.daily.subList(1, responseBody.daily.size - 1))
            }
        })
    }

    // show current weather data
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showCurrentData(current: Current) {
        val weather: PrecipType
        var rate = 100f
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
                weather = PrecipType.CLEAR
                if (Calendar.getInstance().timeInMillis / 1000 in current.sunrise until current.sunset) {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.clear_day))
                } else {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.clear_night))
                }
            }

            "Clouds" -> {
                weather = PrecipType.CLEAR
                if (Calendar.getInstance().timeInMillis / 1000 in current.sunrise until current.sunset) {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.cloudy_day))
                } else {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.cloudy_night))
                }
            }

            "Drizzle" -> {
                weather = PrecipType.RAIN
                rate = 25f
                if (Calendar.getInstance().timeInMillis / 1000 in current.sunrise until current.sunset) {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.rainy_day))
                } else {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.rainy_night))
                }
            }

            "Rain" -> {
                weather = PrecipType.RAIN
                rate = 100f
                if (Calendar.getInstance().timeInMillis / 1000 in current.sunrise until current.sunset) {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.rainy_day))
                } else {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.rainy_night))
                }
            }

            "Snow" -> {
                weather = PrecipType.SNOW
                binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.snow))
            }
            "Thunderstorm" -> {
                weather = PrecipType.RAIN
                rate = 50f
                binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.storm))
            }

            else -> {
                weather = PrecipType.CLEAR
                if (Calendar.getInstance().timeInMillis / 1000 in current.sunrise until current.sunset) {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.foggy_day))
                } else {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.foggy_night))
                }
            }
        }

        MainActivity.binding.wvWeatherView.apply {
            setWeatherData(weather)
            speed = 500
            emissionRate = rate
            angle = 20
            fadeOutPercent = 1.0f
        }
    }

}
