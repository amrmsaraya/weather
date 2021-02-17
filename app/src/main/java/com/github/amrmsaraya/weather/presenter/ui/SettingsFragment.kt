package com.github.amrmsaraya.weather.presenter.ui

import android.os.Bundle
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

        sharedViewModel.setActionBarTitle(getString(R.string.settings))
        sharedViewModel.setCurrentFragment("Settings")
        sharedViewModel.setMapStatus("Current")
        sharedViewModel.setActionBarVisibility(true)

        lifecycleScope.launchWhenStarted {
            sharedViewModel.setDefaultSettings(getString(R.string.gps))
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
            var text = location.text.toString()
            when (location.text.toString()) {
                "موقعك الحالي" -> text = "GPS"
                "الخريطة" -> text = "Map"
            }
            lifecycleScope.launchWhenStarted {
                sharedViewModel.saveDataStore("location", text)
            }
        }

        binding.rgLanguage.setOnCheckedChangeListener { _, checkedId ->
            val lang = binding.root.findViewById<RadioButton>(checkedId)
            var text = lang.text.toString()
            when (lang.text.toString()) {
                "الإنجليزية" -> text = "English"
                "العربية" -> text = "Arabic"
            }
            lifecycleScope.launchWhenStarted {
                sharedViewModel.saveDataStore("language", text)
            }
        }

        binding.rgTemperature.setOnCheckedChangeListener { _, checkedId ->
            val temp = binding.root.findViewById<RadioButton>(checkedId)
            var text = temp.text.toString()
            when (temp.text.toString()) {
                "سيليزيس" -> text = "Celsius"
                "كيلفن" -> text = "Kelvin"
                "فيهرنهايت" -> text = "Fahrenheit"
            }
            lifecycleScope.launchWhenStarted {
                sharedViewModel.saveDataStore("temperature", text)
            }
        }

        binding.rgWindSpeed.setOnCheckedChangeListener { _, checkedId ->
            val wind = binding.root.findViewById<RadioButton>(checkedId)
            var text = wind.text.toString()
            when (wind.text.toString()) {
                "متر / ثانية" -> text = "Meter / Sec"
                "ميل / ساعة" -> text = "Mile / Hour"
            }
            lifecycleScope.launchWhenStarted {
                sharedViewModel.saveDataStore("windSpeed", text)
            }
        }

        binding.rbMap.setOnClickListener {
            sharedViewModel.setMapStatus("Current")
            findNavController().navigate(R.id.mapsFragment)
        }

        return binding.root
    }
}
