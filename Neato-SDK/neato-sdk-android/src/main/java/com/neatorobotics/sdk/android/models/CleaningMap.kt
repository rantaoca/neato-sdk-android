/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.Date

@Parcelize
class CleaningMap(
    var url: String? = null,
    var id: String? = null,
    var startAt: String? = null,
    var endAt: String? = null,
    var error: String? = null,
    var status: String? = null,
    var isDocked: Boolean = false,
    var isDelocalized: Boolean = false,
    var isValidAsPersistentMap: Boolean = false,
    var area: Double = 0.toDouble(),
    var chargingTimeSeconds: Double = 0.toDouble(),
    var timeInError: Double = 0.toDouble(),
    var timeInPause: Double = 0.toDouble(),
    var urlExpiresAt: Date? = null,
    var rotation:Int = 0
) : Parcelable {

    //milliseconds
    //30 secs of margin
    val isExpired: Boolean
        get() {
            val difference = urlExpiresAt!!.time - System.currentTimeMillis()
            return difference < 30 * 1000
        }

    companion object {

        val COMPLETE = "complete"
        val INCOMPLETE = "incomplete"
        val CANCELLED = "cancelled"
        val INVALID = "invalid"
    }
}
