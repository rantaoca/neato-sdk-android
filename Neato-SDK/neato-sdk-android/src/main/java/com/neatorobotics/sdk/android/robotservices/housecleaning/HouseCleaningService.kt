/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.housecleaning

import com.neatorobotics.sdk.android.models.CleaningCategory
import com.neatorobotics.sdk.android.models.Robot
import com.neatorobotics.sdk.android.models.hasService
import com.neatorobotics.sdk.android.robotservices.RobotServices
import com.neatorobotics.sdk.android.robotservices.cleaning.CleaningService

val Robot.houseCleaningService: HouseCleaningService?
    get() {
        if (this.state == null) return null
        return if (this.hasService(RobotServices.SERVICE_HOUSE_CLEANING)) {
            HouseCleaningServiceFactory.get(this.state!!.availableServices[RobotServices.SERVICE_HOUSE_CLEANING]!!)
        } else
            null
    }

abstract class HouseCleaningService : CleaningService() {

    init {
        this.cleaningType = CleaningCategory.HOUSE
    }
}
