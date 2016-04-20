package com.neatorobotics.sdk.android;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.neatorobotics.sdk.android.authentication.AccessTokenDatasource;
import com.neatorobotics.sdk.android.authentication.DefaultAccessTokenDatasource;
import com.neatorobotics.sdk.android.authentication.NeatoAuthentication;
import com.neatorobotics.sdk.android.authentication.NeatoAuthenticationResponse;
import com.neatorobotics.sdk.android.authentication.NeatoOAuth2Scope;
import com.neatorobotics.sdk.android.beehive.BeehiveClient;
import com.neatorobotics.sdk.android.model.NeatoRobot;
import com.neatorobotics.sdk.android.nucleo.NucleoClient;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Marco on 11/03/16.
 */
public class NeatoClient {

    private Context context;
    private static NeatoClient instance = null;
    private NeatoAuthentication neatoAuthentication;
    private AccessTokenDatasource accessTokenDatasource;

    protected NucleoClient nucleo = new NucleoClient();
    protected BeehiveClient beehive;

    /**
     * Use this method to get the singleton neato client that use the default access token datasource.
     * @param context
     * @return
     */
    public static NeatoClient getInstance(Context context) {
        if(instance == null) {
            instance = new NeatoClient(context);
        }
        return instance;
    }

    /**
     * Use this method to get the singleton neato client that use a custom access token datasource.
     * @param context
     * @param accessTokenDatasource
     * @return
     */
    public static NeatoClient getInstance(Context context, AccessTokenDatasource accessTokenDatasource) {
        NeatoClient client = getInstance(context);
        client.accessTokenDatasource = accessTokenDatasource;
        return client;
    }

    /**
     * Private singleton constructor
     * @param context
     */
    private NeatoClient(Context context) {
        this.context = context;
        beehive = new BeehiveClient(context.getString(R.string.beehive_endpoint));
        accessTokenDatasource = new DefaultAccessTokenDatasource(context);
    }

    /**
     * Start the OAuth 2 authentication flow
     */
    public void openLoginInBrowser(String clientId, String redirectUri, NeatoOAuth2Scope[] scopes) {
        neatoAuthentication = new NeatoAuthentication(clientId,redirectUri,scopes);
        neatoAuthentication.openLoginInBrowser(context);
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

    /**
     * Revoke the current access token
     * @param callback
     */
    public void logout(final NeatoCallback<Boolean> callback) {
        beehive.logout(getOauth2AccessToken(), new NeatoCallback<Boolean>() {
            @Override
            public void done(Boolean result) {
                super.done(result);
                clearAccessToken();
                callback.done(result);
            }

            @Override
            public void fail(NeatoError error) {
                super.fail(error);
                callback.fail(error);
            }
        });
    }

    /**
     * Retrieve the user robots list.
     * @param callback
     */
    public void loadRobots(final NeatoCallback<ArrayList<NeatoRobot>> callback) {
        beehive.loadRobots(getOauth2AccessToken(), new NeatoCallback<ArrayList<NeatoRobot>>() {
            @Override
            public void done(ArrayList<NeatoRobot> result) {
                super.done(result);
                callback.done(result);
            }

            @Override
            public void fail(NeatoError error) {
                super.fail(error);
                callback.fail(error);
            }
        });
    }
}
