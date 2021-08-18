package com.github.amrmsaraya.weather.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.github.amrmsaraya.weather.data.repositoryImp.PreferencesRepoImp
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
}
