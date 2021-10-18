package com.github.amrmsaraya.weather.di.usecase

import com.github.amrmsaraya.weather.domain.repository.ForecastRepo
import com.github.amrmsaraya.weather.domain.usecase.forecast.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Forecast {

    @Singleton
    @Provides
    fun provideDeleteForecast(forecastRepo: ForecastRepo): DeleteForecast {
        return DeleteForecast(forecastRepo)
    }

    @Singleton
    @Provides
    fun provideGetCurrentForecast(forecastRepo: ForecastRepo): GetCurrentForecast {
        return GetCurrentForecast(forecastRepo)
    }

    @Singleton
    @Provides
    fun provideGetFavoriteForecasts(forecastRepo: ForecastRepo): GetFavoriteForecasts {
        return GetFavoriteForecasts(forecastRepo)
    }

    @Singleton
    @Provides
    fun provideGetForecast(forecastRepo: ForecastRepo): GetForecast {
        return GetForecast(forecastRepo)
    }

    @Singleton
    @Provides
    fun provideInsertForecast(forecastRepo: ForecastRepo): InsertForecast {
        return InsertForecast(forecastRepo)
    }

    @Singleton
    @Provides
    fun provideGetForecastFromMap(forecastRepo: ForecastRepo): GetForecastFromMap {
        return GetForecastFromMap(forecastRepo)
    }
}