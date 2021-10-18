package com.github.amrmsaraya.weather.di.usecase

import com.github.amrmsaraya.weather.domain.repository.PreferencesRepo
import com.github.amrmsaraya.weather.domain.usecase.preferences.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Preference {

    @Singleton
    @Provides
    fun provideGetBooleanPreference(preferencesRepo: PreferencesRepo): GetBooleanPreference {
        return GetBooleanPreference(preferencesRepo)
    }

    @Singleton
    @Provides
    fun provideGetIntPreference(preferencesRepo: PreferencesRepo): GetIntPreference {
        return GetIntPreference(preferencesRepo)
    }

    @Singleton
    @Provides
    fun provideGetStringPreference(preferencesRepo: PreferencesRepo): GetStringPreference {
        return GetStringPreference(preferencesRepo)
    }

    @Singleton
    @Provides
    fun provideRestorePreferences(preferencesRepo: PreferencesRepo): RestorePreferences {
        return RestorePreferences(preferencesRepo)
    }

    @Singleton
    @Provides
    fun provideSavePreference(preferencesRepo: PreferencesRepo): SavePreference {
        return SavePreference(preferencesRepo)
    }

    @Singleton
    @Provides
    fun provideSetDefaultPreferences(preferencesRepo: PreferencesRepo): SetDefaultPreferences {
        return SetDefaultPreferences(preferencesRepo)
    }
}