/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.iec

import android.util.Log
import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.nucleo.Nucleo
import com.neatorobotics.sdk.android.models.CleaningMode
import com.neatorobotics.sdk.android.models.Robot
import com.neatorobotics.sdk.android.models.hasService
import com.neatorobotics.sdk.android.robotservices.RobotService
import com.neatorobotics.sdk.android.robotservices.RobotServices

import org.json.JSONException
import org.json.JSONObject

import java.util.HashMap

val Robot.iecTestService: IECTestService?
    get() {
        if (this.state == null) return null
        return if (this.hasService(RobotServices.SERVICE_IEC_TEST)) {
            IECTestServiceFactory.get(this.state!!.availableServices[RobotServices.SERVICE_IEC_TEST]!!)
        } else
            null
    }

abstract class IECTestService : RobotService() {

    protected var cleaningMode = CleaningMode.TURBO
    protected var speed = 150// 10-300
    protected var distance = 1200// 20-4000
    protected var carpet = true

    abstract val isCleaningModeSupported: Boolean

    suspend fun startIECTest(robot: Robot, params: HashMap<String, String>?): Resource<Unit> {
        // override default values, with user values
        // discard unsupported values
        if (params != null) {
            if (params.containsKey(Nucleo.CLEANING_MODE_KEY)) {
                cleaningMode = CleaningMode.fromValue(Integer.parseInt(params[Nucleo.CLEANING_MODE_KEY]))
            }
            if (params.containsKey(Nucleo.IEC_TEST_SPEED_KEY)) {
                speed = Integer.parseInt(params[Nucleo.IEC_TEST_SPEED_KEY])
            }
            if (params.containsKey(Nucleo.IEC_TEST_DISTANCE_KEY)) {
                distance = Integer.parseInt(params[Nucleo.IEC_TEST_DISTANCE_KEY])
            }
            if (params.containsKey(Nucleo.IEC_TEST_CARPET_KEY)) {
                carpet = java.lang.Boolean.parseBoolean(params[Nucleo.IEC_TEST_CARPET_KEY])
            }
        }

        val command = JSONObject()
        try {
            command.put("reqId", "77")
            command.put("cmd", "startIECTest")

            val commandParams = JSONObject()
            if (isCleaningModeSupported) {
                commandParams.put(Nucleo.CLEANING_MODE_KEY, cleaningMode)
            }

            commandParams.put(Nucleo.IEC_TEST_SPEED_KEY, speed)
            commandParams.put(Nucleo.IEC_TEST_DISTANCE_KEY, distance)
            commandParams.put(Nucleo.IEC_TEST_CARPET_KEY, carpet)

            command.put("params", commandParams)
        } catch (e: JSONException) {
            Log.e(TAG, "Exception", e)
        }

        val result = nucleoRepository.executeCommand(robot, command)
        return when(result.status) {
            Resource.Status.SUCCESS -> Resource.success(Unit)
            else -> Resource.error(result.code, result.message)
        }
    }

    companion object {

        private const val TAG = "IECTestService"
    }

}
