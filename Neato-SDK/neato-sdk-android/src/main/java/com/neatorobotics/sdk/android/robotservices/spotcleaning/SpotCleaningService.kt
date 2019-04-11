/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.spotcleaning

import com.neatorobotics.sdk.android.models.CleaningCategory
import com.neatorobotics.sdk.android.models.Robot
import com.neatorobotics.sdk.android.models.hasService
import com.neatorobotics.sdk.android.robotservices.RobotServices
import com.neatorobotics.sdk.android.robotservices.cleaning.CleaningService


val Robot.spotCleaningService: SpotCleaningService?
    get() {
        if (this.state == null) return null
        return if (this.hasService(RobotServices.SERVICE_SPOT_CLEANING)) {
            SpotCleaningServiceFactory.get(this.state!!.availableServices[RobotServices.SERVICE_SPOT_CLEANING]!!)
        } else
            null
    }

abstract class SpotCleaningService : CleaningService() {
    init {
        this.cleaningType = CleaningCategory.SPOT
    }
}
