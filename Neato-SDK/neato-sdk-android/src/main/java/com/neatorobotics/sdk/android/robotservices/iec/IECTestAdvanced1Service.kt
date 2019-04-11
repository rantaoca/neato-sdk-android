/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.iec

class IECTestAdvanced1Service : IECTestService() {

    override val isCleaningModeSupported: Boolean
        get() = true

    companion object {

        private val TAG = "IECTestAdvanced1Service"
    }

}
