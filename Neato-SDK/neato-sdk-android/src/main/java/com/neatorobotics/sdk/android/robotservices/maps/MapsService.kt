/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.maps

import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.models.*
import com.neatorobotics.sdk.android.robotservices.RobotService
import com.neatorobotics.sdk.android.robotservices.RobotServices

val Robot.mapService: MapsService?
    get() {
        if (this.state == null) return null
        return if (this.hasService(RobotServices.SERVICE_MAPS)) {
            MapsServiceFactory.get(this.state!!.availableServices[RobotServices.SERVICE_MAPS]!!)
        } else
            null
    }

abstract class MapsService : RobotService() {

    abstract val isFloorPlanSupported: Boolean

    abstract suspend fun startPersistentMapExploration(robot: Robot): Resource<Boolean>

    abstract suspend fun getPersistentMaps(robot: Robot): Resource<List<PersistentMap>>

    abstract suspend fun getMapBoundaries(robot: Robot, mapId: String): Resource<List<Boundary>>

    abstract suspend fun deletePersistentMap(robot: Robot, mapId: String): Resource<Unit>

    abstract suspend fun setMapBoundaries(robot: Robot, mapId: String, boundaries: List<Boundary>?): Resource<List<Boundary>>

    abstract fun areZonesSupported(): Boolean

    abstract fun areMultipleFloorPlanSupported(): Boolean

    suspend fun getCleaningMap(robot: Robot, mapId: String): Resource<CleaningMap> {
        return beehiveRepository.getMapById(robot.serial?:"", mapId)
    }

    suspend fun getCleaningMaps(robot: Robot): Resource<List<CleaningMap>> {
        return beehiveRepository.getRobotMaps(robot)
    }

    companion object {

        private const val TAG = "MapsService"
    }
}