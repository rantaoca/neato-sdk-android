/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.models

import android.graphics.PointF
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Boundary(// local app id
        var id: String? = null,
        var type: String = "polyline", //polyline or polygon
        var name: String = "",
        var color: String = "#000000",
        var isEnabled: Boolean = true,
        var isSelected: Boolean = false,
        var isValid: Boolean = true,
        var vertices: MutableList<PointF>? = null,
        var relevancy: PointF? = null, //interest point inside a zone
        var state: BoundaryStatus = BoundaryStatus.NONE) : Parcelable

enum class BoundaryStatus {
    NONE, PENDING, CURRENT, SKIPPED, COMPLETED
}
