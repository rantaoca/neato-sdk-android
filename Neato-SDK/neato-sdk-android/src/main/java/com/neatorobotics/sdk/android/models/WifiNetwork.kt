/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class WifiNetwork(val ssid: String, val strength: Int,
                  val isConnected: Boolean,
                  var ipAddress: String? = null,
                  var subnetMask: String? = null,
                  var router: String? = null,
                  var macAddress: String? = null): Parcelable
