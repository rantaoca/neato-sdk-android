/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.logcopy

import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.models.Robot

class LogCopyBasic1Service: LogCopyService() {
    override suspend fun uploadLogs(robot: Robot): Resource<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getLogsUploadState(robot: Robot): Resource<CopyLogProgress> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}