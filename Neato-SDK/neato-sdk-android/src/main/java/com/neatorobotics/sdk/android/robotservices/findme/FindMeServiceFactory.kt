/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.findme
import com.neatorobotics.sdk.android.robotservices.RobotServices

object FindMeServiceFactory {
    fun get(serviceVersion: String): FindMeService? {
        return when {
            RobotServices.VERSION_BASIC_1.equals(serviceVersion, ignoreCase = true) -> FindMeBasic1Service()
            else -> null
        }
    }
}
