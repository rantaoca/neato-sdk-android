/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.models


import java.io.Serializable
import java.util.Date

class CleaningDayItem(val date: Date?, val value: Double, val chargingTimeMinutes: Int, val errorTimeSeconds: Int,
                      val pauseTimeSeconds: Int, val mode: CleaningMode) : Serializable {

    var map: CleaningMap? = null
    var isSelected: Boolean = false
}
