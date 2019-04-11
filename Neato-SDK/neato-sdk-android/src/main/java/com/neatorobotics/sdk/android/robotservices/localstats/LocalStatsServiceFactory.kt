/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.localstats

import com.neatorobotics.sdk.android.robotservices.RobotServices

object LocalStatsServiceFactory {
    fun get(serviceVersion: String): LocalStatsService? {
        return when {
            RobotServices.VERSION_ADVANCED_1.equals(serviceVersion, ignoreCase = true) -> LocalStatsAdvanced1Service()
            else -> null
        }
    }
}
