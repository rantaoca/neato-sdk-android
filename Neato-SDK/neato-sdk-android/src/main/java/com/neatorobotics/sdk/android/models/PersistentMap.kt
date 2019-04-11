/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
class PersistentMap(var id: String? = null,
                    var name: String? = null,
                    var url: String? = null,
                    var rawMapUrl: String? = null,
                    var expireAt: Date? = null) : Parcelable{

    //milliseconds
    //30 secs of margin
    val isExpired: Boolean
        get() {
            val difference = expireAt?.time?:Long.MAX_VALUE - System.currentTimeMillis()
            return difference < 30 * 1000
        }
}
