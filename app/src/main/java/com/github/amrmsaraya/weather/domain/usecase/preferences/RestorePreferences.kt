package com.github.amrmsaraya.weather.domain.usecase.preferences

import com.github.amrmsaraya.weather.data.models.Settings
import com.github.amrmsaraya.weather.domain.repository.PreferencesRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RestorePreferences @Inject constructor(private val preferencesRepo: PreferencesRepo) {
    suspend fun execute(): Flow<Settings> = preferencesRepo.restorePreferences()
}
