/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.models

import android.util.Log
import com.neatorobotics.sdk.android.clients.nucleo.json.NucleoJSONParser
import org.json.JSONException
import org.json.JSONObject

private const val TAG = "RobotStateExtensions"

fun RobotState.loadData(json: JSONObject?) {

    if (json == null) return //no data

    //root
    try {
        message = json.optString("message", "")
        message = if (message == null) "" else message
        result = json.optString("result", "")
        robotRemoteProtocolVersion = json.optInt("version", -1)
        state = State.fromValue(json.optInt("state", -1))
        error = json.optString("error", "")
        alert = if (json.isNull("alert")) null else json.optString("alert", null)
        if (json.has("action")) {
            action = Action.fromValue(json.optInt("action", -1))
        }

        //details
        if (json.has("details")) {
            val details = json.getJSONObject("details")

            charge = details.optDouble("charge", -1.0)
            isCharging = details.optBoolean("isCharging", false)
            isDocked = details.optBoolean("isDocked", false)
            isDockHasBeenSeen = details.optBoolean("dockHasBeenSeen", false)
            isScheduleEnabled = details.optBoolean("isScheduleEnabled", false)
        }

        //cleaning
        boundaries.clear()
        if (json.has("cleaning")) {
            val cleaning = json.getJSONObject("cleaning")

            cleaningCategory = CleaningCategory.fromValue(cleaning.optInt("category", -1))
            cleaningModifier = CleaningModifier.fromValue(cleaning.optInt("modifier", -1))
            cleaningMode = CleaningMode.fromValue(cleaning.optInt("mode", -1))
            cleaningSpotWidth = cleaning.optInt("spotWidth", -1)
            cleaningSpotHeight = cleaning.optInt("spotHeight", -1)
            navigationMode = NavigationMode.fromValue(cleaning.optInt("navigationMode", -1))
            mapId = cleaning.optString("mapId", null)
            if (cleaning.has("boundary")) {
                val bJson = cleaning.getJSONObject("boundary")
                if(bJson.has("id") && bJson.has("name")) {
                    boundary = Boundary().apply {
                        id = bJson.optString("id", "")
                        name = bJson.optString("name", "")
                        color = "#000000"
                    }
                }
            }
            if(cleaning.has("boundaries")) {
                val bArray = cleaning.getJSONArray("boundaries")
                for(i in 0 until bArray.length()) {
                    boundaries.add(Boundary().apply {
                        name = bArray.getJSONObject(i).getString("name")
                        id = bArray.getJSONObject(i).getString("id")
                        color = "#000000"
                        state = when(bArray.getJSONObject(i).getString("state")) {
                            "current" -> BoundaryStatus.CURRENT
                            "pending" -> BoundaryStatus.PENDING
                            "skipped" -> BoundaryStatus.SKIPPED
                            "completed" -> BoundaryStatus.COMPLETED
                            else -> BoundaryStatus.NONE
                        }
                    })
                }
            }
        }

        //available commands
        if (json.has("availableCommands")) {
            val availableCommands = json.getJSONObject("availableCommands")
            isStartAvailable = availableCommands.optBoolean("start", false)
            isStopAvailable = availableCommands.optBoolean("stop", false)
            isPauseAvailable = availableCommands.optBoolean("pause", false)
            isResumeAvailable = availableCommands.optBoolean("resume", false)
            isGoToBaseAvailable = availableCommands.optBoolean("goToBase", false)
        }

        //available services
        if (json.has("availableServices")) {
            val availableServicesJSON = json.getJSONObject("availableServices")
            val iterator = availableServicesJSON.keys()
            while (iterator.hasNext()) {
                val serviceName = iterator.next()
                availableServices[serviceName] = availableServicesJSON.optString(serviceName)
            }
        }

        //meta
        if (json.has("meta")) {
            val meta = json.getJSONObject("meta")
            robotModelName = meta.optString("modelName", "")
            firmware = meta.optString("firmware", "")
        }

        //schedule events (only basic-1 or minimal-1)
        scheduleEvents.clear()
        if (json.has("data")) {
            val data = json.getJSONObject("data")
            if (data.has("events")) {
                isScheduleEnabled = data.optBoolean("enabled", false)
                val events = data.getJSONArray("events")
                for (e in 0 until events.length()) {
                    val se = NucleoJSONParser.parseScheduleEvent(events.get(e) as JSONObject)
                    scheduleEvents.add(se)
                }
            }
        }
    } catch (e: JSONException) {
        Log.e(TAG, "Exception", e)
    }

}

val RobotState.isOnline: Boolean
    get() = !this.result.isNullOrEmpty()

val RobotState.iscleaning: Boolean
    get() {
        return (state == State.BUSY || state == State.PAUSED) &&
                (action == Action.HOUSE_CLEANING    ||
                 action == Action.SPOT_CLEANING     ||
                 action == Action.MANUAL_CLEANING   ||
                 action == Action.MAP_CLEANING)

    }
