/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.wifi

import com.neatorobotics.sdk.android.robotservices.RobotServices

object WifiServiceFactory {
    fun get(serviceVersion: String): WifiService? {
        return when {
            RobotServices.VERSION_BASIC_1.equals(serviceVersion, ignoreCase = true) -> WifiBasic1Service()
            else -> null
        }
    }
}
