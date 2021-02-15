package com.github.amrmsaraya.weather.presenter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.models.Daily
import com.github.amrmsaraya.weather.databinding.DailyItemBinding
import com.github.amrmsaraya.weather.presenter.viewModel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class DailyAdapter(private val context: Context, private val sharedViewModel: SharedViewModel) :
    ListAdapter<Daily, DailyAdapter.DailyViewHolder>(DailyDiffUtil()) {
    inner class DailyViewHolder(val binding: DailyItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        return DailyViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.daily_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val formatter = SimpleDateFormat("E")
        val weekday = formatter.format(Date(getItem(position).dt.toLong() * 1000))
        var max = getItem(position).temp.max
        var min = getItem(position).temp.min

        when (sharedViewModel.tempUnit.value) {
            "Celsius" -> {
                holder.binding.tvDailyMaxMinTemp.text =
                    "${max.roundToInt()} / ${min.roundToInt()}°C"
            }
            "Kelvin" -> {
                max += 273.15
                min += 273.15
                holder.binding.tvDailyMaxMinTemp.text =
                    "${max.roundToInt()} / ${min.roundToInt()}°K"
            }
            "Fahrenheit" -> {
                max = (max * 1.8) + 32
                min = (min * 1.8) + 32
                holder.binding.tvDailyMaxMinTemp.text =
                    "${max.roundToInt()} / ${min.roundToInt()}°F"
            }
        }
        when (position) {
            0 -> {
                holder.binding.tvDailyWeekDay.text = "Tomorrow"
                holder.binding.tvDailyContainer.setBackgroundResource(R.drawable.gradient_square)
            }
            else -> holder.binding.tvDailyWeekDay.text = weekday
        }
        holder.binding.tvDailyDescription.text =
            getItem(position).weather[0].description.capitalize()

        when (getItem(position).weather[0].main) {
            "Clear" -> {
                holder.binding.ivDailyIcon.setImageDrawable(context.getDrawable(R.drawable.clear_day_24))
            }
            "Clouds" -> {
                holder.binding.ivDailyIcon.setImageDrawable(context.getDrawable(R.drawable.cloudy_24))
            }
            "Drizzle" -> {
                holder.binding.ivDailyIcon.setImageDrawable(context.getDrawable(R.drawable.rainy_24))
            }
            "Rain" -> {
                holder.binding.ivDailyIcon.setImageDrawable(context.getDrawable(R.drawable.rainy_24))
            }
            "Snow" -> {
                holder.binding.ivDailyIcon.setImageDrawable(context.getDrawable(R.drawable.snow_24))
            }
            "Thunderstorm" -> {
                holder.binding.ivDailyIcon.setImageDrawable(context.getDrawable(R.drawable.storm_24))
            }
            else -> {
                holder.binding.ivDailyIcon.setImageDrawable(context.getDrawable(R.drawable.foggy_day_24))
            }
        }
    }
}

class DailyDiffUtil : DiffUtil.ItemCallback<Daily>() {
    override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem.dt == newItem.dt
    }

    override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem == newItem
    }
}
