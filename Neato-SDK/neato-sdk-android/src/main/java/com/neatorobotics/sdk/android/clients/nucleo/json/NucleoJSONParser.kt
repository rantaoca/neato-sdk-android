/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.clients.nucleo.json

import android.graphics.PointF
import android.util.Log
import com.neatorobotics.sdk.android.models.*
import com.neatorobotics.sdk.android.robotservices.scheduling.model.ScheduleEvent
import com.neatorobotics.sdk.android.utils.DateUtils
import org.json.JSONException
import org.json.JSONObject
import java.util.*

object NucleoJSONParser {

    private val TAG = "NucleoJSONParser"

    fun parseBoundaries(json: JSONObject?): List<Boundary> {
        val list = ArrayList<Boundary>()
        try {
            if (json != null) {
                val array = json.getJSONObject("data").getJSONArray("boundaries")
                for (i in 0 until array.length()) {
                    val b = array.getJSONObject(i)
                    val boundary = Boundary()
                    boundary.id = b.optString("id", "")//b.optInt("id", i)
                    boundary.color = b.optString("color", "#000000")
                    boundary.isEnabled = b.optBoolean("enabled", true)
                    boundary.name = b.optString("name", "" + i)
                    boundary.type = b.optString("type", "polyline")

                    val vertices = ArrayList<PointF>()
                    if (!b.has("vertices")) continue
                    val verticesArray = b.getJSONArray("vertices")
                    for (v in 0 until verticesArray.length()) {
                        val vertex = verticesArray.getJSONArray(v)
                        val point = PointF(vertex.getDouble(0).toFloat(), vertex.getDouble(1).toFloat())
                        vertices.add(point)
                    }
                    boundary.vertices = vertices
                    list.add(boundary)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return list
    }

    fun parseCleaningTimesStats(json: JSONObject?): ArrayList<CleaningDayItem> {
        val cleanings = ArrayList<CleaningDayItem>()
        try {
            val data = json?.getJSONObject("data")

            if (data != null && data.has("houseCleaning")) {
                val houseCleaning = data.getJSONObject("houseCleaning")
                if (houseCleaning.has("history")) {
                    val history = houseCleaning.getJSONArray("history")
                    for (i in 0 until history.length()) {
                        val item = history.getJSONObject(i)

                        val start = DateUtils.getDate("yyyy-MM-dd'T'HH:mm:ss'Z'", item.optString("start", ""))
                        val end = DateUtils.getDate("yyyy-MM-dd'T'HH:mm:ss'Z'", item.optString("end", ""))
                        val chargingTimeMinutes = item.optInt("suspendedCleaningChargingTime", 0)//minutes
                        val errorTime = item.optInt("errorTime", 0)//seconds
                        val pauseTime = item.optInt("pauseTime", 0)//seconds
                        var cleaningTime = DateUtils.getDiffInSeconds(start, end) - errorTime - pauseTime - chargingTimeMinutes * 60
                        if (cleaningTime < 0) cleaningTime = 0//to avoid negative values
                        val mode = CleaningMode.fromValue(item.optInt("mode", 0))

                        val day = CleaningDayItem(end, cleaningTime.toDouble(), chargingTimeMinutes, errorTime, pauseTime, mode)

                        if (DateUtils.isAfterEpochTime(day.date!!)) {
                            cleanings.add(day)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception", e)
        }

        return cleanings
    }

    fun parseCleaningAreasStats(json: JSONObject?): ArrayList<CleaningDayItem> {
        val cleanings = ArrayList<CleaningDayItem>()
        try {
            val data = json?.getJSONObject("data")

            if (data != null && data.has("houseCleaning")) {
                val houseCleaning = data.getJSONObject("houseCleaning")
                if (houseCleaning.has("history")) {
                    val history = houseCleaning.getJSONArray("history")
                    for (i in 0 until history.length()) {
                        val item = history.getJSONObject(i)

                        val end = DateUtils.getDate("yyyy-MM-dd'T'HH:mm:ss'Z'", item.optString("end", ""))
                        val area = item.optDouble("area", 0.0)
                        val mode = CleaningMode.fromValue(item.optInt("mode", 0))

                        val day = CleaningDayItem(end, area, 0, 0, 0, mode)

                        if (DateUtils.isAfterEpochTime(day.date!!)) {
                            cleanings.add(day)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception", e)
        }

        return cleanings
    }

    fun parseTotalCleanedArea(json: JSONObject?): Double {
        var area = 0.0
        try {
            val data = json?.getJSONObject("data")

            if (data != null && data.has("houseCleaning")) {
                val houseCleaning = data.getJSONObject("houseCleaning")
                if (houseCleaning.has("totalCleanedArea")) {
                    area = houseCleaning.optDouble("totalCleanedArea", 0.0)
                }
            }
            if (data != null && data.has("spotCleaning")) {
                val spotCleaning = data.getJSONObject("spotCleaning")
                if (spotCleaning.has("totalCleanedArea")) {
                    area += spotCleaning.optDouble("totalCleanedArea", 0.0)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception", e)
        }

        return area
    }

    fun parseTotalCleanedTime(json: JSONObject?): Int {
        var time = 0
        try {
            val data = json?.getJSONObject("data")

            if (data != null && data.has("houseCleaning")) {
                val houseCleaning = data.getJSONObject("houseCleaning")
                if (houseCleaning.has("totalCleaningTime")) {
                    time = houseCleaning.optInt("totalCleaningTime", 0)
                }
            }
            if (data != null && data.has("spotCleaning")) {
                val spotCleaning = data.getJSONObject("spotCleaning")
                if (spotCleaning.has("totalCleaningTime")) {
                    time += spotCleaning.optInt("totalCleaningTime", 0)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception", e)
        }

        return time
    }

    fun getResultErrorCode(json: JSONObject?): String? {
        return json?.optString("result", null)
    }

    fun parsePreferences(json: JSONObject?): RobotPreferences {
        val model = RobotPreferences()
        // TODO
        return model
    }

    fun parseGeneralInfo(json: JSONObject?): GeneralInfo {
        val out = GeneralInfo()
        if(json == null) return out
        val data = json.getJSONObject("data")
        if(data.has("model")) {
            out.model = data.getString("model")
        }
        if(data.has("serial")) {
            out.serial = data.getString("serial")
        }
        if(data.has("language")) {
            out.language = data.getString("language")
        }
        if(data.has("firmware")) {
            out.firmware = data.getString("firmware")
        }

        if (data.has("battery")) {
            val battery = data.getJSONObject("battery")

            if(battery.has("level")) {
                out.batteryLevel = battery.optInt("level", 0)
            }
            if(battery.has("totalCharges")) {
                out.totalCharges = battery.optInt("totalCharges", 0)
            }
            if (battery.has("authorizationStatus")) {
                out.authorizationStatus = battery.optInt("authorizationStatus", 0)
            }
            if (battery.has("timeToEmpty")) {
                out.timeToEmpty = battery.optInt("timeToEmpty", 0)
            }
            if (battery.has("timeToFullCharge")) {
                out.timeToFullCharge = battery.optInt("timeToFullCharge", 0)
            }
            if (battery.has("vendor")) {
                out.vendor = battery.getString("vendor")
            }
            if (battery.has("manufacturingDate")) {
                out.manufacturingDate = battery.getString("manufacturingDate")
            }
        }
        return out
    }

    fun parseLocalStats(data: JSONObject?): LocalStats {
        val stats = LocalStats()
        stats.cleaningDays = NucleoJSONParser.parseCleaningTimesStats(data)
        stats.cleaningDays?.reverse()
        //compute the avarage of the 21 cleaning+charging times
        if (stats.cleaningDays != null && stats.cleaningDays!!.size > 0) {
            var sum = 0.0
            var validItemCount = 0
            for (i in 0 until stats.cleaningDays!!.size) {
                if (DateUtils.isAfterEpochTime(stats.cleaningDays!![i].date!!)) {
                    validItemCount++
                    sum += stats.cleaningDays!![i].value
                }
            }
            if (validItemCount == 0) validItemCount = 1
            val avarage = Math.round(sum / validItemCount).toInt()
            stats.avarageCleaningPlusChargingTime = avarage
        }

        stats.cleaningAreas = NucleoJSONParser.parseCleaningAreasStats(data)
        stats.cleaningAreas?.reverse()
        //compute the avarage of the 21 cleaned areas
        if (stats.cleaningAreas != null && stats.cleaningAreas!!.size > 0) {
            var sum = 0.0
            var validItemCount = 0
            for (i in 0 until stats.cleaningAreas!!.size) {
                if (DateUtils.isAfterEpochTime(stats.cleaningAreas!![i].date!!)) {
                    validItemCount++
                    sum += stats.cleaningAreas!![i].value
                }
            }
            if (validItemCount == 0) validItemCount = 1
            val avarage = sum / validItemCount
            stats.avarageCleaningArea = avarage
        }

        stats.totalCleanedArea = NucleoJSONParser.parseTotalCleanedArea(data)
        stats.totalCleanedTime = NucleoJSONParser.parseTotalCleanedTime(data)
        return stats
    }

    fun parseManualDriveInfo(json: JSONObject?): ManualDriveInfo {

        var ip = ""
        var port = 0
        var ssid = ""

        var data = json?.getJSONObject("data")
        if(data?.has("ip_address") == true) {
            ip = data.getString("ip_address")
        }
        if(data?.has("port") == true) {
            port = data.optInt("port", 0)
        }
        if(data?.has("ssid") == true) {
            ssid = data.getString("ssid")
        }

        return ManualDriveInfo(ip, port, ssid)
    }

    fun parseWifiNetworks(json: JSONObject?): List<WifiNetwork> {
        val wifiList = mutableListOf<WifiNetwork>()
        try {
            val networks = json?.getJSONArray("data")
            for (i in 0 until networks!!.length()) {
                val net = networks.getJSONObject(i)

                val ssid = if (net.isNull("ssid")) "" else net.optString("ssid", "")

                val wifiNetwork: WifiNetwork

                if (!net.optBoolean("connected", false)) {//if it is not the connected network
                    wifiNetwork = WifiNetwork(ssid, net.optInt("strength", 0),
                            net.optBoolean("connected", false))
                } else {
                    wifiNetwork = WifiNetwork(ssid, net.optInt("strength", 0),
                            net.optBoolean("connected", false),
                            net.optString("ipAddress", ""),
                            net.optString("subnetMask", ""),
                            net.optString("routerIpAddress", ""),
                            net.optString("macAddress", ""))
                }

                wifiList.add(wifiNetwork)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception", e)
        }

        return wifiList
    }

    fun parseAvailableWifiNetworks(json: JSONObject?): List<WifiNetwork> {
        val wifiList = mutableListOf<WifiNetwork>()
        try {
            val networks = json?.getJSONArray("data")

            for (i in 0 until (networks?.length()?:0)) {
                val net = networks?.getJSONObject(i)!!

                val ssid = if (net.isNull("ssid")) "" else net.optString("ssid", "")
                if (!ssid.isNullOrEmpty()) {
                    val strength = net.optInt("strength", 0)
                    val wifiNetwork = WifiNetwork(ssid, strength, false)
                    wifiList.add(wifiNetwork)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception", e)
        }
        return wifiList
    }

    fun parseSchedule(json: JSONObject?): RobotSchedule {
        lateinit var schedule: RobotSchedule
        if(json != null) {
            if (json.has("data")) {
                var enabled = false
                val events = arrayListOf<ScheduleEvent>()
                val data = json.getJSONObject("data")
                if (data.has("events")) {
                    enabled = data.optBoolean("enabled", false)
                    val eventsData = data.getJSONArray("events")
                    for (e in 0 until eventsData.length()) {
                        val se = parseScheduleEvent(eventsData.get(e) as JSONObject)
                        events.add(se)
                    }
                }
                schedule = RobotSchedule(enabled, events)
            }
        }
        return schedule
    }

    fun parseScheduleEvent(json: JSONObject): ScheduleEvent {
        val se = ScheduleEvent().apply {
            if (json.has("startTime") && !json.isNull("startTime")) {
                startTime = json.getString("startTime")
            }
            if (json.has("mode") && !json.isNull("mode")) {
                mode = CleaningMode.fromValue(json.getInt("mode"))
            } else
                mode = CleaningMode.INVALID
            if (json.has("day") && !json.isNull("day")) {
                day = json.getInt("day")
            }
            // single zone schedule
            if (json.has("boundary")) {
                boundaryIds = mutableListOf()
                boundaryNames = mutableListOf()
                val boundaryJSON = json.getJSONObject("boundary")
                if (boundaryJSON.has("id") && !boundaryJSON.isNull("id")) {
                    boundaryIds!!.add(boundaryJSON.getString("id"))
                }
                if (boundaryJSON.has("name") && !boundaryJSON.isNull("name")) {
                    boundaryNames!!.add(boundaryJSON.getString("name"))
                }
            }
            // multiple zones schedule
            if(json.has("boundaries")) {
                boundaryIds = mutableListOf()
                boundaryNames = mutableListOf()
                val barray = json.getJSONArray("boundaries")
                for(i in 0 until barray.length()) {
                    boundaryIds!!.add(barray.getJSONObject(i).getString("id"))
                    boundaryNames!!.add(barray.getJSONObject(i).getString("name"))
                }
            }
            if (json.has("mapId") && !json.isNull("mapId")) {
                mapId = json.getString("mapId")
            }
        }
        return se
    }
}
