package com.github.amrmsaraya.weather.domain.usecase.preferences

import com.github.amrmsaraya.weather.domain.model.Settings
import com.github.amrmsaraya.weather.domain.repository.PreferencesRepo

class SetDefaultPreferences(private val preferencesRepo: PreferencesRepo) {
    suspend fun execute(settings:Settings) {
        preferencesRepo.setDefaultPreferences(settings)
    }

}
