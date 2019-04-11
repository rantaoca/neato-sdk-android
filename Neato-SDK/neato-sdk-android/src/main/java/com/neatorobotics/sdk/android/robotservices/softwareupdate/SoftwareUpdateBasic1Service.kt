/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.softwareupdate

import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.nucleo.Nucleo
import com.neatorobotics.sdk.android.models.Robot
import org.json.JSONObject


class SoftwareUpdateBasic1Service: SoftwareUpdateService() {

    override suspend fun startSoftwareUpdate(robot: Robot, version: String, url: String, fileSize: Int): Resource<Boolean> {
        val command = JSONObject(
            Nucleo.START_ROBOT_UPDATE
                .replace("VERSION_PLACEHOLDER",version)
                .replace("URL_PLACEHOLDER",url)
                .replace("SIZE_PLACEHOLDER",fileSize.toString()))

        val result = nucleoRepository.executeCommand(robot, command)
        return when(result.status) {
            Resource.Status.SUCCESS -> Resource.success(true)
            else -> Resource.error(result.code, result.message)
        }
    }
}