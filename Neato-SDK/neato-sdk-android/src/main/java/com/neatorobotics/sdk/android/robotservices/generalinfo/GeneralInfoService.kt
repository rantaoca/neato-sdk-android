/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.generalinfo

import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.nucleo.Nucleo
import com.neatorobotics.sdk.android.clients.nucleo.json.NucleoJSONParser
import com.neatorobotics.sdk.android.models.GeneralInfo
import com.neatorobotics.sdk.android.models.Robot
import com.neatorobotics.sdk.android.models.hasService
import com.neatorobotics.sdk.android.robotservices.RobotService
import com.neatorobotics.sdk.android.robotservices.RobotServices
import org.json.JSONObject

val Robot.generalInfoService: GeneralInfoService?
    get() {
        if (this.state == null) return null
        return if (this.hasService(RobotServices.SERVICE_GENERAL_INFO)) {
            GeneralInfoServiceFactory.get(this.state!!.availableServices[RobotServices.SERVICE_GENERAL_INFO]!!)
        } else
            null
    }

abstract class GeneralInfoService: RobotService() {

    open suspend fun getInfo(robot: Robot): Resource<GeneralInfo> {
        val result = nucleoRepository.executeCommand(robot, JSONObject(Nucleo.GET_GENERAL_INFO_COMMAND))
        lateinit var out: GeneralInfo
        return if (result.status === Resource.Status.SUCCESS) {
            val info = NucleoJSONParser.parseGeneralInfo(result.data)
            Resource.success(info)
        } else {
            Resource.error(result.code, result.message)
        }
    }

    companion object {
        private const val TAG = "GeneralInfoService"
    }
}
