package com.github.amrmsaraya.weather.presenter.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.amrmsaraya.weather.data.models.WeatherAnimation
import com.github.matteobattilana.weather.PrecipType

class SharedViewModel : ViewModel() {
    var actionBarTitle = MutableLiveData<String>()
    var weatherAnimation = MutableLiveData<WeatherAnimation>()
    var actionBarVisibility = MutableLiveData<String>()
    var currentFragment = MutableLiveData<String>()
    var mapStatus = MutableLiveData<String>()

    init {
        actionBarTitle.value = "Weather Forecast"
        actionBarVisibility.value = "Show"
        weatherAnimation.value = WeatherAnimation(
            PrecipType.CLEAR,
            100f
        )
        currentFragment.value = "Home"
        mapStatus.value = "Default"
    }
}
