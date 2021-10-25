package com.github.amrmsaraya.weather.domain.usecase.alert

import com.github.amrmsaraya.weather.domain.model.Alerts
import com.github.amrmsaraya.weather.domain.repository.FakeAlertRepo
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DeleteAlertTest {
    private lateinit var fakeAlertRepo: FakeAlertRepo
    private lateinit var deleteAlert: DeleteAlert

    @Before
    fun setup() {
        fakeAlertRepo = FakeAlertRepo()
        deleteAlert = DeleteAlert(fakeAlertRepo)
    }

    @Test
    fun `execute() with alert we want to delete then it should be deleted`() = runBlockingTest {
        // Given
        val alert = Alerts(workId = "1234")
        val alert2 = Alerts(workId = "2468")
        fakeAlertRepo.insert(alert)
        fakeAlertRepo.insert(alert2)

        // When
        deleteAlert.execute("2468")

        // Then
        Truth.assertThat(fakeAlertRepo.alertsList).isEqualTo(listOf(alert))
    }
}