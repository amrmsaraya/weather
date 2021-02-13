package com.github.amrmsaraya.weather.presenter.viewModel

import androidx.lifecycle.ViewModel
import com.github.amrmsaraya.weather.data.models.WeatherAnimation
import com.github.matteobattilana.weather.PrecipType
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedViewModel : ViewModel() {
    private val _actionBarTitle = MutableStateFlow("Weather Forecast")
    private val _actionBarVisibility = MutableStateFlow("Show")
    private val _currentFragment = MutableStateFlow("Home")
    private val _mapStatus = MutableStateFlow("Default")
    private val _weatherAnimation = MutableStateFlow(
        WeatherAnimation(
            PrecipType.CLEAR,
            100f
        )
    )
    private val _latLng = MutableStateFlow(LatLng(0.0, 0.0))


    val actionBarTitle: StateFlow<String> = _actionBarTitle
    val actionBarVisibility: StateFlow<String> = _actionBarVisibility
    val currentFragment: StateFlow<String> = _currentFragment
    val mapStatus: StateFlow<String> = _mapStatus
    val weatherAnimation: StateFlow<WeatherAnimation> = _weatherAnimation
    val latLng: StateFlow<LatLng> = _latLng

    fun setActionBarTitle(title: String) {
        _actionBarTitle.value = title
    }

    fun setActionBarVisibility(status: String) {
        _actionBarVisibility.value = status
    }

    fun setCurrentFragment(name: String) {
        _currentFragment.value = name
    }

    fun setMapStatus(status: String) {
        _mapStatus.value = status
    }

    fun setWeatherAnimation(weatherAnimation: WeatherAnimation) {
        _weatherAnimation.value = weatherAnimation
    }

    fun setLatLng(latLng: LatLng) {
        _latLng.value = latLng
    }

}
