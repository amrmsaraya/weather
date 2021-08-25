package com.github.amrmsaraya.weather.domain.repository

import com.github.amrmsaraya.weather.data.models.Settings
import kotlinx.coroutines.flow.Flow

interface PreferencesRepo {
    suspend fun savePreference(key: String, value: Int)
    suspend fun savePreference(key: String, value: Boolean)
    suspend fun savePreference(key: String, value: String)
    suspend fun getIntPreference(key: String): Flow<Int>
    suspend fun getBooleanPreference(key: String): Flow<Boolean>
    suspend fun getStringPreference(key: String): Flow<String>
    suspend fun setDefaultPreferences()
    suspend fun restorePreferences(): Flow<Settings>
}
