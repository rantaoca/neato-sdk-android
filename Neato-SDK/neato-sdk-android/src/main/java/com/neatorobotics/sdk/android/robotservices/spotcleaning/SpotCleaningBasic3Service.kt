/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.spotcleaning

import com.neatorobotics.sdk.android.models.CleaningMode
import com.neatorobotics.sdk.android.models.CleaningModifier
import com.neatorobotics.sdk.android.models.NavigationMode

class SpotCleaningBasic3Service : SpotCleaningService() {

    override val isMultipleZonesCleaningSupported: Boolean
        get() = false

    override val isEcoModeSupported: Boolean
        get() = false

    override val isTurboModeSupported: Boolean
        get() = false

    override val isExtraCareModeSupported: Boolean
        get() = false

    override val isCleaningAreaSupported: Boolean
        get() = true

    override val isCleaningFrequencySupported: Boolean
        get() = false

    init {
        this.cleaningMode = CleaningMode.ECO
        this.cleaningModifier = CleaningModifier.NORMAL
        this.cleaningNavigationMode = NavigationMode.DEEP
    }

    companion object {

        private const val TAG = "SpotCleaningBasic3"
    }

}
