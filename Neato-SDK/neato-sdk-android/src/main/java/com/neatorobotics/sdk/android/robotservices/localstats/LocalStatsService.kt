/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.localstats

import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.nucleo.Nucleo
import com.neatorobotics.sdk.android.clients.nucleo.json.NucleoJSONParser
import com.neatorobotics.sdk.android.models.LocalStats
import com.neatorobotics.sdk.android.models.Robot
import com.neatorobotics.sdk.android.models.hasService
import com.neatorobotics.sdk.android.robotservices.RobotService
import com.neatorobotics.sdk.android.robotservices.RobotServices
import org.json.JSONObject

val Robot.localStatsService: LocalStatsService?
    get() {
        if (this.state == null) return null
        return if (this.hasService(RobotServices.SERVICE_LOCAL_STATS)) {
            LocalStatsServiceFactory.get(this.state!!.availableServices[RobotServices.SERVICE_LOCAL_STATS]!!)
        } else
            null
    }

abstract class LocalStatsService: RobotService() {

    open suspend fun getStats(robot: Robot): Resource<LocalStats> {
        val result = nucleoRepository.executeCommand(robot, JSONObject(Nucleo.GET_ROBOT_LOCAL_STATS_COMMAND))
        return if (result.status === Resource.Status.SUCCESS) {
            Resource.success(NucleoJSONParser.parseLocalStats(result.data))
        } else {
            Resource.error(result.code, result.message)
        }
    }

    companion object {
        private const val TAG = "FindMeService"
    }
}
