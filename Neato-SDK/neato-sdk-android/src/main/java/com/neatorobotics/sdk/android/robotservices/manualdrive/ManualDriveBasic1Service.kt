/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.manualdrive

import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.nucleo.Nucleo
import com.neatorobotics.sdk.android.clients.nucleo.json.NucleoJSONParser
import com.neatorobotics.sdk.android.models.ManualDriveInfo
import com.neatorobotics.sdk.android.models.Robot
import org.json.JSONObject


class ManualDriveBasic1Service: ManualDriveService() {
    override suspend fun getInfo(robot: Robot): Resource<ManualDriveInfo> {
        val result = nucleoRepository.executeCommand(robot, JSONObject( Nucleo.MANUAL_DRIVE_INFO_COMMAND))
        return when(result.status) {
            Resource.Status.SUCCESS -> Resource.success(NucleoJSONParser.parseManualDriveInfo(result.data))
            else -> Resource.error(result.code, result.message)
        }
    }
}