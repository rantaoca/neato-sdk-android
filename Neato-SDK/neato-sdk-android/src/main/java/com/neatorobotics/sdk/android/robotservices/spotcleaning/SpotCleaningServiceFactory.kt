/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.spotcleaning

import com.neatorobotics.sdk.android.robotservices.RobotServices

object SpotCleaningServiceFactory {
    fun get(serviceVersion: String): SpotCleaningService? {
        return when {
            RobotServices.VERSION_BASIC_1.equals(serviceVersion, ignoreCase = true) -> SpotCleaningBasic1Service()
            RobotServices.VERSION_BASIC_3.equals(serviceVersion, ignoreCase = true) -> SpotCleaningBasic3Service()
            RobotServices.VERSION_MINIMAL_2.equals(serviceVersion, ignoreCase = true) -> SpotCleaningMinimal2Service()
            RobotServices.VERSION_MICRO_2.equals(serviceVersion, ignoreCase = true) -> SpotCleaningMicro2Service()
            else -> null
        }
    }
}
