/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.preferences

import com.neatorobotics.sdk.android.robotservices.RobotServices

object PreferencesServiceFactory {
    fun get(serviceVersion: String): PreferencesService? {
        return when {
            RobotServices.VERSION_BASIC_1.equals(serviceVersion, ignoreCase = true) -> PreferencesBasic1Service()
            RobotServices.VERSION_BASIC_LCD_1.equals(serviceVersion, ignoreCase = true) -> PreferencesBasicLCD1Service()
            RobotServices.VERSION_BASIC_2.equals(serviceVersion, ignoreCase = true) -> PreferencesBasic2Service()
            else -> null
        }
    }
}
