/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.models

import android.util.Log
import com.neatorobotics.sdk.android.clients.nucleo.NucleoRepository
import com.neatorobotics.sdk.android.robotservices.RobotServices
import com.neatorobotics.sdk.android.utils.RobotVersionComparator
import org.json.JSONException
import org.json.JSONObject

private const val TAG = "RobotExtensions"

val Robot.latestAvailableFirmware: RobotFirmware?
    get() {
        if (state != null && state!!.robotModelName != null && state!!.firmware != null) {
            val model = state!!.robotModelName?.toLowerCase()
            if (recentFirmware.containsKey(model)) {
                return recentFirmware[model]
            }
        }
        return null
    }

// If OTA service is available OR
// manual update url is available
// manual update url available OR OTA
val Robot.isUpdateAvailable: Boolean
    get() {
        var manualUpdateInfoUrl: String? = null
        if (this.latestAvailableFirmware != null) {
            manualUpdateInfoUrl = this.latestAvailableFirmware!!.manualUpdateInfoUrl
        }
        if (!manualUpdateInfoUrl.isNullOrEmpty() || state != null &&
                state!!.robotModelName != null &&
                state!!.firmware != null &&
                this.hasService(RobotServices.SERVICE_SOFTWARE_UPDATE)) {
            val model = state!!.robotModelName?.toLowerCase()
            if (recentFirmware.containsKey(model)) {
                try {
                    if (RobotVersionComparator.versionCompare(state!!.firmware!!, recentFirmware[model]!!.version!!) < 0) {
                        return true
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    Log.e(TAG, "Exception", e)
                }

            }
        }
        return false
    }

fun Robot.hasTrait(traitName: String): Boolean {
    return traits.contains(traitName)
}

fun Robot.hasService(serviceName: String): Boolean {
    return this.state != null && this.state!!.availableServices.containsKey(serviceName)
}

fun Robot.hasService(serviceName: String, vararg serviceVersions: String): Boolean {
    if (this.state == null) return false
    if (this.state!!.availableServices.containsKey(serviceName)) {
        val version = this.state!!.availableServices[serviceName]

        if (serviceVersions.isNotEmpty()) {
            for (s in serviceVersions) {
                if (version!!.equals(s, ignoreCase = true)) return true
            }
        } else
            return false
    } else
        return false
    return false
}

fun Robot.setRecentFirmwares(jsonFirmwares: JSONObject?) {
    if (jsonFirmwares != null) {
        val modelIterator = jsonFirmwares.keys()
        while (modelIterator.hasNext()) {
            try {
                val model = modelIterator.next()
                val version = jsonFirmwares.getJSONObject(model).optString("version", "")
                val url = jsonFirmwares.getJSONObject(model).optString("url", "")
                val manualUpdateInfoUrl = jsonFirmwares.getJSONObject(model).optString("manual_update_info_url", "")
                //String changelogurl = jsonFirmwares.getJSONObject(model).optString("changelog_url", "");
                val size = jsonFirmwares.getJSONObject(model).optInt("filesize", 0)
                var minRequiredVersion = jsonFirmwares.getJSONObject(model).optString("min_required_version", "0.0.0")
                if (minRequiredVersion.isNullOrEmpty()) {
                    minRequiredVersion = "0.0.0"
                }
                recentFirmware[model.toLowerCase()] = RobotFirmware(model, version, url, manualUpdateInfoUrl, size, minRequiredVersion)
            } catch (e: JSONException) {
                Log.e(TAG, "Exception", e)
            }

        }
    }
}

fun Robot.loadFromJSON(json: JSONObject?) {
    if (json != null) {
        this.serial = json.optString("serial")
        this.prefix = json.optString("prefix")
        this.name = json.optString("name")
        this.model = json.optString("model")
        this.model_readable = json.optString("model_readable")
        this.firmware = json.optString("firmware")
        this.tz_info = json.optString("tz_info")
        this.created_at = json.optString("created_at")
        this.provisioned_at = json.optString("provisioned_at")
        this.lat = json.optDouble("lat", -1.0)
        this.lon = json.optDouble("lon", -1.0)
        this.linkedAt = json.optString("linked_at")
        this.secret_key = json.optString("secret_key")
        this.nucleoUrl = json.optString("nucleo_url")
        this.mac = json.optString("mac_address")
        this.explorationId = json.optString("latest_exploration_map_id")
        if (json.has("traits")) {
            try {
                val traitsArray = json.getJSONArray("traits")
                for (i in 0 until traitsArray.length()) {
                    traits.add(traitsArray.getString(i))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
        if (json.has("persistent_maps")) {
            try {
                val persistentMapsIdArray = json.getJSONArray("persistent_maps")
                for (i in 0 until persistentMapsIdArray.length()) {
                    persistentMapsIds.add(persistentMapsIdArray.getJSONObject(i).getString("id"))
                    var pmName = persistentMapsIdArray.getJSONObject(i).getString("name")
                    if (pmName.isNullOrEmpty()) pmName = "My Home"
                    persistentMapsNames.add(pmName)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
    }
}

suspend fun Robot.updateRobotState() {
    val result = NucleoRepository().getRobotState(this)
    this.state = result.data
}