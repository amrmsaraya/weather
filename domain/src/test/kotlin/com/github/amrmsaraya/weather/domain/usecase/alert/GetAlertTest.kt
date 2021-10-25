package com.github.amrmsaraya.weather.domain.usecase.alert

import com.github.amrmsaraya.weather.domain.model.Alerts
import com.github.amrmsaraya.weather.domain.repository.FakeAlertRepo
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetAlertTest {
    private lateinit var fakeAlertRepo: FakeAlertRepo
    private lateinit var getAlert: GetAlert

    @Before
    fun setup() {
        fakeAlertRepo = FakeAlertRepo()
        getAlert = GetAlert(fakeAlertRepo)
    }

    @Test
    fun `execute() with uuid then return alert`() = runBlockingTest {
        // Given
        val alert = Alerts(workId = "1")
        fakeAlertRepo.insert(alert)

        // when
        val result = getAlert.execute("1")

        // Then
        Truth.assertThat(result).isEqualTo(alert)
    }
}