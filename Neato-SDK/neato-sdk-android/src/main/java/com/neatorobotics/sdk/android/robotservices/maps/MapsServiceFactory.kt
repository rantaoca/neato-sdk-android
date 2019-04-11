/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.maps

import com.neatorobotics.sdk.android.robotservices.RobotServices

object MapsServiceFactory {
    fun get(serviceVersion: String): MapsService? {
        return when {
            RobotServices.VERSION_BASIC_1.equals(serviceVersion, ignoreCase = true) -> MapsBasic1Service()
            RobotServices.VERSION_ADVANCED_1.equals(serviceVersion, ignoreCase = true) -> MapsAdvanced1Service()
            RobotServices.VERSION_MACRO_1.equals(serviceVersion, ignoreCase = true) -> MapsMacro1Service()
            RobotServices.VERSION_BASIC_2.equals(serviceVersion, ignoreCase = true) -> MapsBasic2Service()
            else -> null
        }
    }
}
