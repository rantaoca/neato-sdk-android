/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.authentication

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.io.InvalidObjectException

import java.util.Date

object NeatoAuthentication {

    private val OAUTH_AUTH_URL = "https://apps.neatorobotics.com/oauth2/authorize?client_id=%1\$s&amp;scope=%2\$s&amp;response_type=token&amp;redirect_uri=%3\$s"

    var accessTokenDatasource: AccessTokenDatasource? = null

    /**
     * Use this method to check if the current user is authenticated
     * @return true if the client has a stored access token
     */
    val isAuthenticated: Boolean
        get() = accessTokenDatasource!!.isTokenValid

    /**
     * Use this method to get the current OAuth2 access token
     * @return
     */
    val oauth2AccessToken: String?
        get() = accessTokenDatasource!!.loadToken()

    init {
        this.accessTokenDatasource = DefaultAccessTokenDatasource()
    }

    /**
     * Start the OAuth 2 authentication flow
     */
    fun openLoginInBrowser(context: Context, clientId: String, redirectUri: String, scopes: Array<NeatoOAuth2Scope>) {
        val authUrl = buildOAuthAuthenticationUrl(OAUTH_AUTH_URL, clientId, buildScopesParameter(scopes), redirectUri)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
        context.startActivity(intent)

        clearAccessToken()
    }

    /**
     * Parse the OAuth2 response starting from the response URI
     * @param uri
     * @return the NeatoAuthenticationResponse type
     */
    fun getOAuth2AuthResponseFromUri(uri: Uri): NeatoAuthenticationResponse {
        val response = NeatoAuthenticationResponse.fromUri(uri)
        when (response.type) {
            // Response was successful and contains auth token
            NeatoAuthenticationResponse.Response.TOKEN -> setOauth2AccessToken(
                response.token?:"",
                response.tokenExpirationDate?:Date()
            )
            NeatoAuthenticationResponse.Response.ERROR -> clearAccessToken()
        }// Most likely auth flow was cancelled
        //You can do nothing
        return response
    }

    /**
     *
     * @return the OAuth 2 authentication string URL
     */
    fun buildOAuthAuthenticationUrl(
        formattedUrl: String,
        clientId: String?,
        scopes: String,
        redirectUri: String?
    ): String {
        return String.format(formattedUrl, clientId, scopes, redirectUri)
    }

    /**
     *
     * @param scopes
     * @return the comma separated string of scopes
     */
    fun buildScopesParameter(scopes: Array<NeatoOAuth2Scope>?): String {
        val scopesBuffer = StringBuffer("")
        if (scopes != null && scopes.isNotEmpty()) {
            for (scope in scopes) {
                scopesBuffer.append(scope)
                scopesBuffer.append("+")
            }
            scopesBuffer.deleteCharAt(scopesBuffer.length - 1)
        }
        return scopesBuffer.toString()
    }

    /**
     * Clear the current OAuth2 access token
     */
    fun clearAccessToken() {
        accessTokenDatasource!!.clearToken()
    }

    /**
     * Call this method after the end of a successful OAuth2.0 authentication flow. This method setup
     * the NeatoClient with the obtained access token.
     * @param token
     * @param tokenExpirationDate
     */
    fun setOauth2AccessToken(token: String, tokenExpirationDate: Date) {
        accessTokenDatasource?.storeToken(token, tokenExpirationDate)
    }
}
