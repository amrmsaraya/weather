package com.github.amrmsaraya.weather.domain.usecase.forecast

import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.repository.FakeForecastRepo
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class InsertForecastTest {

    private lateinit var fakeForecastRepo: FakeForecastRepo
    private lateinit var insertForecast: InsertForecast

    @Before
    fun setup() {
        fakeForecastRepo = FakeForecastRepo()
        insertForecast = InsertForecast(fakeForecastRepo)
    }

    @Test
    fun `execute() with forecast then it should be inserted to forecastList`() =
        runBlockingTest {
            // Given
            val forecast = Forecast(id = 2)

            // When
            insertForecast.execute(forecast)

            // Then
            assertThat(fakeForecastRepo.forecastList).isEqualTo(listOf(forecast))
        }

    @Test
    fun `execute() with same id then it should replace the old value in forecastList`() =
        runBlockingTest {
            // Given
            val forecast = Forecast(id = 2, lat = 30.2, lon = 31.6)
            val newForecast = Forecast(id = 2, lat = 20.3, lon = 21.4)
            insertForecast.execute(forecast)

            // When
            insertForecast.execute(newForecast)

            // Then
            assertThat(fakeForecastRepo.forecastList).isEqualTo(listOf(newForecast))
        }
}