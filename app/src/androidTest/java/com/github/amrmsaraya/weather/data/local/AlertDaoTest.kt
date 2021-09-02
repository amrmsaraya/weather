package com.github.amrmsaraya.weather.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.amrmsaraya.weather.data.models.Alerts
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalSerializationApi

@RunWith(AndroidJUnit4::class)
class AlertDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WeatherDatabase
    private lateinit var alertDao: AlertDao

    @Before
    fun initDB() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).build()
        alertDao = database.alertDao()
    }

    @After
    fun closeDB() = database.close()


    @Test
    fun insertAlert() = runBlocking {

        // Given
        val alert = Alerts(5, 1, 1, true, "1234")

        // When
        alertDao.insert(alert)
        val result = alertDao.getAlert("1234")

        // Then
        assertThat(result).isEqualTo(alert)
    }

    @Test
    fun deleteAlert() = runBlocking {

        // Given
        val alert = Alerts(5, 1, 1, true, "1234")
        alertDao.insert(alert)

        // When
        alertDao.delete("1234")
        val result = alertDao.getAlert("1234")

        // Then
        assertThat(result).isNotEqualTo(alert)
    }

    @Test
    fun getAlert() = runBlocking {

        // Given
        val alert = Alerts(5, 1, 1, true, "1234")
        alertDao.insert(alert)

        // When
        val result = alertDao.getAlert("1234")

        // Then
        assertThat(result).isEqualTo(alert)
    }

    @Test
    fun getAlerts() = runBlocking {
        database.clearAllTables()

        // Given
        val alert1 = Alerts(3, 1, 1, true, "1234")
        val alert2 = Alerts(4, 3, 3, true, "1234")
        alertDao.insert(alert1)
        alertDao.insert(alert2)

        // When
        val result = alertDao.getAlerts().first()

        // Then
        assertThat(result).isEqualTo(listOf(alert1, alert2))
        assertThat(alert1).isIn(result)
    }

}
