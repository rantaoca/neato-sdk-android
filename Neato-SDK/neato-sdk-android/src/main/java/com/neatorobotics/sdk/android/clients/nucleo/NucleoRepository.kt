/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.clients.nucleo

import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.ResourceState
import com.neatorobotics.sdk.android.models.Robot
import com.neatorobotics.sdk.android.models.RobotState
import com.neatorobotics.sdk.android.models.loadData
import com.neatorobotics.sdk.android.utils.POST
import com.neatorobotics.sdk.android.utils.retry
import org.json.JSONObject

class NucleoRepository(private val errorsProvider: NucleoErrorsProvider = NucleoErrorsProvider()) {

    var client = NucleoHttpClient()

    suspend fun getRobotState(robot: Robot): Resource<RobotState> {

        val result = retry(
                times = 1,
                initialDelay = 500,
                block = {
                    val command = Nucleo.GET_ROBOT_STATE_COMMAND.replace("77", "" + reqId++ % 1000)
                    executeCommand(robot, JSONObject(command))
                },
                exitBlock = {
                    it.status == Resource.Status.SUCCESS
                },
                default = Resource.error(ResourceState.HTTP_NOT_FOUND, data = JSONObject())
        )

        val out = RobotState(serial = robot.serial)
        return if (result.status === Resource.Status.SUCCESS) {
            out.loadData(result.data)
            Resource.success(out)
        } else {
            out.loadData(result.data)
            Resource.error(result.code, errorsProvider.description(result.code), out)
        }
    }

    suspend fun executeCommand(robot: Robot, params: JSONObject): Resource<JSONObject> {
        val result = client.call(POST, Nucleo.URL, robot.serial?:"", params.toString(), robot.secret_key?:"")
        return if (result.status === Resource.Status.SUCCESS) {
            Resource.success(result.data?:JSONObject())
        } else {
            Resource.error(result.code, errorsProvider.description(result.code), result.data?: JSONObject())
        }
    }

    companion object {

        private const val TAG = "NucleoRepository"
        private var reqId = 1
    }
}