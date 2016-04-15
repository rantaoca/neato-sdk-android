package com.neatorobotics.sdk.android.utils;

import android.os.Build;

/**
 * Created by Marco on 04/04/15.
 */
public class DeviceUtils {

    private static final String TAG = "DeviceUtils";

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
