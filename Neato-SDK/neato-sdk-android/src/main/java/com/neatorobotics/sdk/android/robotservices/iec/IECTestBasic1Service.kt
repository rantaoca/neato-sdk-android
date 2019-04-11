/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.iec

class IECTestBasic1Service : IECTestService() {

    override val isCleaningModeSupported: Boolean
        get() = false

    companion object {

        private val TAG = "IECTestBasic1Service"
    }
}
