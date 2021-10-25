package com.github.amrmsaraya.weather.domain.usecase.forecast

import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.repository.FakeForecastRepo
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi

class DeleteForecastTest {

    private lateinit var fakeForecastRepo: FakeForecastRepo
    private lateinit var deleteForecast: DeleteForecast

    @Before
    fun setup() {
        fakeForecastRepo = FakeForecastRepo()
        deleteForecast = DeleteForecast(fakeForecastRepo)
    }

    @Test
    fun `execute() with forecast then forecast is deleted from forecastList`() =
        runBlockingTest {
            // Given
            val forecast1 = Forecast(id = 2)
            val forecast2 = Forecast(id = 3)
            fakeForecastRepo.insertForecast(forecast1)
            fakeForecastRepo.insertForecast(forecast2)

            // When
            deleteForecast.execute(forecast2)

            // Then
            assertThat(fakeForecastRepo.forecastList).isEqualTo(mutableListOf(Forecast(id = 2)))
        }

    @Test(expected = NoSuchElementException::class)
    fun `execute() with forecast doesn't exist then throw NoSuchElementException`() =
        runBlockingTest {
            // Given
            val forecast = Forecast(id = 2)
            // When
            deleteForecast.execute(forecast)

        }

    @Test
    fun `execute() with forecast List then forecast list is deleted`() =
        runBlockingTest {
            // Given
            val forecastList =
                listOf(Forecast(id = 2), Forecast(id = 3), Forecast(id = 4), Forecast(id = 5))

            forecastList.forEach { fakeForecastRepo.insertForecast(it) }

            val listToDeleted = listOf(Forecast(id = 3), Forecast(id = 5))

            // When
            deleteForecast.execute(listToDeleted)

            // Then
            assertThat(fakeForecastRepo.forecastList).isEqualTo(
                listOf(
                    Forecast(id = 2),
                    Forecast(id = 4)
                )
            )
        }

    @Test(expected = NoSuchElementException::class)
    fun `execute() with forecast List that have items doesn't exist then throw NoSuchElementException`() =
        runBlockingTest {
            // Given
            val forecast = Forecast(id = 2)
            fakeForecastRepo.insertForecast(forecast)
            val listToDeleted = listOf(Forecast(id = 2), Forecast(id = 5))

            // When
            deleteForecast.execute(listToDeleted)

        }
}

