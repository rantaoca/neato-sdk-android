/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.utils

import com.neatorobotics.sdk.android.R
import com.neatorobotics.sdk.android.clients.ResourceState
import org.json.JSONObject

/**
 * These are the result codes returned by the robot after a direct call.
 */
object RobotResults {

    const val ok = "ok"
    const val ko = "ko"
    const val not_found = "not_found"
    const val command_rejected = "command_rejected"
    const val invalid_entry = "invalid_entry"
    const val max_boundaries_exceeded = "max_boundaries_exceeded"
    const val not_on_charge_base = "not_on_charge_base"
    const val not_idle = "not_idle"
    const val command_not_found = "command_not_found"
    const val bad_request = "bad_request"
    const val invalid_json = "invalid_json"
    const val already_added = "already_added"

    fun getRobotResultResourceState(json: JSONObject?): ResourceState {
        if (json != null && json.has("result") && json.optString("result") != null) {
            val result = json.optString("result")
            return if (!result.isNullOrEmpty()) {
                when (result) {
                    not_found -> ResourceState.NOT_FOUND
                    ok -> ResourceState.OK
                    ko -> ResourceState.KO
                    command_rejected -> ResourceState.COMMAND_REJECTED
                    invalid_entry -> ResourceState.INVALID_ENTRY
                    max_boundaries_exceeded -> ResourceState.MAX_BOUNDARIES_EXCEDEED
                    not_on_charge_base -> ResourceState.NOT_ON_CHARGE_BASE
                    not_idle -> ResourceState.NOT_IDLE
                    command_not_found -> ResourceState.COMMAND_NOT_FOUND
                    bad_request -> ResourceState.BAD_REQUEST
                    invalid_json -> ResourceState.INVALID_JSON
                    already_added -> ResourceState.ALREADY_ADDED
                    else -> ResourceState.UNKNOWN
                }
            } else
                ResourceState.INVALID
        }
        return ResourceState.INVALID
    }

    fun isRobotResponseOK(json: JSONObject): Boolean {
        val resourceState = getRobotResultResourceState(json)
        return resourceState == ResourceState.OK
    }
}
