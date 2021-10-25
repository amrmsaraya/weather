package com.github.amrmsaraya.weather.domain.usecase.forecast

import com.github.amrmsaraya.weather.domain.model.forecast.Current
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.repository.FakeForecastRepo
import com.github.amrmsaraya.weather.domain.util.Response
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetCurrentForecastTest {
    private lateinit var fakeForecastRepo: FakeForecastRepo
    private lateinit var getCurrentForecast: GetCurrentForecast

    @Before
    fun setup() {
        fakeForecastRepo = FakeForecastRepo()
        getCurrentForecast = GetCurrentForecast(fakeForecastRepo)
    }

    @Test
    fun `execute() with no params return the current forecast with response success`() =
        runBlockingTest {
            // Given
            val forecast = Forecast(id = 1, current = Current(temp = 10.0))
            fakeForecastRepo.insertForecast(forecast)

            // When
            val result = getCurrentForecast.execute()

            // Then
            assertThat(result).isEqualTo(
                Response.Success(forecast.copy(current = Current(temp = 28.6)))
            )
        }

    @Test(expected = NoSuchElementException::class)
    fun `execute() with no params when current forecast isn't exist then throw NoSuchElementException`() =
        runBlockingTest {
            // When
            val result = getCurrentForecast.execute()
            if (result is Response.Error) {
                throw result.throwable
            }
        }


    @Test
    fun `execute() with lat & lon return the current forecast with response success`() =
        runBlockingTest {
            // Given
            val forecast = Forecast(id = 1, lat = 31.0, lon = 41.0, current = Current(temp = 10.0))
            fakeForecastRepo.insertForecast(forecast)

            // When
            val result = getCurrentForecast.execute(31.0, 41.0)

            // Then
            assertThat(result).isEqualTo(
                Response.Success(forecast.copy(current = Current(temp = 28.6)))
            )
        }


    @Test
    fun `execute() with lat & lon when current forecast isn't exist return the current forecast with response success`() =
        runBlockingTest {
            // When
            val result = getCurrentForecast.execute(31.0, 41.0)

            // Then
            assertThat(result).isEqualTo(
                Response.Success(
                    Forecast(
                        id = 1,
                        lat = 31.0,
                        lon = 41.0,
                        current = Current(temp = 28.6)
                    )
                )
            )
        }
}