/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.softwareupdate

import com.neatorobotics.sdk.android.robotservices.RobotServices

object SoftwareUpdateServiceFactory {
    fun get(serviceVersion: String): SoftwareUpdateService? {
        return when {
            RobotServices.VERSION_BASIC_1.equals(serviceVersion, ignoreCase = true) -> SoftwareUpdateBasic1Service()
            else -> null
        }
    }
}
