/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.spotcleaning

import com.neatorobotics.sdk.android.models.CleaningMode
import com.neatorobotics.sdk.android.models.CleaningModifier
import com.neatorobotics.sdk.android.models.NavigationMode

class SpotCleaningMicro2Service : SpotCleaningService() {

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

    init {
        this.cleaningModifier = CleaningModifier.DOUBLE
        this.cleaningNavigationMode = NavigationMode.NORMAL
    }

    companion object {

        private const val TAG = "SpotCleaningMicro2"
    }

}
