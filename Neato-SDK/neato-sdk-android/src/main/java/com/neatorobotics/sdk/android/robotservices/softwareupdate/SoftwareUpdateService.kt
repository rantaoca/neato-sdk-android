/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.softwareupdate

import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.models.Robot
import com.neatorobotics.sdk.android.models.hasService
import com.neatorobotics.sdk.android.robotservices.RobotService
import com.neatorobotics.sdk.android.robotservices.RobotServices

val Robot.softwareUpdateService: SoftwareUpdateService?
    get() {
        if (this.state == null) return null
        return if (this.hasService(RobotServices.SERVICE_SOFTWARE_UPDATE)) {
            SoftwareUpdateServiceFactory.get(this.state!!.availableServices[RobotServices.SERVICE_SOFTWARE_UPDATE]!!)
        } else
            null
    }

abstract class SoftwareUpdateService: RobotService() {

    abstract suspend fun startSoftwareUpdate(robot: Robot, version: String, url: String, fileSize: Int): Resource<Boolean>

    companion object {
        private const val TAG = "SoftwareUpdateService"
    }
}
