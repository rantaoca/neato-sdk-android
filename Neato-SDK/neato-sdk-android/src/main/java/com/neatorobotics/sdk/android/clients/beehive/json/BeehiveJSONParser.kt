/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.clients.beehive.json

import android.util.Log
import com.neatorobotics.sdk.android.models.*
import com.neatorobotics.sdk.android.utils.DateUtils
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.Calendar


object BeehiveJSONParser {

    private val TAG = "BeehiveJSONParser"

    fun parseRobot(json: JSONObject): Robot {
        return Robot().apply { loadFromJSON(json) }
    }

    fun parseRobotList(json: JSONObject?): Dashboard {
        val output = Dashboard(rawJson = json.toString())
        val robots = ArrayList<Robot>()
        if (json?.has("robots") != null) {
            try {
                val arr = json.getJSONArray("robots")
                for (i in 0 until arr.length()) {
                    val robot = Robot().apply { loadFromJSON(arr.getJSONObject(i)) }

                    //inject recent_firmwares
                    if (json.has("recent_firmwares")) {
                        val firmwares = json.getJSONObject("recent_firmwares")
                        robot.setRecentFirmwares(firmwares)
                    }

                    if (!robot.linkedAt.isNullOrEmpty()) {
                        robots.add(robot)
                    }
                }
                output.robots = robots
                //get current server locale
                if (json.has("locale")) {
                    val serverLocale = json.optString("locale", null)
                    output.locale = serverLocale
                }
                //get user country code
                if (json.has("country_code")) {
                    val countryCode = json.optString("country_code", null)
                    output.countryCode = countryCode
                }
            } catch (e: JSONException) {
                Log.d(TAG, e.message)
            }

        }
        return output
    }

    fun parseRobotMaps(json: JSONObject): List<CleaningMap> {
        val maps = mutableListOf<CleaningMap>()
        try {
            val arr = json.getJSONArray("maps")
            for (i in 0 until arr.length()) {
                //Only VERSION 1 Map now
                if (arr.getJSONObject(i).optInt("version", 0) == 1) {
                    val item = CleaningMap()
                    item.url = arr.getJSONObject(i).optString("url", "")
                    item.id = arr.getJSONObject(i).optString("id", "")
                    item.startAt = arr.getJSONObject(i).optString("start_at", "")
                    item.endAt = arr.getJSONObject(i).optString("end_at", "")
                    item.error = arr.getJSONObject(i).optString("error", "")
                    item.isDocked = arr.getJSONObject(i).optBoolean("is_docked", false)
                    item.isDelocalized = arr.getJSONObject(i).optBoolean("delocalized", false)
                    item.isValidAsPersistentMap = arr.getJSONObject(i).optBoolean("valid_as_persistent_map", false)
                    item.area = arr.getJSONObject(i).optDouble("cleaned_area", 0.0)
                    item.status = arr.getJSONObject(i).optString("status", CleaningMap.INVALID)
                    item.chargingTimeSeconds = arr.getJSONObject(i).optDouble("time_in_suspended_cleaning", 0.0)
                    item.timeInError = arr.getJSONObject(i).optDouble("time_in_error", 0.0)
                    item.timeInPause = arr.getJSONObject(i).optDouble("time_in_pause", 0.0)
                    //compute the expiration date
                    val urlValidForSeconds = arr.getJSONObject(i).optInt("url_valid_for_seconds", 300)
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.SECOND, urlValidForSeconds)
                    item.urlExpiresAt = calendar.time

                    //discard map/stats before EPOCH TIME
                    val start = DateUtils.getDate("yyyy-MM-dd'T'HH:mm:ss'Z'", item.startAt!!)
                    if (DateUtils.isAfterEpochTime(start!!)) {
                        maps.add(item)
                    }
                }
            }
        } catch (e: JSONException) {
            Log.d(TAG, e.message)
        }

        return maps.toList()
    }

    fun parseRobotMap(json: JSONObject): CleaningMap {
        val map = CleaningMap()
        //Only VERSION 1 Map now
        if (json.optInt("version", 0) == 1) {
            map.url = json.optString("url", "")
            map.id = json.optString("id", "")
            map.startAt = json.optString("start_at", "")
            map.endAt = json.optString("end_at", "")
            map.error = json.optString("error", "")
            map.isDocked = json.optBoolean("is_docked", false)
            map.isDelocalized = json.optBoolean("delocalized", false)
            map.area = json.optDouble("cleaned_area", 0.0)
            map.status = json.optString("status", CleaningMap.INVALID)
            map.chargingTimeSeconds = json.optDouble("time_in_suspended_cleaning", 0.0)
            map.timeInError = json.optDouble("time_in_error", 0.0)
            map.timeInPause = json.optDouble("time_in_pause", 0.0)
            //compute the expiration date
            val urlValidForSeconds = json.optInt("url_valid_for_seconds", 300)
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.SECOND, urlValidForSeconds)
            map.urlExpiresAt = calendar.time
        }
        return map
    }

    fun parseRobotPersistentMaps(json: JSONObject?): List<PersistentMap> {
        val maps = ArrayList<PersistentMap>()
        if (json != null) {
            try {
                val list = json.getJSONArray("value")
                for (i in 0 until list.length()) {
                    val mapJson = list.getJSONObject(i)
                    val map = PersistentMap()
                    var name = mapJson.optString("name")
                    if (name.isNullOrEmpty()) name = "My Home"
                    map.name = name
                    map.id = mapJson.optString("id")
                    map.url = mapJson.optString("url")
                    map.rawMapUrl = mapJson.optString("raw_floor_map_url")
                    //compute the expiration date
                    val urlValidForSeconds = mapJson.optInt("url_valid_for_seconds", 300)
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.SECOND, urlValidForSeconds)
                    map.expireAt = calendar.time
                    maps.add(map)
                }
            } catch (e: JSONException) {
                Log.e(TAG, "Exception", e)
            }

        }
        return maps
    }

    fun parseRobotExplorationMaps(json: JSONObject): List<PersistentMap> {
        val maps = ArrayList<PersistentMap>()
        try {
            val list = json.getJSONArray("exploration_maps")
            for (i in 0 until list.length()) {
                val mapJson = list.getJSONObject(i)
                val map = PersistentMap()
                map.name = mapJson.optString("name")
                map.id = mapJson.optString("id")
                map.url = mapJson.optString("url")
                //compute the expiration date
                val urlValidForSeconds = mapJson.optInt("url_valid_for_seconds", 300)
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.SECOND, urlValidForSeconds)
                map.expireAt = calendar.time
                maps.add(map)
            }
        } catch (e: JSONException) {
            Log.e(TAG, "Exception", e)
        }

        return maps
    }


    fun parseFirmware(data: JSONObject?): RobotFirmware {
        val output = RobotFirmware()
        if (data != null) {
            output.version = data.optString("version", "")
            output.url = data.optString("url", "")
            output.manualUpdateInfoUrl = data.optString("manual_update_info_url", "")
            output.filesize = data.optInt("filesize", 0)
            output.minRequiredVersion = data.optString("min_required_version", "0.0.0")
            if (output.minRequiredVersion.isNullOrEmpty()) {
                output.minRequiredVersion = "0.0.0"
            }
        }
        return output
    }

    fun parseUser(json: JSONObject?): UserInfo {
        val user = UserInfo()
        if(json == null) return user
        if (json.has("newsletter")) {
            user.newsletter = json.getBoolean("newsletter")
        }
        if (json.has("id")) {
            user.id = json.getString("id")
        }
        if (json.has("email")) {
            user.email = json.getString("email")
        }
        if (json.has("country_code")) {
            user.countryCode = json.getString("country_code")
        }
        if (json.has("locale")) {
            user.locale = json.getString("locale")
        }
        if (json.has("first_name")) {
            user.first_name = json.getString("first_name")
        }
        if (json.has("last_name")) {
            user.last_name = json.getString("last_name")
        }
        return user
    }
}
