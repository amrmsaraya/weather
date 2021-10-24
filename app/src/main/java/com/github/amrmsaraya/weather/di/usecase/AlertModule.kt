package com.github.amrmsaraya.weather.di.usecase

import com.github.amrmsaraya.weather.domain.repository.AlertRepo
import com.github.amrmsaraya.weather.domain.usecase.alert.DeleteAlert
import com.github.amrmsaraya.weather.domain.usecase.alert.GetAlert
import com.github.amrmsaraya.weather.domain.usecase.alert.GetAlerts
import com.github.amrmsaraya.weather.domain.usecase.alert.InsertAlert
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AlertModule {

    @Singleton
    @Provides
    fun provideDeleteAlert(alertRepo: AlertRepo): DeleteAlert {
        return DeleteAlert(alertRepo)
    }

    @Singleton
    @Provides
    fun provideDGetAlert(alertRepo: AlertRepo): GetAlert {
        return GetAlert(alertRepo)
    }

    @Singleton
    @Provides
    fun provideGetAlerts(alertRepo: AlertRepo): GetAlerts {
        return GetAlerts(alertRepo)
    }

    @Singleton
    @Provides
    fun provideInsertAlert(alertRepo: AlertRepo): InsertAlert {
        return InsertAlert(alertRepo)
    }
}