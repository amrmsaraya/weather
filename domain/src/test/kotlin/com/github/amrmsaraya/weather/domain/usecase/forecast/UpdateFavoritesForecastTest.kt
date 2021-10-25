package com.github.amrmsaraya.weather.domain.usecase.forecast

import com.github.amrmsaraya.weather.domain.model.forecast.Current
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.repository.FakeForecastRepo
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UpdateFavoritesForecastTest {
    private lateinit var fakeForecastRepo: FakeForecastRepo
    private lateinit var updateFavoritesForecast: UpdateFavoritesForecast

    @Before
    fun setup() {
        fakeForecastRepo = FakeForecastRepo()
        updateFavoritesForecast = UpdateFavoritesForecast(fakeForecastRepo)
    }

    @Test
    fun `execute() then the favorite forecasts will be updated`() = runBlockingTest {
        // Given
        val forecasts = listOf(
            Forecast(id = 2, current = Current(temp = 10.0)),
            Forecast(id = 3, current = Current(temp = 10.0)),
            Forecast(id = 4, current = Current(temp = 10.0)),
            Forecast(id = 5, current = Current(temp = 10.0)),
        )
        forecasts.forEach {
            fakeForecastRepo.insertForecast(it)
        }

        // When
        updateFavoritesForecast.execute()

        // Then
        Truth.assertThat(fakeForecastRepo.forecastList).isEqualTo(
            forecasts.map { it.copy(current = Current(temp = 28.6)) }
        )
    }
}