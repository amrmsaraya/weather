package com.github.amrmsaraya.weather.presenter.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.models.WeatherAnimation
import com.github.amrmsaraya.weather.databinding.FragmentSettingsBinding
import com.github.amrmsaraya.weather.presenter.viewModel.SharedViewModel
import com.github.amrmsaraya.weather.utils.SharedViewModelFactory
import com.github.matteobattilana.weather.PrecipType

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var datastore: DataStore<Preferences>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_settings, container, false)
        val sharedFactory = SharedViewModelFactory(requireContext())
        sharedViewModel =
            ViewModelProvider(requireActivity(), sharedFactory).get(SharedViewModel::class.java)
        datastore = requireContext().applicationContext.createDataStore("settings")

        // Reset animation after leaving favorite weather
        if (sharedViewModel.currentFragment.value == "FavoriteWeather") {
            sharedViewModel.setWeatherAnimation(WeatherAnimation(PrecipType.CLEAR, 100f))
        }

        sharedViewModel.setActionBarTitle("Settings")
        sharedViewModel.setCurrentFragment("Settings")
        sharedViewModel.setMapStatus("Current")
        sharedViewModel.setActionBarVisibility(true)

        lifecycleScope.launchWhenStarted {
            if (sharedViewModel.readDataStore("location").isNullOrEmpty() ||
                sharedViewModel.readDataStore("language").isNullOrEmpty() ||
                sharedViewModel.readDataStore("temperature").isNullOrEmpty() ||
                sharedViewModel.readDataStore("windSpeed").isNullOrEmpty()
            ) {
                sharedViewModel.saveDataStore("location", "Map")
                sharedViewModel.saveDataStore("language", "English")
                sharedViewModel.saveDataStore("temperature", "Celsius")
                sharedViewModel.saveDataStore("windSpeed", "Meter / Sec")
                Log.i("myTag", "Default Settings have been created!")
            }
        }

        lifecycleScope.launchWhenStarted {
            when (sharedViewModel.readDataStore("location")) {
                "GPS" -> binding.rgLocation.check(R.id.rbGps)
                "Map" -> binding.rgLocation.check(R.id.rbMap)
            }
            when (sharedViewModel.readDataStore("language")) {
                "English" -> binding.rgLanguage.check(R.id.rbEnglish)
                "Arabic" -> binding.rgLanguage.check(R.id.rbArabic)
            }
            when (sharedViewModel.readDataStore("temperature")) {
                "Celsius" -> binding.rgTemperature.check(R.id.rbCelsius)
                "Kelvin" -> binding.rgTemperature.check(R.id.rbKelvin)
                "Fahrenheit" -> binding.rgTemperature.check(R.id.rbFahrenheit)
            }
            when (sharedViewModel.readDataStore("windSpeed")) {
                "Meter / Sec" -> binding.rgWindSpeed.check(R.id.rbMeter)
                "Mile / Hour" -> binding.rgWindSpeed.check(R.id.rbMile)
            }
        }

        binding.rgLocation.setOnCheckedChangeListener { _, checkedId ->
            val location = binding.root.findViewById<RadioButton>(checkedId)
            lifecycleScope.launchWhenStarted {
                sharedViewModel.saveDataStore("location", location.text.toString())
            }
        }

        binding.rgLanguage.setOnCheckedChangeListener { _, checkedId ->
            val lang = binding.root.findViewById<RadioButton>(checkedId)
            lifecycleScope.launchWhenStarted {
                sharedViewModel.saveDataStore("language", lang.text.toString())
            }
        }

        binding.rgTemperature.setOnCheckedChangeListener { _, checkedId ->
            val temp = binding.root.findViewById<RadioButton>(checkedId)
            lifecycleScope.launchWhenStarted {
                sharedViewModel.saveDataStore("temperature", temp.text.toString())
            }
        }

        binding.rgWindSpeed.setOnCheckedChangeListener { _, checkedId ->
            val wind = binding.root.findViewById<RadioButton>(checkedId)
            lifecycleScope.launchWhenStarted {
                sharedViewModel.saveDataStore("windSpeed", wind.text.toString())
            }
        }

        binding.rbMap.setOnClickListener {
            sharedViewModel.setMapStatus("Current")
            findNavController().navigate(R.id.mapsFragment)
        }

        return binding.root
    }
}
