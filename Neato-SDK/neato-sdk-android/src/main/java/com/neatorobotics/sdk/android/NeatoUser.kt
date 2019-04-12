package com.neatorobotics.sdk.android

import android.content.Context
import androidx.annotation.VisibleForTesting

import com.neatorobotics.sdk.android.authentication.AccessTokenDatasource
import com.neatorobotics.sdk.android.authentication.NeatoAuthentication
import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.beehive.Beehive
import com.neatorobotics.sdk.android.clients.beehive.BeehiveRepository
import com.neatorobotics.sdk.android.models.*

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */
class NeatoUser

/**
 * Private singleton constructor
 * @param context
 */
private constructor(private val context: Context) {

    @VisibleForTesting
    var neatoAuthentication: NeatoAuthentication = NeatoAuthentication.getInstance(context)
    var beehiveRepository: BeehiveRepository = BeehiveRepository(Beehive.URL)

    /**
     * Revoke the current access token
     * @param callback
     */
    suspend fun logout(): Resource<Boolean> {
        return beehiveRepository.logOut()
    }

    /**
     * Retrieve the user robots list.
     * @param callback
     */
    suspend fun loadRobots(): Resource<List<Robot>> {
        return beehiveRepository.loadRobots()
    }

    /**
     * Get information about the user currently authenticated.
     * @param callback
     */
    suspend fun getUserInfo(): Resource<UserInfo> {
        return beehiveRepository.getUser()
    }

    /**
     * Get robot available maps/stats.
     * @param callback
     */
    suspend fun getMaps(robot: Robot): Resource<List<CleaningMap>> {
        return beehiveRepository.getRobotMaps(robot)
    }

    /**
     * Get robot available maps/stats.
     * @param callback
     */
    suspend fun getMap(robot: Robot, mapId: String): Resource<CleaningMap> {
        return beehiveRepository.getMapById(robot.serial?:"", mapId)
    }

    /**
     * Get robot available persistent maps.
     * @param callback
     */
    suspend fun getPersistentMaps(robot: Robot): Resource<List<PersistentMap>> {
        return beehiveRepository.getPersistentMaps(robot)
    }

    /**
     * Delete a persistent maps.
     * @param callback
     */
    suspend fun deletePersistentMap(robot: Robot, mapId: String): Resource<Unit> {
        return beehiveRepository.deletetMap(robot, mapId)
    }

    companion object {
        private var instance: NeatoUser? = null

        /**
         * Use this method to get the singleton neato user.
         * @param context
         * @return
         */
        fun getInstance(context: Context): NeatoUser {
            if (instance == null) {
                instance = NeatoUser(context)
            }
            return instance!!
        }

        /**
         * Use this method to get the singleton neato user that use a custom access token datasource.
         * @param context
         * @param accessTokenDatasource
         * @return
         */
        fun getInstance(context: Context, accessTokenDatasource: AccessTokenDatasource): NeatoUser {
            val client = getInstance(context)
            client.neatoAuthentication = NeatoAuthentication.getInstance(context, accessTokenDatasource)
            return client
        }
    }
}
