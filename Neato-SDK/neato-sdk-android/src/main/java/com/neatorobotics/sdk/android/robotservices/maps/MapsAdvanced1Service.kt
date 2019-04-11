/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.maps

import android.graphics.PointF
import android.util.Log
import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.models.Boundary
import com.neatorobotics.sdk.android.models.PersistentMap
import com.neatorobotics.sdk.android.models.Robot

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.ArrayList

open class MapsAdvanced1Service : MapsService() {

    override val isFloorPlanSupported: Boolean
        get() = true

    override suspend fun startPersistentMapExploration(robot: Robot): Resource<Boolean> {
        val command = JSONObject()
        try {
            command.put("reqId", "77")
            command.put("cmd", "startPersistentMapExploration")
        } catch (e: JSONException) {
            Log.e(TAG, "Exception", e)
        }

        val result = nucleoRepository.executeCommand(robot, command)
        return when(result.status) {
            Resource.Status.SUCCESS -> Resource.success(true)
            else -> Resource.error(result.code, result.message)
        }
    }

    override suspend fun getPersistentMaps(robot: Robot): Resource<List<PersistentMap>> {
        return beehiveRepository.getPersistentMaps(robot)
    }

    override suspend fun getMapBoundaries(robot: Robot, mapId: String): Resource<List<Boundary>> {
        val command = JSONObject()
        try {
            command.put("reqId", "77")
            command.put("cmd", "getMapBoundaries")
            val commandParams = JSONObject()
            commandParams.put("mapId", mapId)
            command.put("params", commandParams)
        } catch (e: JSONException) {
            Log.e(TAG, "Exception", e)
        }

        val result = nucleoRepository.executeCommand(robot, command)
        return when(result.status) {
            Resource.Status.SUCCESS -> {
                val list = ArrayList<Boundary>()
                try {
                    val json = result.data

                    if (json != null) {
                        val array = json.getJSONObject("data").getJSONArray("boundaries")
                        for (i in 0 until array.length()) {
                            val b = array.getJSONObject(i)
                            val boundary = Boundary()
                            boundary.id = i.toString() + ""//b.optInt("id", i)
                            boundary.color = "#E54B1C"//b.optString("color", "#E54B1C")
                            boundary.isEnabled = b.optBoolean("enabled", true)
                            boundary.name = b.optString("name", "")
                            boundary.type = b.optString("type", "polyline")

                            val vertices = ArrayList<PointF>()
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
                Resource.success(list)
            }
            else -> Resource.error(result.code, result.message)
        }
    }

    override suspend fun deletePersistentMap(robot: Robot, mapId: String): Resource<Unit> {
        return beehiveRepository.deletetMap(robot, mapId)
    }

    override suspend fun setMapBoundaries(robot: Robot, mapId: String, boundaries: List<Boundary>?): Resource<List<Boundary>> {
        val otherSymbols = DecimalFormatSymbols()
        otherSymbols.decimalSeparator = '.'
        otherSymbols.groupingSeparator = ','
        val formatter = DecimalFormat("#0.0000", otherSymbols)

        val command = JSONObject()
        try {
            command.put("reqId", "77")
            command.put("cmd", "setMapBoundaries")
            val commandParams = JSONObject()
            commandParams.put("mapId", mapId)
            val boundariesParam = JSONArray()
            if (boundaries != null) {
                for (b in boundaries) {
                    val boundaryPar = JSONObject()
                    boundaryPar.put("type", b.type)
                    val verticesPar = JSONArray()
                    for (p in b.vertices!!) {
                        val verticesStr = StringBuffer("[")
                        verticesStr.append(formatter.format(p.x.toDouble()))
                        verticesStr.append(", ")
                        verticesStr.append(formatter.format(p.y.toDouble()))
                        verticesStr.append("]")

                        val json = "{\"data\":$verticesStr}"  //this is trick.
                        val obj = JSONObject(json)
                        val arr = obj.getJSONArray("data")

                        verticesPar.put(arr)
                    }
                    boundaryPar.put("vertices", verticesPar)
                    boundaryPar.put("name", b.name)
                    boundaryPar.put("color", b.color)
                    boundaryPar.put("enabled", b.isEnabled)

                    boundariesParam.put(boundaryPar)
                }
            }
            commandParams.put("boundaries", boundariesParam)
            command.put("params", commandParams)
        } catch (e: JSONException) {
            Log.e(TAG, "Exception", e)
        }

        val result = nucleoRepository.executeCommand(robot, command)
        return when(result.status) {
            Resource.Status.SUCCESS -> Resource.success(ArrayList())
            else -> Resource.error(result.code, result.message)
        }
    }

    override fun areZonesSupported(): Boolean {
        return false
    }

    override fun areMultipleFloorPlanSupported(): Boolean {
        return false
    }

    companion object {

        private const val TAG = "MapsAdvanced1Service"
    }
}