/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.ArrayList

@Parcelize
class LocalStats(
    var cleaningDays: ArrayList<CleaningDayItem>? = null,
    var cleaningAreas: ArrayList<CleaningDayItem>? = null,
    var avarageCleaningPlusChargingTime: Int = 0,
    var avarageCleaningArea: Double = 0.toDouble(),
    var totalCleanedTime: Int = 0,
    var totalCleanedArea: Double = 0.toDouble()
): Parcelable


