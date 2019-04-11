/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.iec

import com.neatorobotics.sdk.android.robotservices.RobotServices

object IECTestServiceFactory {
    fun get(serviceVersion: String): IECTestService? {
        return when {
            RobotServices.VERSION_BASIC_1.equals(serviceVersion, ignoreCase = true) -> IECTestBasic1Service()
            RobotServices.VERSION_ADVANCED_1.equals(serviceVersion, ignoreCase = true) -> IECTestAdvanced1Service()
            else -> null
        }
    }
}
