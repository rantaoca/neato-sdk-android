package com.neatorobotics.sdk.android.model;

import com.neatorobotics.sdk.android.NeatoClient;

import java.io.Serializable;

/**
 * Created by Marco on 11/03/16.
 */
public class NeatoRobot implements Serializable {

    public String id;
    NeatoClient client;

    private NeatoRobotState state;

    public NeatoRobot(){
        client = NeatoClient.getInstance();
    }

    public void startCleaning() {
    }
}
