/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android

import com.neatorobotics.sdk.android.authentication.NeatoAuthentication
import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.beehive.Beehive
import com.neatorobotics.sdk.android.clients.beehive.BeehiveRepository
import com.neatorobotics.sdk.android.models.*

object NeatoUser {

    var neatoAuthentication: NeatoAuthentication = NeatoAuthentication
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
}
