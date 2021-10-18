package com.github.amrmsaraya.weather.data.sourceImp

import com.github.amrmsaraya.weather.data.local.WeatherDao
import com.github.amrmsaraya.weather.data.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class LocalDataSourceImpTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var weatherDao: WeatherDao

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun insertForecast() = runBlocking {
        // Given
        val forecast = Forecast(id = 35, lat = 5.5)

        // When
        weatherDao.insertForecast(forecast)
        val result = weatherDao.getForecast(35).first()

        // Then
        Truth.assertThat(result).isEqualTo(forecast)
    }

    @Test
    fun deleteForecast() = runBlocking {
        // Given
        val forecast1 = Forecast(id = 35, lat = 5.5)
        val forecast2 = Forecast(id = 20, lat = 6.6)

        weatherDao.insertForecast(forecast1)
        weatherDao.insertForecast(forecast2)

        // When
        weatherDao.deleteForecast(forecast1)
        val result = weatherDao.getForecast(35).first()

        // Then
        Truth.assertThat(result).isNotEqualTo(forecast1)
    }

    @Test
    fun testDeleteForecast() = runBlocking {
        // Given
        val forecast1 = Forecast(id = 35, lat = 5.5)
        val forecast2 = Forecast(id = 20, lat = 6.6)

        weatherDao.insertForecast(forecast1)
        weatherDao.insertForecast(forecast2)

        // When
        weatherDao.deleteForecast(listOf(forecast1, forecast2))
        val result1 = weatherDao.getForecast(35).first()
        val result2 = weatherDao.getForecast(20).first()

        // Then
        Truth.assertThat(result1).isNotEqualTo(forecast1)
        Truth.assertThat(result2).isNotEqualTo(forecast2)
    }

    @Test
    fun getForecast() = runBlocking {
        // Given
        val forecast = Forecast(id = 35, lat = 5.5)
        weatherDao.insertForecast(forecast)

        // When
        val result = weatherDao.getForecast(35).first()

        // Then
        Truth.assertThat(result).isEqualTo(forecast)
    }

    @Test
    fun getCurrentForecast() = runBlocking {
        // Given
        val forecast = Forecast(id = 1, lat = 5.5)
        weatherDao.insertForecast(forecast)

        // When
        val result = weatherDao.getCurrentForecast().first()

        // Then
        Truth.assertThat(result).isEqualTo(forecast)
    }

    @Test
    fun getFavoriteForecasts() = runBlocking {
        // Given
        val forecast = Forecast(id = 1, lat = 5.5)
        val forecast1 = Forecast(id = 2, lat = 5.5)

        weatherDao.insertForecast(forecast)
        weatherDao.insertForecast(forecast1)

        // When
        val result = weatherDao.getFavoriteForecasts().first()

        // Then
        Truth.assertThat(forecast).isNotIn(result)
        Truth.assertThat(forecast1).isIn(result)
    }
}
