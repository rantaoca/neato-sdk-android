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

    @Mock
    lateinit var ctx: Context

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        NeatoSDK.applicationContext = mockContext

        neatoUser.neatoAuthentication = mockNeatoAutentication
        neatoUser.beehiveRepository = mockBeehiveRepository
    }

    @Test
    fun singletonTest() {
        val neatoUser1 = NeatoUser
        assertNotNull(neatoUser1)

        val neatoUser2 = NeatoUser
        assertNotNull(neatoUser2)

        assertEquals(neatoUser1, neatoUser2)
    }

    @Test
    fun logout() {
        runBlocking {
            neatoUser.logout()
            verify(mockBeehiveRepository).logOut()
        }
    }

    @Test
    fun loadRobots() {
        runBlocking {
            neatoUser.loadRobots()
            verify(mockBeehiveRepository).loadRobots()
        }
    }

    @Test
    fun loadRobots_InvalidToken() {

    }

    @Test
    fun loadRobots_Error() {

    }

    @Test
    fun loadRobots_GenericError() {

    }

    @Test
    fun loadRobots_NullJSON() {

    }

    @Test
    fun loadRobots_OK_Robot_In_List() {

    }

    @Test
    fun getUserInfo() {
        runBlocking {
            neatoUser.getUserInfo()
            verify(mockBeehiveRepository).getUser()
        }
    }

    @Test
    fun getUserInfoFail() {

    }
}