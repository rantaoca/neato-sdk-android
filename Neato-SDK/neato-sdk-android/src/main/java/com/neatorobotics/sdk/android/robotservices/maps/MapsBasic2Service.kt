/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.maps

import android.util.Log
import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.nucleo.json.NucleoJSONParser
import com.neatorobotics.sdk.android.models.Boundary
import com.neatorobotics.sdk.android.models.Robot

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class MapsBasic2Service : MapsAdvanced1Service() {

    override val isFloorPlanSupported: Boolean
        get() = true

    override fun areZonesSupported(): Boolean {
        return true
    }

    override fun areMultipleFloorPlanSupported(): Boolean {
        return true
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
            Resource.Status.SUCCESS -> Resource.success(NucleoJSONParser.parseBoundaries(result.data))
            else -> Resource.error(result.code, result.message)
        }
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
                    boundaryPar.put("id", b.id)
                    boundaryPar.put("type", b.type)
                    val verticesPar = JSONArray()
                    for (p in b.vertices!!) {
                        val verticesStr = StringBuffer("[")
                        verticesStr.append(formatter.format(p.x.toDouble()))
                        verticesStr.append(", ")
                        verticesStr.append(formatter.format(p.y.toDouble()))
                        verticesStr.append("]")

                        val json = "{\"data\":$verticesStr}"  //IMPORTANT: this is the trick to avoid JSON to place double quotes around numbers and keeping the 4 digits precision.
                        val obj = JSONObject(json)
                        val arr = obj.getJSONArray("data")

                        verticesPar.put(arr)
                    }
                    boundaryPar.put("vertices", verticesPar)
                    if (b.type.equals("polygon", ignoreCase = true)) {
                        if (b.relevancy != null) {
                            val relevancyStr = StringBuffer("[")
                            relevancyStr.append(formatter.format(b.relevancy!!.x.toDouble()))
                            relevancyStr.append(", ")
                            relevancyStr.append(formatter.format(b.relevancy!!.y.toDouble()))
                            relevancyStr.append("]")

                            val json = "{\"data\":$relevancyStr}"  //IMPORTANT: this is the trick to avoid JSON to place double quotes around numbers and keeping the 4 digits precision.
                            val obj = JSONObject(json)
                            val arr = obj.getJSONArray("data")

                            boundaryPar.put("relevancy", arr)
                        } else
                            boundaryPar.put("relevancy", null) // maybe we want to discard empty areas?
                    }
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
            Resource.Status.SUCCESS -> Resource.success(NucleoJSONParser.parseBoundaries(result.data))
            else -> Resource.error(result.code, result.message)
        }
    }

    companion object {

        private val TAG = "MapsBasic2Service"
    }
}