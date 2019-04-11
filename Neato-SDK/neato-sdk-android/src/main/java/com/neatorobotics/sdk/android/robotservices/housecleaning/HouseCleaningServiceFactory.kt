/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.housecleaning

import com.neatorobotics.sdk.android.robotservices.RobotServices


object HouseCleaningServiceFactory {
    fun get(serviceVersion: String): HouseCleaningService? {
        return when {
            RobotServices.VERSION_BASIC_1.equals(serviceVersion, ignoreCase = true) -> HouseCleaningBasic1Service()
            RobotServices.VERSION_BASIC_3.equals(serviceVersion, ignoreCase = true) -> HouseCleaningBasic3Service()
            RobotServices.VERSION_MINIMAL_2.equals(serviceVersion, ignoreCase = true) -> HouseCleaningMinimal2Service()
            RobotServices.VERSION_BASIC_4.equals(serviceVersion, ignoreCase = true) -> HouseCleaningBasic4Service()
            RobotServices.VERSION_ADVANCED_4.equals(serviceVersion, ignoreCase = true) -> HouseCleaningAdvanced4Service()
            else -> null
        }
    }
}
