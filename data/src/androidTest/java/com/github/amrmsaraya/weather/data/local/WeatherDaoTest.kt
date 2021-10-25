package com.github.amrmsaraya.weather.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.amrmsaraya.weather.data.model.forecast.ForecastDTO
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalSerializationApi
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WeatherDatabase
    private lateinit var weatherDao: WeatherDao

    @Before
    fun initDB() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).build()
        weatherDao = database.weatherDao()
    }

    @After
    fun closeDB() = database.close()

    @Test
    fun insertForecast() = runBlocking {
        // Given
        val forecast = ForecastDTO(id = 35, lat = 5.5)

        // When
        weatherDao.insertForecast(forecast)
        val result = weatherDao.getForecast(35)

        // Then
        assertThat(result).isEqualTo(forecast)
    }

    @Test
    fun deleteForecast() = runBlocking {
        // Given
        val forecast1 = ForecastDTO(id = 35, lat = 5.5)
        val forecast2 = ForecastDTO(id = 20, lat = 6.6)

        weatherDao.insertForecast(forecast1)
        weatherDao.insertForecast(forecast2)

        // When
        weatherDao.deleteForecast(forecast1)
        val result = weatherDao.getForecast(35)

        // Then
        assertThat(result).isNotEqualTo(forecast1)
    }

    @Test
    fun testDeleteForecast() = runBlocking {
        // Given
        val forecast1 = ForecastDTO(id = 35, lat = 5.5)
        val forecast2 = ForecastDTO(id = 20, lat = 6.6)

        weatherDao.insertForecast(forecast1)
        weatherDao.insertForecast(forecast2)

        // When
        weatherDao.deleteForecast(listOf(forecast1, forecast2))
        val result1 = weatherDao.getForecast(35)
        val result2 = weatherDao.getForecast(20)

        // Then
        assertThat(result1).isNotEqualTo(forecast1)
        assertThat(result2).isNotEqualTo(forecast2)
    }

    @Test
    fun getForecast() = runBlocking {
        // Given
        val forecast = ForecastDTO(id = 35, lat = 5.5)
        weatherDao.insertForecast(forecast)

        // When
        val result = weatherDao.getForecast(35)

        // Then
        assertThat(result).isEqualTo(forecast)
    }

    @Test
    fun getCurrentForecast() = runBlocking {
        // Given
        val forecast = ForecastDTO(id = 1, lat = 5.5)
        weatherDao.insertForecast(forecast)

        // When
        val result = weatherDao.getCurrentForecast()

        // Then
        assertThat(result).isEqualTo(forecast)
    }

    @Test
    fun getFavoriteForecasts() = runBlocking {
        // Given
        val forecast = ForecastDTO(id = 1, lat = 5.5)
        val forecast1 = ForecastDTO(id = 2, lat = 5.5)

        weatherDao.insertForecast(forecast)
        weatherDao.insertForecast(forecast1)

        // When
        val result = weatherDao.getFavoriteForecasts().first()

        // Then
        assertThat(forecast).isNotIn(result)
        assertThat(forecast1).isIn(result)
    }


}
