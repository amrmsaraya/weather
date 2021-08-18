package com.github.amrmsaraya.weather.domain.usecase.preferences

import com.github.amrmsaraya.weather.domain.repository.PreferencesRepo
import javax.inject.Inject

class GetBooleanPreference @Inject constructor(private val preferencesRepo: PreferencesRepo) {
    suspend fun execute(key: String) = preferencesRepo.getBooleanPreference(key)

}
