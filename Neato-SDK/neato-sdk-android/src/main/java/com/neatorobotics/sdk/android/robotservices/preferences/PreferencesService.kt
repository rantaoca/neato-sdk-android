/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.preferences

import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.robotservices.RobotServices
import com.neatorobotics.sdk.android.clients.nucleo.Nucleo
import com.neatorobotics.sdk.android.clients.nucleo.json.NucleoJSONParser
import com.neatorobotics.sdk.android.models.Robot
import com.neatorobotics.sdk.android.models.RobotPreferences
import com.neatorobotics.sdk.android.models.hasService
import com.neatorobotics.sdk.android.robotservices.RobotService
import org.json.JSONObject

val Robot.preferencesService: PreferencesService?
    get() {
        if (this.state == null) return null
        return if (this.hasService(RobotServices.SERVICE_PREFERENCES)) {
            PreferencesServiceFactory.get(this.state!!.availableServices[RobotServices.SERVICE_PREFERENCES]!!)
        } else
            null
    }

abstract class PreferencesService: RobotService() {

    open suspend fun getPreferences(robot: Robot): Resource<RobotPreferences> {
        val result = nucleoRepository.executeCommand(robot, JSONObject(Nucleo.GET_PREFERENCES_COMMAND))
        lateinit var out: RobotPreferences
        return if (result.status === Resource.Status.SUCCESS) {
            out = NucleoJSONParser.parsePreferences(result.data)
            Resource.success(out)
        } else {
            Resource.error(result.code, result.message)
        }
    }

    open suspend fun setPreferences(robot: Robot, preferences: JSONObject): Resource<Boolean> {
        val input = JSONObject(Nucleo.SET_PREFERENCES_COMMAND).apply {
            put("params", preferences)
        }
        val result = nucleoRepository.executeCommand(robot, input)
        return if (result.status === Resource.Status.SUCCESS) {
            Resource.success(true)
        }else Resource.error(result.code, result.message)
    }

    companion object {
        private const val TAG = "PreferencesService"
    }
}
