package com.github.amrmsaraya.weather.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.github.amrmsaraya.weather.data.repositoryImp.AlertRepoImp
import com.github.amrmsaraya.weather.data.repositoryImp.ForecastRepoImp
import com.github.amrmsaraya.weather.data.repositoryImp.PreferencesRepoImp
import com.github.amrmsaraya.weather.data.source.AlertDataSource
import com.github.amrmsaraya.weather.data.source.LocalDataSource
import com.github.amrmsaraya.weather.data.source.RemoteDataSource
import com.github.amrmsaraya.weather.domain.repository.AlertRepo
import com.github.amrmsaraya.weather.domain.repository.ForecastRepo
import com.github.amrmsaraya.weather.domain.repository.PreferencesRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class Repository {

    @Provides
    @Singleton
    fun providePreferencesRepo(dataStore: DataStore<Preferences>): PreferencesRepo {
        return PreferencesRepoImp(dataStore)
    }

    @Provides
    @Singleton
    fun provideForecastRepo(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ): ForecastRepo {
        return ForecastRepoImp(localDataSource, remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideAlertRepo(
        alertDataSource: AlertDataSource,
    ): AlertRepo {
        return AlertRepoImp(alertDataSource)
    }
}
