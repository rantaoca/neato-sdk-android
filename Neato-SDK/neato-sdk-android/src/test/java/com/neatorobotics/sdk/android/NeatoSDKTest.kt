package com.neatorobotics.sdk.android

import android.content.Context
import com.neatorobotics.sdk.android.authentication.DefaultAccessTokenDatasource
import com.neatorobotics.sdk.android.authentication.NeatoAuthentication
import com.neatorobotics.sdk.android.clients.beehive.BeehiveRepository
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking

import org.json.JSONObject
import org.junit.Assert.*
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

import org.mockito.Matchers.any
import org.mockito.Matchers.anyString
import org.mockito.Mockito.*

@RunWith(MockitoJUnitRunner::class)
class NeatoSDKTest {

    @Mock
    lateinit var mockContext: Context

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `test NeatoSDK context initialization`() {
        NeatoSDK.init(mockContext)
        assertNotNull(NeatoSDK.applicationContext)
        assertSame(NeatoSDK.applicationContext, mockContext)
    }
}