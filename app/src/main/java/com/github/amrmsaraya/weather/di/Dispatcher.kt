package com.github.amrmsaraya.weather.di

import com.github.amrmsaraya.weather.util.dispatchers.IDispatchers
import com.github.amrmsaraya.weather.util.dispatchers.IDispatchersImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Dispatcher {

    @Singleton
    @Provides
    fun provideCoroutineDispatcher(): IDispatchers {
        return IDispatchersImpl()
    }

}