/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.generalinfo

import com.neatorobotics.sdk.android.robotservices.RobotServices

object GeneralInfoServiceFactory {
    fun get(serviceVersion: String): GeneralInfoService? {
        return when {
            RobotServices.VERSION_BASIC_1.equals(serviceVersion, ignoreCase = true) -> GeneralInfoBasic1Service()
            RobotServices.VERSION_BASIC_LCD_1.equals(serviceVersion, ignoreCase = true) -> GeneralInfoBasicLCD1Service()
            else -> null
        }
    }
}
