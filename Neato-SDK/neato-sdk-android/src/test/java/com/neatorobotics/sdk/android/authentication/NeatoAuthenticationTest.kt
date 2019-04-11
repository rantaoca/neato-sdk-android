package com.neatorobotics.sdk.android.authentication

import android.content.Context
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

import org.junit.Assert.*

@RunWith(MockitoJUnitRunner::class)
class NeatoAuthenticationTest {

    private val FORMATTED_AUTH_URL =
        "https://beehive.neatocloud.com/oauth2/authorize?client_id=%1\$s&scope=%2\$s&response_type=token&redirect_uri=%3\$s"

    @Test
    fun buildOAuthAuthenticationUrl() {
        val clientId = "12323232"
        val redirectUri = "myapp://neato"
        val scopes = arrayOf(NeatoOAuth2Scope.CONTROL_ROBOTS)

        val neatoAuthentication = NeatoAuthentication.getInstance(mock())

        assertTrue(
            neatoAuthentication.buildOAuthAuthenticationUrl(
                FORMATTED_AUTH_URL,
                clientId,
                neatoAuthentication.buildScopesParameter(scopes),
                redirectUri
            ) == "https://beehive.neatocloud.com/oauth2/authorize?client_id=12323232&scope=control_robots&response_type=token&redirect_uri=myapp://neato"
        )
    }

    @Test
    fun buildOAuthAuthenticationUrlWithNullValues() {
        val clientId: String? = null
        val redirectUri: String? = null
        val scopes: Array<NeatoOAuth2Scope>? = null

        val neatoAuthentication = NeatoAuthentication.getInstance(mock())

        assertTrue(
            neatoAuthentication.buildOAuthAuthenticationUrl(
                FORMATTED_AUTH_URL,
                clientId,
                neatoAuthentication.buildScopesParameter(scopes),
                redirectUri
            ) == "https://beehive.neatocloud.com/oauth2/authorize?client_id=null&scope=&response_type=token&redirect_uri=null"
        )
    }

    @Test
    fun buildScopesParameter() {
        val neatoAuthentication = NeatoAuthentication.getInstance(mock())

        var scopes: Array<NeatoOAuth2Scope>? = arrayOf(NeatoOAuth2Scope.CONTROL_ROBOTS)
        assertTrue(neatoAuthentication.buildScopesParameter(scopes) == "control_robots")

        scopes = arrayOf(NeatoOAuth2Scope.CONTROL_ROBOTS, NeatoOAuth2Scope.PUBLIC_PROFILE)
        assertTrue(neatoAuthentication.buildScopesParameter(scopes) == "control_robots+public_profile")

        scopes = arrayOf(NeatoOAuth2Scope.PUBLIC_PROFILE, NeatoOAuth2Scope.CONTROL_ROBOTS)
        assertTrue(neatoAuthentication.buildScopesParameter(scopes) == "public_profile+control_robots")

        scopes = arrayOf()
        assertTrue(neatoAuthentication.buildScopesParameter(scopes) == "")

        scopes = null
        assertTrue(neatoAuthentication.buildScopesParameter(scopes) == "")
    }
}
