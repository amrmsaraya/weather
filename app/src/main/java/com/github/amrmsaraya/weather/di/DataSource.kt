package com.github.amrmsaraya.weather.di

import com.github.amrmsaraya.weather.data.local.AlertDao
import com.github.amrmsaraya.weather.data.local.WeatherDao
import com.github.amrmsaraya.weather.data.remote.ApiService
import com.github.amrmsaraya.weather.data.source.AlertLocalDataSource
import com.github.amrmsaraya.weather.data.source.LocalDataSource
import com.github.amrmsaraya.weather.data.source.RemoteDataSource
import com.github.amrmsaraya.weather.data.sourceImp.AlertLocalDataSourceImp
import com.github.amrmsaraya.weather.data.sourceImp.LocalDataSourceImp
import com.github.amrmsaraya.weather.data.sourceImp.RemoteDataSourceImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataSource {

    @Provides
    @Singleton
    fun provideLocalDataSource(weatherDao: WeatherDao): LocalDataSource {
        return LocalDataSourceImp(weatherDao)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: ApiService): RemoteDataSource {
        return RemoteDataSourceImp(apiService)
    }

    @Provides
    @Singleton
    fun provideAlertLocalDataSource(alertDao: AlertDao): AlertLocalDataSource {
        return AlertLocalDataSourceImp(alertDao)
    }
}
