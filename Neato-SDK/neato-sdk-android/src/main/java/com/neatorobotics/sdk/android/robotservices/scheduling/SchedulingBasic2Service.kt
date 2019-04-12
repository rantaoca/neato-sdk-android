/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.scheduling

import android.util.Log
import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.nucleo.Nucleo
import com.neatorobotics.sdk.android.clients.nucleo.json.NucleoJSONParser
import com.neatorobotics.sdk.android.models.Robot
import com.neatorobotics.sdk.android.models.RobotSchedule
import com.neatorobotics.sdk.android.models.ScheduleEvent

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

class SchedulingBasic2Service : SchedulingService() {

    override val cleaningModeSupported: Boolean
        get() = true
    override val singleZoneSupported: Boolean
        get() = true
    override val multipleZoneSupported: Boolean
        get() = false

    override suspend fun getSchedule(robot: Robot): Resource<RobotSchedule> {
        val result = nucleoRepository.executeCommand(robot, JSONObject(Nucleo.GET_ROBOT_SCHEDULE_COMMAND))
        return when(result.status) {
            Resource.Status.SUCCESS -> Resource.success(NucleoJSONParser.parseSchedule(result.data))
            else -> Resource.error(result.code, result.message)
        }
    }

    override suspend fun setSchedule(robot: Robot, schedule: RobotSchedule): Resource<Boolean> {
        val command = Nucleo.SET_SCHEDULE_COMMAND.replace("EVENTS_PLACEHOLDER", "events:" + convertEventsToJSON(schedule.events, true))
        val result = nucleoRepository.executeCommand(robot, JSONObject(command))
        return when(result.status) {
            Resource.Status.SUCCESS -> Resource.success(true)
            else -> Resource.error(result.code, result.message)
        }
    }

    fun convertEventsToJSON(events: ArrayList<ScheduleEvent>, toSendToTheRobot: Boolean): String {
        val array = JSONArray()
        for (i in events.indices) {
            array.put(convertEventToJSON(events[i], toSendToTheRobot))
        }
        return array.toString()
    }

    fun convertEventToJSON(event: ScheduleEvent, toSendToTheRobot: Boolean = false): JSONObject? {
        val json = JSONObject()
        try {
            json.put("mode", event.mode.value)
            json.put("day", event.day)
            json.put("startTime", event.startTime)

            if (toSendToTheRobot) {
                if (event.boundaryIds?.isNotEmpty() == true) {
                    json.put("boundaryId", event.boundaryIds!![0])
                }
            } else {
                if (event.boundaryIds?.isNotEmpty() == true) {
                    val boundaryJson = JSONObject()
                    boundaryJson.put("id", event.boundaryIds!![0])
                    if (!event.boundaryNames!![0].isNullOrEmpty()) {
                        boundaryJson.put("name", event.boundaryNames!![0])
                    }
                    json.put("boundary", boundaryJson)
                }
                if (!event.mapId.isNullOrEmpty()) {
                    json.put("mapId", event.mapId)
                }
            }

        } catch (e: JSONException) {
            Log.e(TAG, "Exception", e)
            return null
        }

        return json
    }

    companion object {

        private val TAG = "SchedulingBasic2"
    }
}
