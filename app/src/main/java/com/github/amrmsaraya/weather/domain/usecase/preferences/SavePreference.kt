package com.github.amrmsaraya.weather.domain.usecase.preferences

import com.github.amrmsaraya.weather.domain.repository.PreferencesRepo
import javax.inject.Inject

class SavePreference @Inject constructor(private val preferencesRepo: PreferencesRepo) {
    suspend fun execute(key: String, value: Int) = preferencesRepo.savePreference(key, value)
    suspend fun execute(key: String, value: Boolean) = preferencesRepo.savePreference(key, value)
    suspend fun execute(key: String, value: String) = preferencesRepo.savePreference(key, value)
}
