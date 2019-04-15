/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.utils

import android.os.Build

object DeviceUtils {

    val model: String
        get() = Build.MODEL
    val manufacturer: String
        get() = Build.MANUFACTURER
    val androidVersionAPI: Int
        get() = Build.VERSION.SDK_INT

    val xAgentString: String
        get() {
            val builder = StringBuffer()
            builder.append("android-")
            builder.append(DeviceUtils.androidVersionAPI)
            builder.append("|")
            builder.append(DeviceUtils.model)
            builder.append("|")
            builder.append(DeviceUtils.manufacturer)
            return builder.toString()
        }
}
