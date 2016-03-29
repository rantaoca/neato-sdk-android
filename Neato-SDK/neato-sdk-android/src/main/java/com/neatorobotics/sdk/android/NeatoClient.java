package com.neatorobotics.sdk.android;
import android.content.Context;

import com.neatorobotics.sdk.android.authentication.NeatoAuthSession;

import java.io.Serializable;

/**
 * Created by Marco on 11/03/16.
 */
public class NeatoClient {

    private NeatoAuthSession session;
    private static NeatoClient instance = null;

    public static NeatoClient getInstance(Context context) {
        if(instance == null) {
            instance = new NeatoClient(context);
        }
        return instance;
    }

    //private singleton constructor
    private NeatoClient(Context context) {
        session = NeatoAuthSession.getInstance(context);
    }

    public NeatoAuthSession getSession() {
        return session;
    }
}
