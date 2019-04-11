/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.logcopy

import com.neatorobotics.sdk.android.robotservices.RobotServices

object LogCopyServiceFactory {
    fun get(serviceVersion: String): LogCopyService? {
        return when {
            RobotServices.VERSION_BASIC_1.equals(serviceVersion, ignoreCase = true) -> LogCopyBasic1Service()
            else -> null
        }
    }
}
