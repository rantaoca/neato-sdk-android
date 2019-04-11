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
import com.neatorobotics.sdk.android.robotservices.scheduling.model.ScheduleEvent

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

class SchedulingBasic1Service : SchedulingService() {

    override val cleaningModeSupported: Boolean
        get() = true
    override val singleZoneSupported: Boolean
        get() = false
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
        val command = Nucleo.SET_SCHEDULE_COMMAND.replace("EVENTS_PLACEHOLDER", "events:" + convertEventsToJSON(schedule.events))

        val result = nucleoRepository.executeCommand(robot, JSONObject(command))
        return when(result.status) {
            Resource.Status.SUCCESS -> Resource.success(true)
            else -> Resource.error(result.code, result.message)
        }
    }

    private fun convertEventsToJSON(events: ArrayList<ScheduleEvent>): String {
        val array = JSONArray()
        for (i in events.indices) {
            array.put(convertEventToJSON(events[i]))
        }
        return array.toString()
    }

    fun convertEventToJSON(event: ScheduleEvent): JSONObject? {
        val json = JSONObject()
        try {
            json.put("startTime", event.startTime)
            json.put("mode", event.mode)
            json.put("day", event.day)
        } catch (e: JSONException) {
            Log.e(TAG, "Exception", e)
            return null
        }
        return json
    }

    companion object {

        private const val TAG = "SchedulingBasic1"
    }
}
