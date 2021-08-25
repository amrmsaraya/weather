package com.github.amrmsaraya.weather.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.amrmsaraya.weather.data.models.forecast.Forecast
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
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
    fun insertForecast_returnForecast() = runBlockingTest {
        // Given
        val forecast = Forecast(7, 2.22, 3.33)
        weatherDao.insertForecast(forecast)

        // When
        val result = weatherDao.getForecast(7).first()

        // Then
        assertThat(result).isEqualTo(forecast)

    }

    @Test
    fun getCurrentForecast_returnCurrentForecast() = runBlockingTest {
        // Given
        val forecast = Forecast(1, 2.22, 3.33)
        val forecast2 = Forecast(7, 2.22, 3.33)

        weatherDao.insertForecast(forecast)
        weatherDao.insertForecast(forecast2)

        // When
        val result = weatherDao.getCurrentForecast().first()

        // Then
        assertThat(result).isEqualTo(forecast)

    }

    @Test
    fun getAllForecast_insertThreeForecasts_returnExceptCurrent() = runBlockingTest {
        // Given
        val forecast1 = Forecast(1, 1.11, 1.11)
        val forecast2 = Forecast(4, 2.22, 2.22)
        val forecast3 = Forecast(5, 3.33, 3.33)

        weatherDao.insertForecast(forecast1)
        weatherDao.insertForecast(forecast2)
        weatherDao.insertForecast(forecast3)

        // When
        val resultList = weatherDao.getFavoriteForecasts().first()

        // Then
        assertThat(resultList).isEqualTo(listOf(forecast2, forecast3))
        assertThat(resultList).doesNotContain(forecast1)

    }

    @Test
    fun deleteForecast_returnNull() = runBlockingTest {
        // Given
        val forecast = Forecast(3, 2.22, 2.22)

        weatherDao.insertForecast(forecast)

        // When
        weatherDao.deleteForecast(forecast)
        val result = weatherDao.getForecast(3).first()

        // Then
        assertThat(result).isEqualTo(null)
    }


}
