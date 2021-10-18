package com.github.amrmsaraya.weather.domain.usecase.preferences

import com.github.amrmsaraya.weather.domain.repository.PreferencesRepo
import kotlinx.coroutines.flow.Flow

class GetBooleanPreference(private val preferencesRepo: PreferencesRepo) {
    suspend fun execute(key: String): Flow<Boolean> {
        return preferencesRepo.getBooleanPreference(key)
    }
}
