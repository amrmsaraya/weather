package com.github.amrmsaraya.weather.domain.usecase.preferences

import com.github.amrmsaraya.weather.domain.model.Settings
import com.github.amrmsaraya.weather.domain.repository.PreferencesRepo
import kotlinx.coroutines.flow.Flow

class RestorePreferences(private val preferencesRepo: PreferencesRepo) {
    suspend fun execute(): Flow<Settings> {
        return preferencesRepo.restorePreferences()
    }
}
