/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.scheduling

import com.neatorobotics.sdk.android.robotservices.RobotServices

object SchedulingServiceFactory {
    fun get(serviceVersion: String): SchedulingService? {
        return when {
            RobotServices.VERSION_BASIC_1.equals(serviceVersion, ignoreCase = true) -> SchedulingBasic1Service()
            RobotServices.VERSION_MINIMAL_1.equals(serviceVersion, ignoreCase = true) -> SchedulingMinimal1Service()
            RobotServices.VERSION_BASIC_2.equals(serviceVersion, ignoreCase = true) -> SchedulingBasic2Service()
            RobotServices.VERSION_ADVANCED_2.equals(serviceVersion, ignoreCase = true) -> SchedulingAdvanced2Service()
            else -> null
        }
    }
}
