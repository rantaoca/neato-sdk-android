/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.maps

class MapsMacro1Service : MapsAdvanced1Service() {

    override val isFloorPlanSupported: Boolean
        get() = true

    override fun areMultipleFloorPlanSupported(): Boolean {
        return true
    }

    override fun areZonesSupported(): Boolean {
        return false
    }

    companion object {

        private const val TAG = "MapsMacro1Service"
    }
}