package com.github.amrmsaraya.weather.presenter.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.local.WeatherDatabase
import com.github.amrmsaraya.weather.data.models.Alarm
import com.github.amrmsaraya.weather.data.models.WeatherAnimation
import com.github.amrmsaraya.weather.databinding.DialogAddAlertBinding
import com.github.amrmsaraya.weather.databinding.FragmentAlertsBinding
import com.github.amrmsaraya.weather.presenter.adapters.AlarmAdapter
import com.github.amrmsaraya.weather.presenter.viewModel.AlarmViewModel
import com.github.amrmsaraya.weather.presenter.viewModel.LocationViewModel
import com.github.amrmsaraya.weather.presenter.viewModel.SharedViewModel
import com.github.amrmsaraya.weather.presenter.viewModel.WeatherViewModel
import com.github.amrmsaraya.weather.repositories.AlarmsRepo
import com.github.amrmsaraya.weather.repositories.LocationRepo
import com.github.amrmsaraya.weather.repositories.WeatherRepo
import com.github.amrmsaraya.weather.utils.AlarmViewModelFactory
import com.github.amrmsaraya.weather.utils.LocationViewModelFactory
import com.github.amrmsaraya.weather.utils.SharedViewModelFactory
import com.github.amrmsaraya.weather.utils.WeatherViewModelFactory
import com.github.amrmsaraya.weather.workers.AlarmWorker
import com.github.matteobattilana.weather.PrecipType
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AlertsFragment : Fragment() {
    private lateinit var binding: FragmentAlertsBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var alarmViewModel: AlarmViewModel
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var workManager: WorkManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var to: Long
        var from: Long

        workManager = WorkManager.getInstance(requireContext().applicationContext)

        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_alerts, container, false)

        val alarmDao = WeatherDatabase.getInstance(requireActivity().application).alarmDao()
        val locationDao = WeatherDatabase.getInstance(requireActivity().application).locationDao()
        val weatherDao = WeatherDatabase.getInstance(requireActivity().application).weatherDao()

        val alarmRepo = AlarmsRepo(alarmDao)
        val locationRepo = LocationRepo(locationDao)
        val weatherRepo = WeatherRepo(requireContext(), weatherDao)

        val sharedFactory = SharedViewModelFactory(requireContext())
        val alarmFactory = AlarmViewModelFactory(alarmRepo)
        val locationFactory = LocationViewModelFactory(locationRepo)
        val weatherFactory = WeatherViewModelFactory(weatherRepo)

        sharedViewModel =
            ViewModelProvider(requireActivity(), sharedFactory).get(SharedViewModel::class.java)
        alarmViewModel =
            ViewModelProvider(requireActivity(), alarmFactory).get(AlarmViewModel::class.java)
        locationViewModel =
            ViewModelProvider(requireActivity(), locationFactory).get(LocationViewModel::class.java)
        weatherViewModel =
            ViewModelProvider(requireActivity(), weatherFactory).get(WeatherViewModel::class.java)

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

        sharedViewModel.setActionBarTitle(getString(R.string.alerts))
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
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            to = System.currentTimeMillis()
            from = System.currentTimeMillis()


            val dialogBinding: DialogAddAlertBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.dialog_add_alert, container, false)
            var alarmType = dialogBinding.root.findViewById<RadioButton>(R.id.rbAlarm)

            dialogBinding.tvFromDate.text =
                SimpleDateFormat("dd MMM, y", Locale.getDefault()).format(from)
            dialogBinding.tvFromTime.text =
                SimpleDateFormat("h:mm a", Locale.getDefault()).format(from)
            dialogBinding.tvToDate.text =
                SimpleDateFormat("dd MMM, y", Locale.getDefault()).format(to)
            dialogBinding.tvToTime.text =
                SimpleDateFormat("h:mm a", Locale.getDefault()).format(to)

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
                                        SimpleDateFormat(
                                            "dd MMM, y",
                                            Locale.getDefault()
                                        ).format(to)
                                    dialogBinding.tvToTime.text =
                                        SimpleDateFormat("h:mm a", Locale.getDefault()).format(to)
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
                                        SimpleDateFormat("dd MMM, y", Locale.getDefault()).format(
                                            from
                                        )
                                    dialogBinding.tvFromTime.text =
                                        SimpleDateFormat("h:mm a", Locale.getDefault()).format(from)
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
            dialogBinding.rgAlarmType.setOnCheckedChangeListener { _, checkedId ->
                alarmType = dialogBinding.root.findViewById<RadioButton>(checkedId)
            }
            dialogBinding.btnSaveAlert.setOnClickListener {
                if (alarmType.text == getString(R.string.alarm)) {
                    if (!Settings.canDrawOverlays(context)) {
                        val permissionIntent = Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + requireContext().packageName)
                        )
                        permissionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        requireContext().startActivity(permissionIntent)
                    }
                }
                if (to > System.currentTimeMillis() && from > System.currentTimeMillis()) {
                    lifecycleScope.launchWhenStarted {
                        val alarm = Alarm(UUID.randomUUID(), from, to, alarmType.text.toString())
                        val workId = setOneTimeWorkRequest(alarm)
                        alarm.workId = workId
                        alarmViewModel.insert(alarm)
                    }
                    alertDialog.dismiss()
                } else {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.invalid_time),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.rvAlerts.adapter = AlarmAdapter { alarm: Alarm -> onDelete(alarm) }

        binding.rvAlerts.layoutManager = LinearLayoutManager(requireContext())
        val alarmAdapter = binding.rvAlerts.adapter as AlarmAdapter

        lifecycleScope.launchWhenStarted {
            alarmViewModel.queryAll().collect {
                alarmAdapter.submitList(it)
            }
        }
        return binding.root
    }

    private fun onDelete(alarm: Alarm) {
        lifecycleScope.launchWhenStarted {
            WorkManager.getInstance(requireContext()).cancelWorkById(alarm.workId)
            alarmViewModel.delete(alarm)
        }
    }

    private fun setOneTimeWorkRequest(alarm: Alarm): UUID {
        val data = Data.Builder()
            .putString("id", alarm.id.toString())
            .putString("type", "custom")
            .build()

        // Calculate triggering time
        val currentTime = System.currentTimeMillis()
        var specificTimeToTrigger = alarm.start - 7200000
        if (alarm.start - 7200000 <= currentTime && alarm.start > currentTime) {
            specificTimeToTrigger = alarm.start
        }
        val delayToPass = specificTimeToTrigger - currentTime

        val alarmWorkRequest = OneTimeWorkRequest.Builder(AlarmWorker::class.java)
            .setInputData(data)
            .setInitialDelay(delayToPass, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueue(alarmWorkRequest)

        Log.i("myTag", "Alarm has been Scheduled")
        return alarmWorkRequest.id
    }
}
