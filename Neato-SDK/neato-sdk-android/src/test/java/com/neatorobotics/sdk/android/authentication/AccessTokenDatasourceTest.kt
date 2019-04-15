package com.neatorobotics.sdk.android.authentication

import android.content.Context

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

import java.util.Date

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue

@RunWith(MockitoJUnitRunner::class)
class AccessTokenDatasourceTest {

    @Mock
    lateinit var context: Context

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `test custom access token datasource`() {
        val auth = NeatoAuthentication.apply {
            accessTokenDatasource = CustomAccessTokenDatasource()
        }
        val expires = Date()
        auth.setOauth2AccessToken("123", expires)
        assertEquals("123", auth.oauth2AccessToken)
        assertTrue(auth.isAuthenticated)
    }

    /**
     * Custom accessTokenDatasource for testing
     */
    private inner class CustomAccessTokenDatasource : AccessTokenDatasource {

        var token: String = ""
        var expires: Date = Date()

        override val isTokenValid: Boolean
            get() = true

        override fun storeToken(token: String, expires: Date) {
            this.token = token
            this.expires = expires
        }

        override fun loadToken(): String? {
            return token
        }

        override fun clearToken() {}
    }
}