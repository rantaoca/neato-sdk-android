/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.logcopy

import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.models.Robot
import com.neatorobotics.sdk.android.models.hasService
import com.neatorobotics.sdk.android.robotservices.RobotService
import com.neatorobotics.sdk.android.robotservices.RobotServices

val Robot.logCopyService: LogCopyService?
    get() {
        if (this.state == null) return null
        return if (this.hasService(RobotServices.SERVICE_LOG_COPY)) {
            LogCopyServiceFactory.get(this.state!!.availableServices[RobotServices.SERVICE_LOG_COPY]!!)
        } else
            null
    }

abstract class LogCopyService: RobotService() {

    abstract suspend fun uploadLogs(robot: Robot): Resource<Boolean>
    abstract suspend fun getLogsUploadState(robot: Robot): Resource<CopyLogProgress>

    companion object {
        private const val TAG = "LogCopyService"
    }
}
