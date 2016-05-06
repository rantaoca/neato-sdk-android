package com.neatorobotics.sdk.android.utils;

import android.os.Build;

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */
public class DeviceUtils {

    public static String getModel(){
        return Build.MODEL;
    }
    public static String getManufacturer(){
        return Build.MANUFACTURER;
    }
    public static int getAndroidVersionAPI() {
        return Build.VERSION.SDK_INT;
    }

    public static String getXAgentString() {
        StringBuffer builder = new StringBuffer();
        builder.append("android-");
        builder.append(DeviceUtils.getAndroidVersionAPI());
        builder.append("|");
        builder.append(DeviceUtils.getModel());
        builder.append("|");
        builder.append(DeviceUtils.getManufacturer());
        return builder.toString();
    }
}
