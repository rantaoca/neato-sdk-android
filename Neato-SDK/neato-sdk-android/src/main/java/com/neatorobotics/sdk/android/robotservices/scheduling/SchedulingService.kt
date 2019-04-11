/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.scheduling

import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.models.Robot
import com.neatorobotics.sdk.android.models.RobotSchedule
import com.neatorobotics.sdk.android.models.hasService
import com.neatorobotics.sdk.android.robotservices.RobotService
import com.neatorobotics.sdk.android.robotservices.RobotServices

import org.json.JSONObject

val Robot.schedulingService: SchedulingService?
    get() {
        if (this.state == null) return null
        return if (this.hasService(RobotServices.SERVICE_SCHEDULE)) {
            SchedulingServiceFactory.get(this.state!!.availableServices[RobotServices.SERVICE_SCHEDULE]!!)
        } else
            null
    }

abstract class SchedulingService: RobotService() {

    abstract suspend fun getSchedule(robot: Robot): Resource<RobotSchedule>
    abstract suspend fun setSchedule(robot: Robot, schedule: RobotSchedule): Resource<Boolean>

    abstract val cleaningModeSupported: Boolean
    abstract val singleZoneSupported: Boolean
    abstract val multipleZoneSupported: Boolean

    open suspend fun enableSchedule(robot: Robot): Resource<Boolean> {
        val command = JSONObject().apply {
            put("reqId", "77")
            put("cmd", "enableSchedule")
        }

        val result = nucleoRepository.executeCommand(robot, command)
        return when(result.status) {
            Resource.Status.SUCCESS -> Resource.success(true)
            else -> Resource.error(result.code, result.message)
        }
    }

    open suspend fun disableSchedule(robot: Robot): Resource<Boolean> {
        val command = JSONObject().apply {
            put("reqId", "77")
            put("cmd", "disableSchedule")
        }

        val result = nucleoRepository.executeCommand(robot, command)
        return when(result.status) {
            Resource.Status.SUCCESS -> Resource.success(true)
            else -> Resource.error(result.code, result.message)
        }
    }

    companion object {

        private const val TAG = "SchedulingService"
    }
}
