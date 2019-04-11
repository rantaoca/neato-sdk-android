/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.cleaning

import android.util.Log

import com.neatorobotics.sdk.android.clients.nucleo.Nucleo
import com.neatorobotics.sdk.android.robotservices.RobotService
import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.nucleo.NucleoRepository
import com.neatorobotics.sdk.android.models.*
import com.neatorobotics.sdk.android.robotservices.housecleaning.HouseCleaningBasic1Service
import org.json.JSONArray

import org.json.JSONException
import org.json.JSONObject

import java.util.HashMap

val Robot.cleaningService: CleaningService
    get() {return HouseCleaningBasic1Service()}

abstract class CleaningService : RobotService() {

    // default values
    protected var cleaningType = CleaningCategory.HOUSE

    protected var cleaningMode = CleaningMode.TURBO
    protected var cleaningModifier = CleaningModifier.NORMAL
    protected var cleaningNavigationMode = NavigationMode.NORMAL
    protected var cleaningAreaWidth = Nucleo.SPOT_AREA_LARGE
    protected var cleaningAreaHeight = Nucleo.SPOT_AREA_LARGE
    protected var boundaryId = ""
    protected var boundaryIds = listOf<String>()

    abstract val isEcoModeSupported: Boolean
    abstract val isTurboModeSupported: Boolean
    abstract val isExtraCareModeSupported: Boolean
    abstract val isCleaningAreaSupported: Boolean
    abstract val isCleaningFrequencySupported: Boolean
    abstract val isMultipleZonesCleaningSupported: Boolean

    suspend fun stopCleaning(robot: Robot, params: HashMap<String, String>? = null): Resource<RobotState> {
        val command = JSONObject().apply {
            put("reqId", "77")
            put("cmd", "stopCleaning")
        }

        val result = nucleoRepository.executeCommand(robot, command)

        val newState = RobotState(serial = robot.serial).apply {
            loadData(result.data)
        }

        return when(result.status) {
            Resource.Status.SUCCESS -> Resource.success(newState)
            else -> Resource.error(result.code, result.message, newState)
        }
    }

    suspend fun pauseCleaning(robot: Robot, params: HashMap<String, String>? = null): Resource<RobotState> {
        val command = JSONObject().apply {
            put("reqId", "77")
            put("cmd", "pauseCleaning")
        }
        val result = nucleoRepository.executeCommand(robot, command)

        val newState = RobotState(serial = robot.serial).apply {
            loadData(result.data)
        }

        return when(result.status) {
            Resource.Status.SUCCESS -> Resource.success(newState)
            else -> Resource.error(result.code, result.message, newState)
        }
    }

    suspend fun resumeCleaning(robot: Robot, params: HashMap<String, String>? = null): Resource<RobotState> {
        val command = JSONObject().apply {
            put("reqId", "77")
            put("cmd", "resumeCleaning")
        }
        val result = nucleoRepository.executeCommand(robot, command)

        val newState = RobotState(serial = robot.serial).apply {
            loadData(result.data)
        }

        return when(result.status) {
            Resource.Status.SUCCESS -> Resource.success(newState)
            else -> Resource.error(result.code, result.message, newState)
        }
    }

    suspend fun returnToBase(robot: Robot, params: HashMap<String, String>? = null): Resource<RobotState> {
        val command = JSONObject().apply {
            put("reqId", "77")
            put("cmd", "sendToBase")
        }

        val result = nucleoRepository.executeCommand(robot, command)

        val newState = RobotState(serial = robot.serial).apply {
            loadData(result.data)
        }

        return when(result.status) {
            Resource.Status.SUCCESS -> Resource.success(newState)
            else -> Resource.error(result.code, result.message, newState)
        }
    }

    open suspend fun startCleaning(robot: Robot, params: HashMap<String, String>? = null): Resource<RobotState> {
        // override default values, with user values
        // discard unsupported values
        if (params != null) {
            if (params.containsKey(Nucleo.CLEANING_CATEGORY_KEY)) {
                cleaningType = CleaningCategory.fromValue(Integer.parseInt(params[Nucleo.CLEANING_CATEGORY_KEY]))
            }
            if (params.containsKey(Nucleo.CLEANING_MODE_KEY)) {
                cleaningMode = CleaningMode.fromValue(Integer.parseInt(params[Nucleo.CLEANING_MODE_KEY]))
            }
            if (params.containsKey(Nucleo.CLEANING_MODIFIER_KEY)) {
                cleaningModifier = CleaningModifier.fromValue(Integer.parseInt(params[Nucleo.CLEANING_MODIFIER_KEY]))
            }
            if (params.containsKey(Nucleo.CLEANING_NAVIGATION_MODE_KEY)) {
                cleaningNavigationMode = NavigationMode.fromValue(Integer.parseInt(params[Nucleo.CLEANING_NAVIGATION_MODE_KEY]))
            }
            if (params.containsKey(Nucleo.CLEANING_AREA_SPOT_HEIGHT_KEY)) {
                cleaningAreaHeight = Integer.parseInt(params[Nucleo.CLEANING_AREA_SPOT_HEIGHT_KEY])
            }
            if (params.containsKey(Nucleo.CLEANING_AREA_SPOT_WIDTH_KEY)) {
                cleaningAreaWidth = Integer.parseInt(params[Nucleo.CLEANING_AREA_SPOT_WIDTH_KEY])
            }
            if (params.containsKey(Nucleo.ZONE_BOUNDARY_ID_KEY)) {
                boundaryId = params[Nucleo.ZONE_BOUNDARY_ID_KEY]?:""
            }
            if (params.containsKey(Nucleo.ZONE_BOUNDARIES_ID_KEY)) {
                boundaryIds = params[Nucleo.ZONE_BOUNDARIES_ID_KEY]?.split(",")?: listOf()
            }
        }

        val command = JSONObject()
        try {
            command.put("reqId", "77")
            command.put("cmd", "startCleaning")

            val commandParams = JSONObject()
            commandParams.put(Nucleo.CLEANING_CATEGORY_KEY, cleaningType.value)
            if (isEcoModeSupported && isTurboModeSupported) {
                commandParams.put(Nucleo.CLEANING_MODE_KEY, cleaningMode.value)
            }
            if (isCleaningFrequencySupported) {
                commandParams.put(Nucleo.CLEANING_MODIFIER_KEY, cleaningModifier.value)
            }

            // this is an exception house basic-1 support only frequency normal fixed
            if (this is HouseCleaningBasic1Service) {
                commandParams.put(Nucleo.CLEANING_MODIFIER_KEY, CleaningModifier.NORMAL.value)
            }

            if (isExtraCareModeSupported) {
                commandParams.put(Nucleo.CLEANING_NAVIGATION_MODE_KEY, cleaningNavigationMode.value)
            }

            if (!boundaryId.isNullOrEmpty()) {
                commandParams.put(Nucleo.ZONE_BOUNDARY_ID_KEY, boundaryId)
            }

            if (boundaryIds.isNotEmpty()) {
                commandParams.put(Nucleo.ZONE_BOUNDARIES_ID_KEY, JSONArray(boundaryIds))
            }

            if (isCleaningAreaSupported) {
                commandParams.put(Nucleo.CLEANING_AREA_SPOT_HEIGHT_KEY, cleaningAreaHeight)
                commandParams.put(Nucleo.CLEANING_AREA_SPOT_WIDTH_KEY, cleaningAreaWidth)
            }
            command.put("params", commandParams)
        } catch (e: JSONException) {
            Log.e(TAG, "Exception", e)
        }

        val result = nucleoRepository.executeCommand(robot, command)

        val newState = RobotState(serial = robot.serial).apply {
            loadData(result.data)
        }

        return when(result.status) {
            Resource.Status.SUCCESS -> Resource.success(newState)
            else -> Resource.error(result.code, result.message, newState)
        }
    }

    companion object {
        private const val TAG = "CleaningService"
    }
}
