package com.neatorobotics.sdk.android.utils

import android.os.Build

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */
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
            builder.append("androidsdk-")
            builder.append(DeviceUtils.androidVersionAPI)
            builder.append("|")
            builder.append(DeviceUtils.model)
            builder.append("|")
            builder.append(DeviceUtils.manufacturer)
            return builder.toString()
        }
}
