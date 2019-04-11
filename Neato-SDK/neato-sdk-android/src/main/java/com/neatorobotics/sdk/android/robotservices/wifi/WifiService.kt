/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.wifi

import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.models.Robot
import com.neatorobotics.sdk.android.models.WifiNetwork
import com.neatorobotics.sdk.android.models.hasService
import com.neatorobotics.sdk.android.robotservices.RobotService
import com.neatorobotics.sdk.android.robotservices.RobotServices

val Robot.wifiService: WifiService?
    get() {
        if (this.state == null) return null
        return if (this.hasService(RobotServices.SERVICE_WIFI)) {
            WifiServiceFactory.get(this.state!!.availableServices[RobotServices.SERVICE_WIFI]!!)
        } else
            null
    }

abstract class WifiService: RobotService() {

    abstract suspend fun getWifiNetworks(robot: Robot): Resource<List<WifiNetwork>>
    abstract suspend fun setWifiNetwork(robot: Robot, ssid: String): Resource<Boolean>
    abstract suspend fun addWifiNetwork(robot: Robot, ssid: String, password: String): Resource<Boolean>
    abstract suspend fun removeWifiNetwork(robot: Robot, ssid: String): Resource<Boolean>
    abstract suspend fun getAvailableWifiNetworks(robot: Robot): Resource<List<WifiNetwork>>
    abstract suspend fun resetWifiNetworks(robot: Robot): Resource<Boolean>

    companion object {
        private const val TAG = "WifiService"
    }
}
