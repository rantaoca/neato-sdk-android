/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.scheduling.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class RobotSchedule(val enabled: Boolean, val events: ArrayList<ScheduleEvent>) : Parcelable