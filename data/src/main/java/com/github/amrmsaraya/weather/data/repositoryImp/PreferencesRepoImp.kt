package com.github.amrmsaraya.weather.data.repositoryImp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.github.amrmsaraya.weather.domain.model.Settings
import com.github.amrmsaraya.weather.domain.repository.PreferencesRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesRepoImp(private val dataStore: DataStore<Preferences>) : PreferencesRepo {

    override suspend fun savePreference(key: String, value: Int) {
        val preferencesKey = intPreferencesKey(key)
        dataStore.edit { settings ->
            settings[preferencesKey] = value
        }
    }

    override suspend fun savePreference(key: String, value: Boolean) {
        val preferencesKey = booleanPreferencesKey(key)
        dataStore.edit { settings ->
            settings[preferencesKey] = value
        }
    }

    override suspend fun savePreference(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        dataStore.edit { settings ->
            settings[preferencesKey] = value
        }
    }

    override suspend fun getIntPreference(key: String): Flow<Int> {
        val preferencesKey = intPreferencesKey(key)
        return dataStore.data.map { preferences ->
            preferences[preferencesKey] ?: 0
        }
    }

    override suspend fun getBooleanPreference(key: String): Flow<Boolean> {
        val preferencesKey = booleanPreferencesKey(key)
        return dataStore.data.map { preferences ->
            preferences[preferencesKey] ?: true
        }
    }

    override suspend fun getStringPreference(key: String): Flow<String> {
        val preferencesKey = stringPreferencesKey(key)
        return dataStore.data.map { preferences ->
            preferences[preferencesKey] ?: ""
        }
    }

    override suspend fun setDefaultPreferences(settings: Settings) {
        savePreference("location", settings.location)
        savePreference("language", settings.language)
        savePreference("theme", settings.theme)
        savePreference("accent", settings.accent)
        savePreference("notifications", settings.notifications)
        savePreference("temperature", settings.temperature)
        savePreference("windSpeed", settings.windSpeed)
        savePreference("versionCode", settings.versionCode)
    }

    override suspend fun restorePreferences(): Flow<Settings> {
        return dataStore.data.map { preferences ->
            Settings(
                location = preferences[intPreferencesKey("location")] ?: 0,
                language = preferences[intPreferencesKey("language")] ?: 0,
                theme = preferences[intPreferencesKey("theme")] ?: 0,
                accent = preferences[intPreferencesKey("accent")] ?: 0,
                notifications = preferences[booleanPreferencesKey("notifications")] ?: true,
                temperature = preferences[intPreferencesKey("temperature")] ?: 0,
                windSpeed = preferences[intPreferencesKey("windSpeed")] ?: 0,
                versionCode = preferences[intPreferencesKey("versionCode")] ?: 0,
            )
        }
    }
}
