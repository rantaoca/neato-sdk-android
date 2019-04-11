/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.manualdrive

import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.models.ManualDriveInfo
import com.neatorobotics.sdk.android.models.Robot
import com.neatorobotics.sdk.android.models.hasService
import com.neatorobotics.sdk.android.robotservices.RobotService
import com.neatorobotics.sdk.android.robotservices.RobotServices

val Robot.manualDriveService: ManualDriveService?
    get() {
        if (this.state == null) return null
        return if (this.hasService(RobotServices.SERVICE_MANUAL_CLEANING)) {
            ManualDriveServiceFactory.get(this.state!!.availableServices[RobotServices.SERVICE_MANUAL_CLEANING]!!)
        } else
            null
    }

abstract class ManualDriveService: RobotService() {

    abstract suspend fun getInfo(robot: Robot): Resource<ManualDriveInfo>

    companion object {
        private const val TAG = "ManualDriveService"
    }
}
