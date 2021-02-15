package com.github.amrmsaraya.weather.presenter.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.github.amrmsaraya.weather.data.models.Location
import com.github.amrmsaraya.weather.data.models.WeatherAnimation
import com.github.amrmsaraya.weather.repositories.DataStoreRepo
import com.github.matteobattilana.weather.PrecipType
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedViewModel(private val context: Context) : ViewModel() {

    private val dataStoreRepo = DataStoreRepo(context)
    private val _actionBarTitle = MutableStateFlow("Weather Forecast")
    private val _actionBarVisibility = MutableStateFlow(true)
    private val _currentFragment = MutableStateFlow("Home")
    private val _mapStatus = MutableStateFlow("Current")
    private val _weatherAnimation = MutableStateFlow(
        WeatherAnimation(
            PrecipType.CLEAR,
            100f
        )
    )
    private val _latLng = MutableStateFlow(LatLng(0.0, 0.0))
    private val _currentLatLng = MutableStateFlow(LatLng(0.0, 0.0))
    private val _isPermissionGranted = MutableStateFlow(true)
    private val _langUnit = MutableStateFlow("English")
    private val _tempUnit = MutableStateFlow("Celsius")
    private val _windUnit = MutableStateFlow("Meter / Sec")
    private val _clickedFavoriteLocation = MutableStateFlow(Location(0.0, 0.0, ""))


    val actionBarTitle: StateFlow<String> = _actionBarTitle
    val actionBarVisibility: StateFlow<Boolean> = _actionBarVisibility
    val currentFragment: StateFlow<String> = _currentFragment
    val mapStatus: StateFlow<String> = _mapStatus
    val weatherAnimation: StateFlow<WeatherAnimation> = _weatherAnimation
    val latLng: StateFlow<LatLng> = _latLng
    val currentLatLng: StateFlow<LatLng> = _currentLatLng
    val isPermissionGranted: StateFlow<Boolean> = _isPermissionGranted
    val langUnit = _langUnit
    val tempUnit = _tempUnit
    val windUnit = _windUnit
    val clickedFavoriteLocation = _clickedFavoriteLocation


    fun setActionBarTitle(title: String) {
        _actionBarTitle.value = title
    }

    fun setActionBarVisibility(isVisible: Boolean) {
        _actionBarVisibility.value = isVisible
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

    fun setCurrentLatLng(latLng: LatLng) {
        _currentLatLng.value = latLng
    }

    fun setPermissionGranted(status: Boolean) {
        _isPermissionGranted.value = status
    }

    fun setClickedFavoriteLocation(location: Location) {
        _clickedFavoriteLocation.value = location
    }

    suspend fun readDataStore(key: String): String? {
        val value = dataStoreRepo.readDataStore(key)
        if (value != null) {
            when (key) {
                "language" -> when (value) {
                    "English" -> _langUnit.value = "en"
                    "Arabic" -> _langUnit.value = "ar"
                }
                "temperature" -> _tempUnit.value = value
                "windSpeed" -> _windUnit.value = value
            }
        }
        return dataStoreRepo.readDataStore(key)
    }

    suspend fun saveDataStore(key: String, value: String) {
        dataStoreRepo.saveDataStore(key, value)
        when (key) {
            "language" -> when (value) {
                "English" -> _langUnit.value = "en"
                "Arabic" -> _langUnit.value = "ar"
            }
            "temperature" -> _tempUnit.value = value
            "windSpeed" -> _windUnit.value = value
        }
    }

}
