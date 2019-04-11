/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.wifi

import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.nucleo.Nucleo
import com.neatorobotics.sdk.android.clients.nucleo.json.NucleoJSONParser
import com.neatorobotics.sdk.android.models.Robot
import com.neatorobotics.sdk.android.models.WifiNetwork
import org.json.JSONObject


class WifiBasic1Service: WifiService() {
    override suspend fun setWifiNetwork(robot: Robot, ssid: String): Resource<Boolean> {
        val command = JSONObject(
            Nucleo.CONNECT_TO_NETWORKS_COMMAND
                .replace("SSID_PLACEHOLDER",ssid))
        val result = nucleoRepository.executeCommand(robot, command)
        return when(result.status) {
            Resource.Status.SUCCESS -> Resource.success(true)
            else -> Resource.error(result.code, result.message)
        }
    }

    override suspend fun addWifiNetwork(robot: Robot, ssid: String, password: String): Resource<Boolean> {
        val command = JSONObject(
            Nucleo.ADD_NETWORKS_COMMAND
                .replace("SSID_PLACEHOLDER",ssid)
                .replace("PWD_PLACEHOLDER",password))
        val result = nucleoRepository.executeCommand(robot, command)
        return when(result.status) {
            Resource.Status.SUCCESS -> Resource.success(true)
            else -> Resource.error(result.code, result.message)
        }
    }

    override suspend fun removeWifiNetwork(robot: Robot, ssid: String): Resource<Boolean> {
        val command = JSONObject(
            Nucleo.REMOVE_WIFI_NETWORK_COMMAND
                .replace("SSID_PLACEHOLDER",ssid))
        val result = nucleoRepository.executeCommand(robot, command)
        return when(result.status) {
            Resource.Status.SUCCESS -> Resource.success(true)
            else -> Resource.error(result.code, result.message)
        }
    }

    override suspend fun getAvailableWifiNetworks(robot: Robot): Resource<List<WifiNetwork>> {
        val result = nucleoRepository.executeCommand(robot, JSONObject(Nucleo.GET_AVAILABLE_WIFI_NETWORKS_COMMAND))
        return when(result.status) {
            Resource.Status.SUCCESS -> Resource.success(NucleoJSONParser.parseAvailableWifiNetworks(result.data))
            else -> Resource.error(result.code, result.message)
        }
    }

    override suspend fun resetWifiNetworks(robot: Robot): Resource<Boolean> {
        val result = nucleoRepository.executeCommand(robot, JSONObject(Nucleo.RESET_WIFI_NETWORKS_COMMAND))
        return when(result.status) {
            Resource.Status.SUCCESS -> Resource.success(true)
            else -> Resource.error(result.code, result.message)
        }
    }

    override suspend fun getWifiNetworks(robot: Robot): Resource<List<WifiNetwork>> {
        val result = nucleoRepository.executeCommand(robot, JSONObject(Nucleo.GET_WIFI_NETWORKS_COMMAND))
        return when(result.status) {
            Resource.Status.SUCCESS -> Resource.success(NucleoJSONParser.parseWifiNetworks(result.data))
            else -> Resource.error(result.code, result.message)
        }
    }
}