/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class GeneralInfo(
    var productNumber: String? = null,
    var serial: String? = null,
    var model: String? = null,
    var language: String? = null,
    var firmware: String? = null,
    var batteryLevel: Int = 0,
    var timeToEmpty: Int = 0,
    var timeToFullCharge: Int = 0,
    var totalCharges: Int = 0,
    var manufacturingDate: String? = null,
    var authorizationStatus: Int = 0,
    var vendor: String? = null
) : Parcelable