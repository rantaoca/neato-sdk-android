package com.neatorobotics.sdk.android.models;

import java.io.Serializable;

/**
 * RobotFirmware model
 */
public class RobotFirmware implements Serializable {
    private static final String TAG = "RobotFirmware";

    public String model, version, url, changelogurl;
    public int filesize;

    //costructors
    public RobotFirmware(){}
    public RobotFirmware(String model, String version, String url, int filesize, String changelogurl){
        this.model = model;
        this.version = version;
        this.url = url;
        this.filesize = filesize;
        this.changelogurl = changelogurl;
    }
}
