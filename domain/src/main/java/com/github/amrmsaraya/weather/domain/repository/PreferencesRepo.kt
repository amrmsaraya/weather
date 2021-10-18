package com.github.amrmsaraya.weather.domain.repository

import com.github.amrmsaraya.weather.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface PreferencesRepo {
    suspend fun savePreference(key: String, value: Int)
    suspend fun savePreference(key: String, value: Boolean)
    suspend fun savePreference(key: String, value: String)
    suspend fun getIntPreference(key: String): Flow<Int>
    suspend fun getBooleanPreference(key: String): Flow<Boolean>
    suspend fun getStringPreference(key: String): Flow<String>
    suspend fun setDefaultPreferences(settings: Settings)
    suspend fun restorePreferences(): Flow<Settings>
}
