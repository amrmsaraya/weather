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
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.Current
import com.github.amrmsaraya.weather.data.WeatherBase
import com.github.amrmsaraya.weather.databinding.FragmentHomeBinding
import com.github.amrmsaraya.weather.retrofit.RetrofitInstance
import com.github.amrmsaraya.weather.retrofit.WeatherService
import com.github.matteobattilana.weather.PrecipType
import retrofit2.Response
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
        binding.tvDate.text = getCurrentDate()

        retrofitService = RetrofitInstance.getRetrofitInstance().create(WeatherService::class.java)
        getWeatherData()

        return binding.root
    }


    // get weather data from API
    private fun getWeatherData() {
        val responseLiveData: LiveData<Response<WeatherBase>> = liveData {
            val response = retrofitService.getWeather(
                33.441792,
                -94.037689,
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
            current.weather[0].description.capitalize()
        binding.tvPressure.text = "${current.pressure} hpa"
        binding.tvHumidity.text = "${current.humidity} %"
        binding.tvWindSpeed.text = "${current.wind_speed} m/s"
        binding.tvClouds.text = "${current.clouds} %"
        binding.tvUltraviolet.text = current.uvi.toString()
        binding.tvVisibility.text = "${current.humidity} m"

        when (current.weather[0].main) {
            "Clear" -> {
                weather = PrecipType.CLEAR
                if (Calendar.getInstance().timeInMillis in current.sunrise until current.sunset) {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.clear_day))
                } else {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.clear_night))
                }
            }

            "Clouds" -> {
                weather = PrecipType.CLEAR
                if (Calendar.getInstance().timeInMillis in current.sunrise until current.sunset) {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.cloudy_day))
                } else {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.cloudy_night))
                }
            }

            "Drizzle" -> {
                weather = PrecipType.RAIN
                rate = 25f
                if (Calendar.getInstance().timeInMillis in current.sunrise until current.sunset) {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.rainy_day))
                } else {
                    binding.ivIcon.setImageDrawable(context?.getDrawable(R.drawable.rainy_night))
                }
            }

            "Rain" -> {
                weather = PrecipType.RAIN
                rate = 100f
                if (Calendar.getInstance().timeInMillis in current.sunrise until current.sunset) {
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
                if (Calendar.getInstance().timeInMillis in current.sunrise until current.sunset) {
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

    private fun getCurrentDate(): String {
        var dayOfWeek = "Saturday"
        var dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        var month = "Jan"

        when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            Calendar.SATURDAY -> dayOfWeek = "Saturday"
            Calendar.SUNDAY -> dayOfWeek = "Sunday"
            Calendar.MONDAY -> dayOfWeek = "Monday"
            Calendar.TUESDAY -> dayOfWeek = "Tuesday"
            Calendar.WEDNESDAY -> dayOfWeek = "Wednesday"
            Calendar.THURSDAY -> dayOfWeek = "Thursday"
            Calendar.FRIDAY -> dayOfWeek = "Friday"
        }

        when (Calendar.getInstance().get(Calendar.MONTH)) {
            Calendar.JANUARY -> month = "Jan"
            Calendar.FEBRUARY -> month = "Feb"
            Calendar.MARCH -> month = "Mar"
            Calendar.APRIL -> month = "Apr"
            Calendar.MAY -> month = "May"
            Calendar.JUNE -> month = "June"
            Calendar.JULY -> month = "July"
            Calendar.AUGUST -> month = "Aug"
            Calendar.SEPTEMBER -> month = "Sept"
            Calendar.OCTOBER -> month = "Oct"
            Calendar.NOVEMBER -> month = "Nov"
            Calendar.DECEMBER -> month = "Dec"
        }
        return "$dayOfWeek, $dayOfMonth $month"
    }
}
