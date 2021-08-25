package com.github.amrmsaraya.weather.domain.usecase.preferences

import com.github.amrmsaraya.weather.domain.repository.PreferencesRepo
import javax.inject.Inject

class SetDefaultPreferences @Inject constructor(private val preferencesRepo: PreferencesRepo) {
    suspend fun execute() = preferencesRepo.setDefaultPreferences()

}
