/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.maps

import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.ResourceState
import com.neatorobotics.sdk.android.models.Boundary
import com.neatorobotics.sdk.android.models.PersistentMap
import com.neatorobotics.sdk.android.models.Robot


class MapsBasic1Service : MapsService() {
    override suspend fun startPersistentMapExploration(robot: Robot): Resource<Boolean> {
        return Resource.error(ResourceState.COMMAND_NOT_FOUND, "")
    }

    override suspend fun getPersistentMaps(robot: Robot): Resource<List<PersistentMap>> {
        return Resource.error(ResourceState.COMMAND_NOT_FOUND, "")
    }

    override suspend fun getMapBoundaries(robot: Robot, mapId: String): Resource<List<Boundary>> {
        return Resource.error(ResourceState.COMMAND_NOT_FOUND, "")
    }

    override suspend fun deletePersistentMap(robot: Robot, mapId: String): Resource<Unit> {
        return Resource.error(ResourceState.COMMAND_NOT_FOUND, "")
    }

    override suspend fun setMapBoundaries(robot: Robot, mapId: String, boundaries: List<Boundary>?): Resource<List<Boundary>> {
        return Resource.error(ResourceState.COMMAND_NOT_FOUND, "")
    }


    override val isFloorPlanSupported: Boolean
        get() = false


    override fun areZonesSupported(): Boolean {
        return false
    }

    override fun areMultipleFloorPlanSupported(): Boolean {
        return false
    }

    companion object {

        private val TAG = "MapsBasic1Service"
    }
}