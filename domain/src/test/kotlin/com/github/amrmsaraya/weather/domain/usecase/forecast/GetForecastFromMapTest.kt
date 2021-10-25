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
class GetForecastFromMapTest {
    private lateinit var fakeForecastRepo: FakeForecastRepo
    private lateinit var getForecastFromMap: GetForecastFromMap

    @Before
    fun setup() {
        fakeForecastRepo = FakeForecastRepo()
        getForecastFromMap = GetForecastFromMap(fakeForecastRepo)
    }

    @Test
    fun `execute() with lat & lon then insert the new forecast to forecastList`() =
        runBlockingTest {
            // When
            getForecastFromMap.execute(20.0, 30.0)

            // Then
            Truth.assertThat(Forecast(lat = 20.0, lon = 30.0, current = Current(temp = 28.6))).isIn(
                fakeForecastRepo.forecastList
            )
        }
}