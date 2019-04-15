package com.neatorobotics.sdk.android

import android.content.Context
import com.neatorobotics.sdk.android.authentication.DefaultAccessTokenDatasource
import com.neatorobotics.sdk.android.authentication.NeatoAuthentication
import com.neatorobotics.sdk.android.clients.beehive.BeehiveRepository
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking

import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.invocation.InvocationOnMock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.stubbing.Answer

import java.net.HttpURLConnection
import java.util.ArrayList

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.mockito.Matchers.any
import org.mockito.Matchers.anyString
import org.mockito.Mockito.*

@RunWith(MockitoJUnitRunner::class)
class NeatoUserTest {

    // singleton class under test
    var neatoUser = NeatoUser

    @Mock
    lateinit var mockNeatoAutentication: NeatoAuthentication

    @Mock
    lateinit var mockContext: Context

    @Mock
    lateinit var mockBeehiveRepository: BeehiveRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        NeatoSDK.applicationContext = mockContext

        neatoUser.neatoAuthentication = mockNeatoAutentication
        neatoUser.beehiveRepository = mockBeehiveRepository
    }

    @Test
    fun `NeatoUser is a singleton`() {
        val neatoUser1 = NeatoUser
        assertNotNull(neatoUser1)

        val neatoUser2 = NeatoUser
        assertNotNull(neatoUser2)

        assertEquals(neatoUser1, neatoUser2)
    }

    @Test
    fun `NeatoUser logout fun should call beehive repository logout fun`() {
        runBlocking {
            neatoUser.logout()
            verify(mockBeehiveRepository).logOut()
        }
    }

    @Test
    fun `NeatoUser loadRobots fun should call beehive repository loadRobots fun`() {
        runBlocking {
            neatoUser.loadRobots()
            verify(mockBeehiveRepository).loadRobots()
        }
    }

    @Test
    fun `NeatoUser getUserInfo fun should call beehive repository getUser fun`() {
        runBlocking {
            neatoUser.getUserInfo()
            verify(mockBeehiveRepository).getUser()
        }
    }
}