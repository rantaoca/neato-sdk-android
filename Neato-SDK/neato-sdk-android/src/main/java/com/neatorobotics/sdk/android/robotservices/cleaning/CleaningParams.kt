/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.cleaning

import android.os.Parcelable
import com.neatorobotics.sdk.android.models.CleaningCategory
import com.neatorobotics.sdk.android.models.CleaningMode
import com.neatorobotics.sdk.android.models.CleaningModifier
import com.neatorobotics.sdk.android.models.NavigationMode
import kotlinx.android.parcel.Parcelize

@Parcelize
class CleaningParams(var category: CleaningCategory? = null,
                     var mode: CleaningMode? = null,
                     var modifier: CleaningModifier? = null,
                     var navigationMode: NavigationMode? = null,
                     var spotSize: Int? = null,
                     var zoneId: String? = null,
                     var zonesId: List<String>? = null
                     ): Parcelable