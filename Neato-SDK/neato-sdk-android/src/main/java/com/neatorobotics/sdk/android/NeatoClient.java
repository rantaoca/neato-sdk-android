package com.neatorobotics.sdk.android;

import com.neatorobotics.sdk.android.nucleo.NucleoClient;

import java.io.Serializable;

/**
 * Created by Marco on 11/03/16.
 */
public class NeatoClient implements Serializable{
    public void startCleaning(String robotID) {
        NucleoClient.call();
    }
}
