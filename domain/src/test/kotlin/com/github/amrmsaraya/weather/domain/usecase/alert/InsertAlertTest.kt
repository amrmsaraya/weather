package com.github.amrmsaraya.weather.domain.usecase.alert

import com.github.amrmsaraya.weather.domain.model.Alerts
import com.github.amrmsaraya.weather.domain.repository.FakeAlertRepo
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class InsertAlertTest {
    private lateinit var fakeAlertRepo: FakeAlertRepo
    private lateinit var insertAlert: InsertAlert

    @Before
    fun setup() {
        fakeAlertRepo = FakeAlertRepo()
        insertAlert = InsertAlert(fakeAlertRepo)
    }

    @Test
    fun `execute() insert the given alert`() = runBlockingTest {
        // Given
        val alert = Alerts(workId = "12")

        // When
        insertAlert.execute(alert)

        // Then
        Truth.assertThat(fakeAlertRepo.alertsList).contains(alert)
    }
}