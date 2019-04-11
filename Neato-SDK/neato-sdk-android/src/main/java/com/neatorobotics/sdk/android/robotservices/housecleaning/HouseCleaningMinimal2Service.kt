/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.housecleaning

class HouseCleaningMinimal2Service : HouseCleaningService() {

    override val isMultipleZonesCleaningSupported: Boolean
        get() = false

    override val isEcoModeSupported: Boolean
        get() = false

    override val isTurboModeSupported: Boolean
        get() = false

    override val isExtraCareModeSupported: Boolean
        get() = true

    override val isCleaningAreaSupported: Boolean
        get() = false

    override val isCleaningFrequencySupported: Boolean
        get() = false

    companion object {

        private const val TAG = "HouseCleaningMinimal2"
    }

}
