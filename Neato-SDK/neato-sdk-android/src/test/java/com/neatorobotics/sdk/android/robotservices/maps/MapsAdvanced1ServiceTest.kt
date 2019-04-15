/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.maps

import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.ResourceState
import com.neatorobotics.sdk.android.clients.beehive.BeehiveRepository
import com.neatorobotics.sdk.android.clients.nucleo.NucleoRepository
import com.neatorobotics.sdk.android.models.Robot
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking

import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.mockito.Mockito

@RunWith(MockitoJUnitRunner::class)
class MapsAdvanced1ServiceTest {

    @Mock
    private lateinit var mockNucleoRepository: NucleoRepository
    @Mock
    private lateinit var mockBeehiveRepository: BeehiveRepository

    lateinit var service: MapsAdvanced1Service

    @Before
    fun setup() {
        service = MapsAdvanced1Service()
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this)
        service.nucleoRepository = mockNucleoRepository
        service.beehiveRepository = mockBeehiveRepository

        runBlocking {
            whenever(mockNucleoRepository.executeCommand(any(), any())).thenReturn(Resource.error(ResourceState.NOT_FOUND, ""))
        }
    }

    @Test
    fun testSupportFloorPlan() {
        assertTrue(service.isFloorPlanSupported)
    }

    @Test
    fun testSupportMultipleFloorPlans() {
        assertFalse(service.areMultipleFloorPlanSupported())
    }

    @Test
    fun testSupportZones() {
        assertFalse(service.areZonesSupported())
    }

    @Test
    fun testStartPersistentMapExplorationReqIdAsString() {

        runBlocking {
            service.startPersistentMapExploration(Robot("serial"))
            argumentCaptor<JSONObject>().apply {
                verify(mockNucleoRepository, Mockito.times(1)).executeCommand(any(), capture())
                assertNotNull(firstValue)
                assertTrue(firstValue.get("reqId") is String)
            }
        }
    }

    @Test
    fun testGetMapBoundariesReqIdAsString() {

        runBlocking {
            service.getMapBoundaries(Robot("serial"), "123")
            argumentCaptor<JSONObject>().apply {
                verify(mockNucleoRepository, Mockito.times(1)).executeCommand(any(), capture())
                assertNotNull(firstValue)
                assertTrue(firstValue.get("reqId") is String)
            }
        }
    }

    @Test
    fun testGetPersistentMaps() {

        runBlocking {
            service.getPersistentMaps(Robot("serial"))
            verify(mockBeehiveRepository, Mockito.times(1)).getPersistentMaps(any())
        }
    }

    @Test
    fun testDeletePersistentMaps() {
        runBlocking {
            service.deletePersistentMap(Robot("serial"), "123")
            verify(mockBeehiveRepository, Mockito.times(1)).deletetMap(any(), any())
        }
    }

    @Test
    fun testSetMapBoundariesReqIdAsString() {

        runBlocking {
            service.setMapBoundaries(Robot("serial"), "123", null)
            argumentCaptor<JSONObject>().apply {
                verify(mockNucleoRepository, Mockito.times(1)).executeCommand(any(), capture())
                assertNotNull(firstValue)
                assertTrue(firstValue.get("reqId") is String)
            }
        }
    }

    @Test
    fun testGetMapBoundaries() {
        val expectedMapId = "12345"

        runBlocking {
            service.getMapBoundaries(Robot("serial"), expectedMapId)
            argumentCaptor<JSONObject>().apply {
                verify(mockNucleoRepository, Mockito.times(1)).executeCommand(any(), capture())
                assertNotNull(firstValue)
                assertTrue(firstValue.get("reqId") is String)
                val params = firstValue.getJSONObject("params")
                assertNotNull(params)
                assertEquals("getMapBoundaries", firstValue.get("cmd"))
                assertTrue(params.has("mapId"))
                assertEquals(params.getString("mapId"), expectedMapId)
            }
        }
    }
}
