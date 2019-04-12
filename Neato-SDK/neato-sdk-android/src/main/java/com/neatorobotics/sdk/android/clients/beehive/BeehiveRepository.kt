/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.clients.beehive

import com.neatorobotics.sdk.android.NeatoSDK
import com.neatorobotics.sdk.android.authentication.NeatoAuthentication
import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.ResourceState
import com.neatorobotics.sdk.android.clients.beehive.json.BeehiveJSONParser
import com.neatorobotics.sdk.android.models.*
import com.neatorobotics.sdk.android.utils.DELETE
import com.neatorobotics.sdk.android.utils.GET
import com.neatorobotics.sdk.android.utils.POST
import com.neatorobotics.sdk.android.utils.PUT
import org.json.JSONObject

class BeehiveRepository(val endpoint: String,
                        private val errorsProvider: BeehiveErrorsProvider = BeehiveErrorsProvider()) {

    var client = BeehiveHttpClient()

    suspend fun logOut(): Resource<Boolean> {
        val params = JSONObject("{\"token\":\"" + NeatoAuthentication.oauth2AccessToken + "\"}")
        val result = client.call(POST, "$endpoint/oauth2/revoke", params)
        return when (result.status) {
            Resource.Status.SUCCESS  -> Resource.success(true)
            else -> Resource.error(result.code, getDescriptiveErrorMessage(result.code), false)
        }
    }

    suspend fun getUser(): Resource<UserInfo> {
        val result = client.call(GET, "$endpoint/users/me")
        return when (result.status) {
            Resource.Status.SUCCESS  -> Resource.success(BeehiveJSONParser.parseUser(result.data))
            else -> Resource.error(result.code, getDescriptiveErrorMessage(result.code))
        }
    }

    suspend fun updateUser(params: JSONObject): Resource<Boolean> {
        val response = client.call(PUT, "$endpoint/users/me", params)
        return when (response.status) {
            Resource.Status.SUCCESS  -> Resource.success(true)
            else -> Resource.error(response.code, getDescriptiveErrorMessage(response.code))
        }
    }

    suspend fun updateRobot(robot: Robot, params: JSONObject): Resource<Boolean> {
        val response = client.call(PUT, "$endpoint/robots/${robot.serial}", params)
        return when (response.status) {
            Resource.Status.SUCCESS  -> Resource.success(true)
            else -> Resource.error(response.code, getDescriptiveErrorMessage(response.code))
        }
    }

    suspend fun loadRobots(): Resource<List<Robot>> {
        val result = client.call(GET, "$endpoint/users/me/robots")
        return if (result.status === Resource.Status.SUCCESS) {
            val output = BeehiveJSONParser.parseRobotList(result.data)
            Resource.success(output)
        } else {
            Resource.error(result.code, getDescriptiveErrorMessage(result.code))
        }
    }

    suspend fun delete(robot: Robot): Resource<Boolean> {
        val response = client.call(DELETE, "$endpoint/robots/${robot.serial}")
        return when (response.status) {
            Resource.Status.SUCCESS  -> Resource.success(true)
            else -> Resource.error(response.code, getDescriptiveErrorMessage(response.code))
        }
    }

    suspend fun getMapById(serial: String, mapId: String): Resource<CleaningMap> {
        val result = client.call(GET, "$endpoint/users/me/robots/$serial/maps/$mapId")

        return when (result.status) {
            Resource.Status.SUCCESS  -> Resource.success(BeehiveJSONParser.parseRobotMap(result.data!!))
            else -> Resource.error(result.code, getDescriptiveErrorMessage(result.code))
        }
    }

    suspend fun getExplorationMaps(robot: Robot): Resource<List<PersistentMap>> {
        val result = client.call(GET, "$endpoint/users/me/robots/${robot.serial}/exploration_maps")

        return when (result.status) {
            Resource.Status.SUCCESS  -> Resource.success(BeehiveJSONParser.parseRobotExplorationMaps(result.data!!))
            else -> Resource.error(result.code, getDescriptiveErrorMessage(result.code))
        }
    }

    suspend fun acceptExplorationMap(robot: Robot, mapId: String, mapName: String): Resource<Boolean> {
        val input = JSONObject()
        input.put("name", mapName)

        val response = client.call(PUT, "$endpoint/users/me/robots/${robot.serial}/exploration_maps/$mapId/accept", input)

        return when (response.status) {
            Resource.Status.SUCCESS  -> Resource.success(true)
            else -> Resource.error(response.code, getDescriptiveErrorMessage(response.code))
        }
    }

    suspend fun rejectExplorationMap(robot: Robot?, mapId: String): Resource<Boolean> {

        val result = client.call(PUT, "$endpoint/users/me/robots/${robot?.serial}/exploration_maps/$mapId/reject")
        return when (result.status) {
            Resource.Status.SUCCESS  -> Resource.success(true)
            else -> Resource.error(result.code, getDescriptiveErrorMessage(result.code))
        }
    }

    suspend fun getPersistentMaps(robot: Robot): Resource<List<PersistentMap>> {

        val result = client.call(GET, "$endpoint/users/me/robots/${robot.serial}/persistent_maps")
        return when (result.status) {
            Resource.Status.SUCCESS  -> {
                var output: List<PersistentMap> = ArrayList()
                output = BeehiveJSONParser.parseRobotPersistentMaps(result.data)
                Resource.success(output)
            }
            else -> Resource.error(result.code, getDescriptiveErrorMessage(result.code))
        }
    }

    suspend fun deletetMap(robot: Robot, mapId: String): Resource<Unit>{

        val result = client.call(DELETE, "$endpoint/users/me/robots/${robot.serial}/persistent_maps/$mapId")
        return when (result.status) {
            Resource.Status.SUCCESS  -> Resource.success(Unit)
            else -> Resource.error(result.code, getDescriptiveErrorMessage(result.code))
        }
    }

    suspend fun changeUserLocale(locale: String): Resource<Boolean> {
        val input = JSONObject().apply {
            put("locale", locale)
        }
        val result = client.call(PUT, "$endpoint/users/me", input)
        return when (result.status) {
            Resource.Status.SUCCESS  -> Resource.success(true)
            else -> Resource.error(result.code, getDescriptiveErrorMessage(result.code))
        }
    }

    suspend fun changeUserCountry(country: String): Resource<Boolean> {
        val input = JSONObject().apply {
            put("country_code", country)
        }
        val result = client.call(PUT, "$endpoint/users/me", input)
        return when (result.status) {
            Resource.Status.SUCCESS  -> Resource.success(true)
            else -> Resource.error(result.code, getDescriptiveErrorMessage(result.code))
        }
    }

    suspend fun updatePersistentMap(robot: Robot?, mapId: String, params: JSONObject): Resource<Boolean>  {
        val result = client.call(PUT, "$endpoint/users/me/robots/${robot?.serial}/persistent_maps/$mapId", params)
        return when (result.status) {
            Resource.Status.SUCCESS  -> Resource.success(true)
            else -> Resource.error(result.code, getDescriptiveErrorMessage(result.code))
        }
    }

    suspend fun getRobot(serial: String): Resource<Robot?> {
        val result = client.call(GET, "$endpoint/robots/$serial", null)
        return when (result.status) {
            Resource.Status.SUCCESS  -> Resource.success(BeehiveJSONParser.parseRobot(result.data!!))
            else -> Resource.error(result.code, getDescriptiveErrorMessage(result.code))
        }
    }

    suspend fun getRobotMaps(robot: Robot?): Resource<List<CleaningMap>> {
        val path = "/users/me/robots/" + robot?.serial + "/maps"
        val result = client.call(GET, endpoint + path)

        return when (result.status) {
            Resource.Status.SUCCESS  -> Resource.success(BeehiveJSONParser.parseRobotMaps(result.data!!))
            else -> Resource.error(result.code, getDescriptiveErrorMessage(result.code))
        }
    }

    private fun getDescriptiveErrorMessage(code: ResourceState): String {
        return errorsProvider.description(code)
    }

    companion object {
        private val TAG = "BeehiveRepository"
    }
}
