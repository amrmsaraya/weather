package com.github.amrmsaraya.weather.domain.usecase.forecast

import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.repository.FakeForecastRepo
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetFavoriteForecastsTest {
    private lateinit var fakeForecastRepo: FakeForecastRepo
    private lateinit var getFavoriteForecasts: GetFavoriteForecasts

    @Before
    fun setup() {
        fakeForecastRepo = FakeForecastRepo()
        getFavoriteForecasts = GetFavoriteForecasts(fakeForecastRepo)
    }

    @Test
    fun `getFavoriteForecasts() then return flow contain list of favorite forecasts`() =
        runBlockingTest {
            // Given
            val forecasts = listOf(
                Forecast(id = 1),
                Forecast(id = 2),
                Forecast(id = 3),
                Forecast(id = 4),
            )
            forecasts.forEach { fakeForecastRepo.insertForecast(it) }

            // When
            val favorites = getFavoriteForecasts.execute().first()

            // Then
            assertThat(favorites).isEqualTo(forecasts.filter { it.id != 1L })
        }
}