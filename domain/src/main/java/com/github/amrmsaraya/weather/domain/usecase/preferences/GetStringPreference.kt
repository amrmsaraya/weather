package com.github.amrmsaraya.weather.domain.usecase.preferences

import com.github.amrmsaraya.weather.domain.repository.PreferencesRepo
import kotlinx.coroutines.flow.Flow

class GetStringPreference(private val preferencesRepo: PreferencesRepo) {
    suspend fun execute(key: String): Flow<String> {
        return preferencesRepo.getStringPreference(key)
    }
}
