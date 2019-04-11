/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.findme
import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.nucleo.Nucleo
import com.neatorobotics.sdk.android.models.Robot
import com.neatorobotics.sdk.android.models.hasService
import com.neatorobotics.sdk.android.robotservices.RobotService
import com.neatorobotics.sdk.android.robotservices.RobotServices
import org.json.JSONObject

val Robot.findMeService: FindMeService?
    get() {
        if (this.state == null) return null
        return if (this.hasService(RobotServices.SERVICE_FIND_ME)) {
            FindMeServiceFactory.get(this.state!!.availableServices[RobotServices.SERVICE_FIND_ME]!!)
        } else
            null
    }

abstract class FindMeService: RobotService() {

    open suspend fun findMe(robot: Robot): Resource<Boolean> {
        val result = nucleoRepository.executeCommand(robot, JSONObject(Nucleo.FIND_ME_COMMAND))
        return if (result.status === Resource.Status.SUCCESS) {
            Resource.success(true)
        } else {
            Resource.error(result.code, result.message)
        }
    }

    companion object {
        private const val TAG = "FindMeService"
    }
}
