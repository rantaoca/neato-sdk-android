/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ScheduleEvent(var startTime: String? = null,//24hours format
                    var mode: CleaningMode = CleaningMode.TURBO,
                    var day: Int = 0,//0 is sunday
                    var boundaryIds: MutableList<String>? = null,
                    var boundaryNames: MutableList<String>? = null,
                    var mapId: String? = null) : Parcelable
