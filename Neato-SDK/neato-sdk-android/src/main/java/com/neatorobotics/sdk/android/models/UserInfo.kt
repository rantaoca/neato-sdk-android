/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserInfo( var email: String? = null,
                var id: String? = null,
                var countryCode: String? = null,
                var first_name: String? = null,
                var last_name: String? = null,
                var locale: String? = null,
                var newsletter: Boolean = false): Parcelable
