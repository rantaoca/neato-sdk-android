/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.clients.beehive

import com.neatorobotics.sdk.android.authentication.AccessTokenDatasource
import com.neatorobotics.sdk.android.authentication.NeatoAuthentication
import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.ResourceState
import com.neatorobotics.sdk.android.utils.GET
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
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class BeehiveRepositoryTest {

    private lateinit var repository: BeehiveRepository

    @Mock
    private lateinit var mockHttpClient: BeehiveHttpClient
    private val endpoint = "beehive_endpoint"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val mockErrorsProvider = object: BeehiveErrorsProvider() {
            override fun description(code: ResourceState): String {
                return "my_mocked_error"
            }
        }

        // class under test
        repository = BeehiveRepository(endpoint, mockErrorsProvider)
        repository.client = mockHttpClient
    }

    @Test
    fun `test logout params`() {
        runBlocking {
            whenever(mockHttpClient.call(any(), any(), any())).thenReturn(Resource.success(JSONObject()))

            NeatoAuthentication.accessTokenDatasource = object : AccessTokenDatasource {
                override val isTokenValid: Boolean = true

                override fun storeToken(token: String, expires: Date) {}

                override fun loadToken(): String? = "456789"

                override fun clearToken() {}

            }

            repository.logOut()
            verify(mockHttpClient).call(eq(POST), eq("$endpoint/oauth2/revoke"), any())
        }
    }

    @Test
    fun `test logout with success`() {
        runBlocking {
            whenever(mockHttpClient.call(any(), any(), any())).thenReturn(Resource.success(JSONObject()))

            val logout = repository.logOut()
            assertEquals(Resource.Status.SUCCESS, logout.status)
        }
    }

    @Test
    fun `test logout with failure`() {
        runBlocking {
            whenever(mockHttpClient.call(any(), any(), any())).thenReturn(Resource.error(ResourceState.BAD_REQUEST, "my message"))

            val logout = repository.logOut()
            assertEquals(Resource.Status.ERROR, logout.status)
        }
    }

    @Test
    fun `test load robots arguments`() {
        runBlocking {
            whenever(mockHttpClient.call(any(), any(), isNull())).thenReturn(Resource.error(ResourceState.BAD_REQUEST, "my message"))
            repository.loadRobots()
            verify(mockHttpClient).call(eq(GET), eq("$endpoint/users/me/robots"), isNull())
        }
    }
}
