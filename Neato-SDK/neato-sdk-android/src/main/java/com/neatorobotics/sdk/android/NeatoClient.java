package com.neatorobotics.sdk.android;
import com.neatorobotics.sdk.android.authentication.NeatoAuthSession;

import java.io.Serializable;

/**
 * Created by Marco on 11/03/16.
 */
public class NeatoClient implements Serializable{

    private NeatoAuthSession session = NeatoAuthSession.getInstance();
    private static NeatoClient instance = null;

    public static NeatoClient getInstance() {
        if(instance == null) {
            instance = new NeatoClient();
        }
        return instance;
    }

    //private singleton constructor
    private NeatoClient() {}

    public NeatoAuthSession getSession() {
        return session;
    }
}
