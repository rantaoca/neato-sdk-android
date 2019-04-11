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
data class RobotPreferences(var sounds:Boolean = false) : Parcelable
