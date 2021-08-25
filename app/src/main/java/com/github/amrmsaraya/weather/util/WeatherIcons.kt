package com.github.amrmsaraya.weather.util

import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.models.forecast.Daily

object WeatherIcons {

    private var tomorrowTime = 0
    private var todaySunrise = 0
    private var todaySunset = 0
    private var tomorrowSunrise = 0
    private var tomorrowSunset = 0
    private var sunrise = 0
    private var sunset = 0

    private fun setSunriseSunset(time: Int, today: Daily, tomorrow: Daily) {
        todaySunrise = today.sunrise
        todaySunset = today.sunset
        tomorrowSunrise = tomorrow.sunrise
        tomorrowSunset = tomorrow.sunset
        tomorrowTime = tomorrow.dt

        if (time < tomorrowTime - 72000) {
            sunrise = todaySunrise
            sunset = todaySunset
        } else {
            sunrise = tomorrowSunrise
            sunset = tomorrowSunset
        }
    }

    fun getCurrentIcon(weather: String, sunrise: Int, sunset: Int): Int {
        return when (weather) {
            "Clear" -> {
                if (System.currentTimeMillis() / 1000 in sunrise until sunset) {
                    R.drawable.clear_day
                } else {
                    R.drawable.clear_night
                }
            }
            "Clouds" -> {
                if (System.currentTimeMillis() / 1000 in sunrise until sunset) {
                    R.drawable.cloudy_day
                } else {
                    R.drawable.cloudy_night
                }
            }
            "Drizzle" -> {
                if (System.currentTimeMillis() / 1000 in sunrise until sunset) {
                    R.drawable.rainy_day
                } else {
                    R.drawable.rainy_night
                }
            }
            "Rain" -> {
                if (System.currentTimeMillis() / 1000 in sunrise until sunset) {
                    R.drawable.rainy_day
                } else {
                    R.drawable.rainy_night
                }
            }
            "Snow" -> {
                R.drawable.snow
            }
            "Thunderstorm" -> {
                R.drawable.storm
            }
            else -> {
                if (System.currentTimeMillis() / 1000 in sunrise until sunset) {
                    R.drawable.foggy_day
                } else {
                    R.drawable.foggy_night
                }
            }
        }
    }

    fun getHourlyIcon(today: Daily, tomorrow: Daily, weather: String, time: Int): Int {
        setSunriseSunset(time, today, tomorrow)
        return when (weather) {
            "Clear" -> {
                if (time.toLong() in sunrise until sunset) {
                    R.drawable.clear_day_24
                } else {
                    R.drawable.clear_night_24
                }
            }
            "Clouds" -> {
                if (time.toLong() in sunrise until sunset) {
                    R.drawable.cloudy_day_24
                } else {
                    R.drawable.cloudy_night_24
                }
            }
            "Drizzle" -> {
                if (time.toLong() in sunrise until sunset) {
                    R.drawable.rainy_day_24
                } else {
                    R.drawable.rainy_night_24
                }
            }
            "Rain" -> {
                if (time.toLong() in sunrise until sunset) {
                    R.drawable.rainy_day_24
                } else {
                    R.drawable.rainy_night_24
                }
            }
            "Snow" -> {
                R.drawable.snow_24
            }
            "Thunderstorm" -> {
                R.drawable.storm_24
            }
            else -> {
                if (time.toLong() in sunrise until sunset) {
                    R.drawable.foggy_day_24
                } else {
                    R.drawable.foggy_night_24
                }
            }
        }
    }

    fun getDailyIcon(weather: String): Int {
        return when (weather) {
            "Clear" -> R.drawable.clear_day_24
            "Clouds" -> R.drawable.cloudy_day_24
            "Drizzle" -> R.drawable.rainy_day_24
            "Rain" -> R.drawable.rainy_day_24
            "Snow" -> R.drawable.snow_24
            "Thunderstorm" -> R.drawable.storm_24
            else -> R.drawable.foggy_day_24
        }
    }
}
