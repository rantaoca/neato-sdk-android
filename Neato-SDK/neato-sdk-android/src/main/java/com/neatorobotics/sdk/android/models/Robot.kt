/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet

@Parcelize
data class Robot(var serial: String? = null,
            var prefix: String? = null,
            var name: String? = null,
            var mac: String? = null,
            var model: String? = null,
            var model_readable: String? = null,
            var firmware: String? = null,
            var tz_info: String? = null,
            var created_at: String? = null,
            var provisioned_at: String? = null,
            var secret_key: String? = null,
            var lat: Double = 0.toDouble(),
            var lon: Double = 0.toDouble(),
            var linkedAt: String? = null,
            var nucleoUrl: String? = null,
            var state: RobotState? = null,
            var recentFirmware: HashMap<String, RobotFirmware> = HashMap(),
            var traits: MutableSet<String> = HashSet(),
            var explorationId: String? = null,
            var persistentMapsIds: MutableList<String> = ArrayList(),
            var persistentMapsNames: MutableList<String> = ArrayList()) : Parcelable
