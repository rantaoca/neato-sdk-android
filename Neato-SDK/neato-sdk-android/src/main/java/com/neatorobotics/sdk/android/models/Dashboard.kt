/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

@Parcelize
class Dashboard(var robots: List<Robot> = ArrayList(),
                var locale: String? = null,
                var rawJson: String? = null,
                var countryCode: String? = null) : Parcelable
