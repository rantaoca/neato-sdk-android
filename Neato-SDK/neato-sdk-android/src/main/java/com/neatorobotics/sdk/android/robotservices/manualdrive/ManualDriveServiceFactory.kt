/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.manualdrive

import com.neatorobotics.sdk.android.robotservices.RobotServices

object ManualDriveServiceFactory {
    fun get(serviceVersion: String): ManualDriveService? {
        return when {
            RobotServices.VERSION_BASIC_1.equals(serviceVersion, ignoreCase = true) -> ManualDriveBasic1Service()
            else -> null
        }
    }
}
