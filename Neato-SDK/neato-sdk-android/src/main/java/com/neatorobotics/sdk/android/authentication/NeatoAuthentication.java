package com.neatorobotics.sdk.android.authentication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.neatorobotics.sdk.android.R;

import java.util.Date;

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */
public class NeatoAuthentication {

    private static NeatoAuthentication instance = null;
    private AccessTokenDatasource accessTokenDatasource;

    /**
     * Use this method to get the singleton that use the default access token datasource.
     * @param context
     * @return
     */
    public static NeatoAuthentication getInstance(Context context) {
        if(instance == null) {
            instance = new NeatoAuthentication(context);
        }
        return instance;
    }

    /**
     * Use this method to get the singleton that use a custom access token datasource.
     * @param context
     * @param accessTokenDatasource
     * @return
     */
    public static NeatoAuthentication getInstance(Context context, AccessTokenDatasource accessTokenDatasource) {
        NeatoAuthentication auth = getInstance(context);
        auth.accessTokenDatasource = accessTokenDatasource;
        return auth;
    }

    /**
     *
     * @param context
     */
    private NeatoAuthentication(Context context){
        this.accessTokenDatasource = new DefaultAccessTokenDatasource(context);
    }

    /**
     * Start the OAuth 2 authentication flow
     */
    public void openLoginInBrowser(Context context,String clientId, String redirectUri, NeatoOAuth2Scope[] scopes) {
        String authUrl = buildOAuthAuthenticationUrl(context.getString(R.string.oauth2_authentication_url),clientId,buildScopesParameter(scopes), redirectUri);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl));
        context.startActivity(intent);

        clearAccessToken();
    }

    /**
     * Parse the OAuth2 response starting from the response URI
     * @param uri
     * @return the NeatoAuthenticationResponse type
     */
    public NeatoAuthenticationResponse getOAuth2AuthResponseFromUri(Uri uri) {
        NeatoAuthenticationResponse response = NeatoAuthenticationResponse.fromUri(uri);
        switch (response.getType()) {
            // Response was successful and contains auth token
            case TOKEN:
                setOauth2AccessToken(response.getToken(), response.getTokenExpirationDate());
                break;
            case ERROR:
                clearAccessToken();
                break;
            // Most likely auth flow was cancelled
            default:
                //You can do nothing
        }
        return response;
    }

    /**
     *
     * @return the OAuth 2 authentication string URL
     */
    protected String buildOAuthAuthenticationUrl(String formattedUrl, String clientId, String scopes, String redirectUri) {
        String authUrl = String.format(formattedUrl, clientId, scopes, redirectUri);
        return authUrl;
    }

    /**
     *
     * @param scopes
     * @return the comma separated string of scopes
     */
    protected String buildScopesParameter(NeatoOAuth2Scope[] scopes) {
        StringBuffer scopesBuffer = new StringBuffer("");
        if(scopes != null && scopes.length > 0) {
            for (NeatoOAuth2Scope scope : scopes) {
                scopesBuffer.append(scope);
                scopesBuffer.append(",");
            }
            scopesBuffer.deleteCharAt(scopesBuffer.length()-1);
        }
        return scopesBuffer.toString();
    }

    /**
     * Use this method to check if the current user is authenticated
     * @return true if the client has a stored access token
     */
    public boolean isAuthenticated() {
        return accessTokenDatasource.isTokenValid();
    }

    /**
     * Clear the current OAuth2 access token
     */
    public void clearAccessToken(){
        accessTokenDatasource.clearToken();
    }

    /**
     * Call this method after the end of a successful OAuth2.0 authentication flow. This method setup
     * the NeatoClient with the obtained access token.
     * @param token
     * @param tokenExpirationDate
     */
    public void setOauth2AccessToken(String token, Date tokenExpirationDate) {
        accessTokenDatasource.storeToken(token, tokenExpirationDate);
    }

    /**
     * Use this method to get the current OAuth2 access token
     * @return
     */
    public String getOauth2AccessToken() {
        return accessTokenDatasource.loadToken();
    }
}
