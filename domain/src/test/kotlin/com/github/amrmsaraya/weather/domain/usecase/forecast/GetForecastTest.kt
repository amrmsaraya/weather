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
class GetForecastTest {

    private lateinit var fakeForecastRepo: FakeForecastRepo
    private lateinit var getForecast: GetForecast

    @Before
    fun setup() {
        fakeForecastRepo = FakeForecastRepo()
        getForecast = GetForecast(fakeForecastRepo)
    }

    @Test
    fun `execute() with id then get forecast from remote and save it to forecastList then return Success`() =
        runBlockingTest {
            // Given
            val forecast =
                Forecast(id = 3, lat = 30.54, lon = 45.59, current = Current(temp = 10.0))
            fakeForecastRepo.insertForecast(forecast)

            // When
            val result = getForecast.execute(3)

            // Then
            assertThat(result).isEqualTo(
                Response.Success(forecast.copy(current = Current(temp = 28.6)))
            )
        }

    @Test(expected = NoSuchElementException::class)
    fun `execute() with id that doesn't exist then get forecast from remote and save it to forecastList then return Error`() =
        runBlockingTest {
            // When
            val result = getForecast.execute(3)

            // Then
            if (result is Response.Error) {
                throw  result.throwable
            }
        }
}