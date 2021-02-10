package com.github.amrmsaraya.weather.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.databinding.HourlyItemBinding
import com.github.amrmsaraya.weather.models.Daily
import com.github.amrmsaraya.weather.models.Hourly
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class HourlyAdapter(private val context: Context) :
    ListAdapter<Hourly, HourlyAdapter.HourlyViewHolder>(HourlyDiffUtil()) {
    private var tomorrow = 0
    private var todaySunrise = 0
    private var todaySunset = 0
    private var tomorrowSunrise = 0
    private var tomorrowSunset = 0
    private var sunrise = 0
    private var sunset = 0

    inner class HourlyViewHolder(val binding: HourlyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        return HourlyViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.hourly_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val formatter = SimpleDateFormat("h a")
        val time = formatter.format(Date(getItem(position).dt.toLong() * 1000))
        holder.binding.tvHourlyTemp.text = getItem(position).temp.roundToInt().toString()
        holder.binding.tvHourlyTime.text = time
        if (getItem(position).dt < tomorrow - 72000) {
            sunrise = todaySunrise
            sunset = todaySunset
        } else {
            sunrise = tomorrowSunrise
            sunset = tomorrowSunset
        }
        when (getItem(position).weather[0].main) {
            "Clear" -> {
                if (getItem(position).dt.toLong() in sunrise until sunset) {
                    holder.binding.ivHourlyIcon.setImageDrawable(context.getDrawable(R.drawable.clear_day_24))
                } else {
                    holder.binding.ivHourlyIcon.setImageDrawable(context.getDrawable(R.drawable.clear_night_24))
                }
            }
            "Clouds" -> {
                if (getItem(position).dt.toLong() in sunrise until sunset) {
                    holder.binding.ivHourlyIcon.setImageDrawable(context.getDrawable(R.drawable.cloudy_day_24))
                } else {
                    holder.binding.ivHourlyIcon.setImageDrawable(context.getDrawable(R.drawable.cloudy_night_24))
                }
            }
            "Drizzle" -> {
                if (getItem(position).dt.toLong() in sunrise until sunset) {
                    holder.binding.ivHourlyIcon.setImageDrawable(context.getDrawable(R.drawable.rainy_day_24))
                } else {
                    holder.binding.ivHourlyIcon.setImageDrawable(context.getDrawable(R.drawable.rainy_night_24))
                }
            }
            "Rain" -> {
                if (getItem(position).dt.toLong() in sunrise until sunset) {
                    holder.binding.ivHourlyIcon.setImageDrawable(context.getDrawable(R.drawable.rainy_day_24))
                } else {
                    holder.binding.ivHourlyIcon.setImageDrawable(context.getDrawable(R.drawable.rainy_night_24))
                }
            }
            "Snow" -> {
                holder.binding.ivHourlyIcon.setImageDrawable(context.getDrawable(R.drawable.snow_24))
            }
            "Thunderstorm" -> {
                holder.binding.ivHourlyIcon.setImageDrawable(context.getDrawable(R.drawable.storm_24))
            }
            else -> {
                if (getItem(position).dt.toLong() in sunrise until sunset) {
                    holder.binding.ivHourlyIcon.setImageDrawable(context.getDrawable(R.drawable.foggy_day_24))
                } else {
                    holder.binding.ivHourlyIcon.setImageDrawable(context.getDrawable(R.drawable.foggy_night_24))
                }
            }
        }

    }

    fun setSunriseAndSunset(daily: List<Daily>) {
        todaySunrise = daily[0].sunrise
        todaySunset = daily[0].sunset
        tomorrowSunrise = daily[1].sunrise
        tomorrowSunset = daily[1].sunset
        tomorrow = daily[1].dt
    }
}

class HourlyDiffUtil : DiffUtil.ItemCallback<Hourly>() {
    override fun areItemsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
        return oldItem == newItem
    }

}
