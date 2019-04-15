/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.clients.nucleo

import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.ResourceState
import com.neatorobotics.sdk.android.models.Robot
import com.neatorobotics.sdk.android.utils.POST
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock

import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

import org.junit.Assert.*

@RunWith(MockitoJUnitRunner::class)
class NucleoRepositoryTest {

    private lateinit var repository: NucleoRepository

    @Mock
    private lateinit var mockHttpClient: NucleoHttpClient

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val mockErrorsProvider = object: NucleoErrorsProvider() {
            override fun description(code: ResourceState): String {
                return "my_mocked_error"
            }
        }

        // class under test
        repository = NucleoRepository(mockErrorsProvider)
        repository.client = mockHttpClient
    }

    @Test
    fun `test get robot state call`() {
        runBlocking {
            whenever(mockHttpClient.call(any(), any(), any(), any(), any())).thenReturn(Resource.success(JSONObject()))

            repository.getRobotState(Robot(serial = "123", secret_key = "secret"))

            argumentCaptor<String>().apply {
                verify(mockHttpClient).call(eq(POST), eq(Nucleo.URL), eq("123"), capture(), eq("secret"))
                assertTrue(firstValue.contains("getRobotState"))
            }
        }
    }

}
