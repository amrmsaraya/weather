package com.github.amrmsaraya.weather.presenter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.models.Alarm
import com.github.amrmsaraya.weather.databinding.AlertsItemBinding
import java.text.SimpleDateFormat
import java.util.*

class AlarmAdapter(
    private val onSwitchStatusChanged: (Alarm) -> Unit,
    private val onDeleteClicked: (Alarm) -> Unit
) :
    ListAdapter<Alarm, AlarmAdapter.AlarmViewHolder>(
        AlarmDiffUtil
    ) {
    inner class AlarmViewHolder(val binding: AlertsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        return AlarmViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.alerts_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = getItem(position)

        holder.binding.switchAlert.isChecked = getItem(position).isChecked
        holder.binding.switchAlert.setOnCheckedChangeListener { buttonView, isChecked ->

            alarm.isChecked = isChecked
            onSwitchStatusChanged(alarm)
        }

        val popupMenu = PopupMenu(holder.binding.root.context, holder.binding.root)
        popupMenu.inflate(R.menu.favorite_menu)
        popupMenu.setOnMenuItemClickListener {
            onDeleteClicked(alarm)
            true
        }

        holder.binding.root.setOnLongClickListener {
            popupMenu.show()
            true
        }

        holder.binding.tvAlarmFromTime.text =
            SimpleDateFormat("h:mm a", Locale.US).format(getItem(position).start)
        holder.binding.tvAlarmFromDate.text =
            SimpleDateFormat("dd MMM", Locale.US).format(getItem(position).start)
        holder.binding.tvAlarmToTime.text =
            SimpleDateFormat("h:mm a", Locale.US).format(getItem(position).end)
        holder.binding.tvAlarmToDate.text =
            SimpleDateFormat("dd MMM", Locale.US).format(getItem(position).end)
    }
}

object AlarmDiffUtil : DiffUtil.ItemCallback<Alarm>() {
    override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
        return oldItem == newItem
    }

}
