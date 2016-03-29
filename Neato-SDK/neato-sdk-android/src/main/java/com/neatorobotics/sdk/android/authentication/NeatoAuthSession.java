package com.neatorobotics.sdk.android.authentication;

/**
 * Created by Marco on 29/03/16.
 */
public class NeatoAuthSession {

    private static NeatoAuthSession instance = null;

    public static NeatoAuthSession getInstance() {
        if(instance == null) {
            instance = new NeatoAuthSession();
        }
        return instance;
    }

    //private singleton constructor
    private NeatoAuthSession() {}

    public void setAccessToken(String token) {
        //STORE
    }

    public boolean isAuthenticated() {
        //CHECK TOKEN VALIDITY
        return true;
    }
}
