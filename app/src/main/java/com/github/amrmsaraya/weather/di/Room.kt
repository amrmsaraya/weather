package com.github.amrmsaraya.weather.di

import android.content.Context
import com.github.amrmsaraya.weather.data.local.WeatherDao
import com.github.amrmsaraya.weather.data.local.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class Room {

    @Singleton
    @Provides
    fun provideWeatherDao(@ApplicationContext context: Context): WeatherDao {
        return WeatherDatabase.getDatabase(context).weatherDao()
    }
}
