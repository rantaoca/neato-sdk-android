/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ManualDriveInfo(val ip: String, val port: Int, val ssid: String? = null) : Parcelable