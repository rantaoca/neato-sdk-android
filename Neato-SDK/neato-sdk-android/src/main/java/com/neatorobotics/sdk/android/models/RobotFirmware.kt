/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RobotFirmware(var model: String? = null,
                    var version: String? = null,
                    var url: String? = null,
                    var manualUpdateInfoUrl: String? = null,
                    var filesize: Int = 0,
                    var minRequiredVersion: String? = null) : Parcelable
