package com.github.amrmsaraya.weather.presenter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.databinding.FragmentSettingsBinding
import com.github.amrmsaraya.weather.presenter.viewModel.SharedViewModel
import kotlinx.coroutines.flow.first

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
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        datastore = requireContext().applicationContext.createDataStore("settings")
        sharedViewModel.actionBarTitle.value = "Settings"
        sharedViewModel.currentFragment.value = "Settings"

        lifecycleScope.launchWhenCreated {
            when (read("location")) {
                "GPS" -> binding.rgLocation.check(R.id.rbGps)
                "Map" -> binding.rgLocation.check(R.id.rbFromMap)
            }
            when (read("language")) {
                "English" -> binding.rgLanguage.check(R.id.rbEnglish)
                "Arabic" -> binding.rgLanguage.check(R.id.rbArabic)
            }
            when (read("temperature")) {
                "Celsius" -> binding.rgTemperature.check(R.id.rbCelsius)
                "Kelvin" -> binding.rgTemperature.check(R.id.rbKelvin)
                "Fahrenheit" -> binding.rgTemperature.check(R.id.rbFahrenheit)
            }
            when (read("windSpeed")) {
                "Meter / Sec" -> binding.rgWindSpeed.check(R.id.rbMeter)
                "Mile / Hour" -> binding.rgWindSpeed.check(R.id.rbMile)
            }
        }

        binding.rgLocation.setOnCheckedChangeListener { _, checkedId ->
            val location = binding.root.findViewById<RadioButton>(checkedId)
            lifecycleScope.launchWhenCreated {
                save("location", location.text.toString())
            }
        }

        binding.rgLanguage.setOnCheckedChangeListener { _, checkedId ->
            val lang = binding.root.findViewById<RadioButton>(checkedId)
            lifecycleScope.launchWhenCreated {
                save("language", lang.text.toString())
            }
        }

        binding.rgTemperature.setOnCheckedChangeListener { _, checkedId ->
            val temp = binding.root.findViewById<RadioButton>(checkedId)
            lifecycleScope.launchWhenCreated {
                save("temperature", temp.text.toString())
            }
        }

        binding.rgWindSpeed.setOnCheckedChangeListener { _, checkedId ->
            val wind = binding.root.findViewById<RadioButton>(checkedId)
            lifecycleScope.launchWhenCreated {
                save("windSpeed", wind.text.toString())
            }
        }

        return binding.root
    }

    private suspend fun save(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        datastore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun read(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = datastore.data.first()
        return preferences[dataStoreKey]
    }
}
