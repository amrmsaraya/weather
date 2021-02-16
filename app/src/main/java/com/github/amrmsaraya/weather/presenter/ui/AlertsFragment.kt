package com.github.amrmsaraya.weather.presenter.ui

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.local.WeatherDatabase
import com.github.amrmsaraya.weather.data.models.Alarm
import com.github.amrmsaraya.weather.data.models.WeatherAnimation
import com.github.amrmsaraya.weather.databinding.DialogAddAlertBinding
import com.github.amrmsaraya.weather.databinding.FragmentAlertsBinding
import com.github.amrmsaraya.weather.presenter.adapters.AlarmAdapter
import com.github.amrmsaraya.weather.presenter.viewModel.AlarmViewModel
import com.github.amrmsaraya.weather.presenter.viewModel.SharedViewModel
import com.github.amrmsaraya.weather.repositories.AlarmsRepo
import com.github.amrmsaraya.weather.utils.AlarmViewModelFactory
import com.github.amrmsaraya.weather.utils.SharedViewModelFactory
import com.github.matteobattilana.weather.PrecipType
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*

class AlertsFragment : Fragment() {
    private lateinit var binding: FragmentAlertsBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var alarmViewModel: AlarmViewModel
    private val channelId = "com.github.amrmsaraya.weather.channel1"
    private var notificationManager: NotificationManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var to: Long
        var from: Long

        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_alerts, container, false)

        // Notification
        notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(channelId, "AlertChannel", "Weather Alerts")

        val alarmDao = WeatherDatabase.getInstance(requireActivity().application).alarmDao()
        val alarmRepo = AlarmsRepo(alarmDao)

        val sharedFactory = SharedViewModelFactory(requireContext())
        val alarmFactory = AlarmViewModelFactory(alarmRepo)

        sharedViewModel =
            ViewModelProvider(requireActivity(), sharedFactory).get(SharedViewModel::class.java)
        alarmViewModel =
            ViewModelProvider(requireActivity(), alarmFactory).get(AlarmViewModel::class.java)

        // Reset animation after leaving favorite weather
        if (sharedViewModel.currentFragment.value == "FavoriteWeather") {
            sharedViewModel.setWeatherAnimation(WeatherAnimation(PrecipType.CLEAR, 100f))
        }

        lifecycleScope.launchWhenCreated {
            when (sharedViewModel.readDataStore("notification")) {
                "true" -> binding.switchNotifications.isChecked = true
                "false" -> binding.switchNotifications.isChecked = false
            }
        }

        sharedViewModel.setActionBarTitle("Alerts")
        sharedViewModel.setCurrentFragment("Alerts")

        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launchWhenCreated {
                when (isChecked) {
                    true -> sharedViewModel.saveDataStore("notification", "true")
                    false -> sharedViewModel.saveDataStore("notification", "false")
                }
            }
        }

        binding.fabAddAlert.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            val hour = calendar.get(Calendar.HOUR)
            val minute = calendar.get(Calendar.MINUTE)
            to = System.currentTimeMillis()
            from = System.currentTimeMillis()

            val dialogBinding: DialogAddAlertBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.dialog_add_alert, container, false)
            dialogBinding.tvFromDate.text =
                SimpleDateFormat("dd MMM, y", Locale.US).format(from)
            dialogBinding.tvFromTime.text =
                SimpleDateFormat("h:mm a", Locale.US).format(from)
            dialogBinding.tvToDate.text =
                SimpleDateFormat("dd MMM, y", Locale.US).format(to)
            dialogBinding.tvToTime.text =
                SimpleDateFormat("h:mm a", Locale.US).format(to)

            val alertDialog = AlertDialog.Builder(requireContext())
                .setView(dialogBinding.root)
                .create()
            alertDialog.show()

            dialogBinding.layoutTo.setOnClickListener {
                val datePickerDialog =
                    DatePickerDialog(
                        requireContext(),
                        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                            val timePickerDialog = TimePickerDialog(
                                requireContext(),
                                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                                    val c = Calendar.getInstance()
                                    c.set(year, month, dayOfMonth, hourOfDay, minute)
                                    to = c.timeInMillis
                                    dialogBinding.tvToDate.text =
                                        SimpleDateFormat("dd MMM, y", Locale.US).format(to)
                                    dialogBinding.tvToTime.text =
                                        SimpleDateFormat("h:mm a", Locale.US).format(to)
                                },
                                hour,
                                minute,
                                false
                            )
                            timePickerDialog.show()
                        },
                        year,
                        month,
                        day
                    )
                datePickerDialog.datePicker.minDate = calendar.timeInMillis
                datePickerDialog.show()
            }

            dialogBinding.layoutFrom.setOnClickListener {
                val datePickerDialog =
                    DatePickerDialog(
                        requireContext(),
                        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                            val timePickerDialog = TimePickerDialog(
                                requireContext(),
                                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                                    val c = Calendar.getInstance()
                                    c.set(year, month, dayOfMonth, hourOfDay, minute)
                                    from = c.timeInMillis
                                    dialogBinding.tvFromDate.text =
                                        SimpleDateFormat("dd MMM, y", Locale.US).format(from)
                                    dialogBinding.tvFromTime.text =
                                        SimpleDateFormat("h:mm a", Locale.US).format(from)
                                },
                                hour,
                                minute,
                                false
                            )
                            timePickerDialog.show()
                        },
                        year,
                        month,
                        day
                    )
                datePickerDialog.datePicker.minDate = calendar.timeInMillis
                datePickerDialog.show()
            }
            dialogBinding.btnSaveAlert.setOnClickListener {
                if (to > System.currentTimeMillis()) {
                    lifecycleScope.launchWhenStarted {
                        alarmViewModel.insert(Alarm(from, to, dialogBinding.swSound.isChecked))
                    }
                    alertDialog.dismiss()
                } else {
                    Snackbar.make(binding.root, "Invalid time", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        binding.rvAlerts.adapter = AlarmAdapter({ alarm: Alarm -> onSwitchStatusChanged(alarm) },
            { alarm: Alarm -> onDelete(alarm) })

        binding.rvAlerts.layoutManager = LinearLayoutManager(requireContext())
        val alarmAdapter = binding.rvAlerts.adapter as AlarmAdapter

        lifecycleScope.launchWhenStarted {
            alarmViewModel.queryAll().collect {
                alarmAdapter.submitList(it)
            }
        }

        return binding.root
    }

    private fun onSwitchStatusChanged(alarm: Alarm) {
        lifecycleScope.launchWhenStarted {
            alarmViewModel.update(alarm)
        }
    }

    private fun onDelete(alarm: Alarm) {
        lifecycleScope.launchWhenStarted {
            alarmViewModel.delete(alarm)
        }
    }

    private fun displayNotification() {
        val notificationId = 120
        val tapResultIntent = Intent(requireContext(), MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            tapResultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        val notification = NotificationCompat.Builder(requireContext(), channelId)
            .setContentTitle("Weather Alert")
            .setContentText("There is a weather alert and this is its description")
            .setSmallIcon(R.drawable.cloud)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("There is a weather alert and this is its description\nSecondLine\nThirdLine")
            )
            .build()
        notificationManager?.notify(notificationId, notification)
    }

    private fun createNotificationChannel(id: String, name: String, channelDescription: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance).apply {
                description = channelDescription
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }
}
