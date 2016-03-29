package com.neatorobotics.sdk.android.authentication;

import android.content.Context;

import java.util.Date;

/**
 * Created by Marco on 29/03/16.
 */
public class NeatoAuthSession {

    private static NeatoAuthSession instance = null;
    private AccessTokenDatasource accessTokenDatasource;

    public static NeatoAuthSession getInstance(Context context) {
        if(instance == null) {
            instance = new NeatoAuthSession(context);
        }
        return instance;
    }

    //private singleton constructor
    private NeatoAuthSession(Context context) {
        accessTokenDatasource = new DefaultAccessTokenDatasource(context);
    }

    /**
     * To override the default accessTokenDataSource, for example if you want to store
     * securely the token. By default the SDK se the DefaultAccessTokenDatasource.java
     * @param accessTokenDatasource
     */
    public void setAccessTokenDatasource(AccessTokenDatasource accessTokenDatasource) {
        this.accessTokenDatasource = accessTokenDatasource;
    }

    public void setAccessToken(String token, Date expires) {
        accessTokenDatasource.saveToken(token, expires);
    }

    public String getAccessToken() {
        return accessTokenDatasource.getToken();
    }

    public boolean isAuthenticated() {
        return accessTokenDatasource.isTokenValid();
    }
}
