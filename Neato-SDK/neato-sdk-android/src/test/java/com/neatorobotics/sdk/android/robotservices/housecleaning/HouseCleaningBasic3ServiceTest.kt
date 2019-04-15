/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.housecleaning

import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.nucleo.NucleoRepository
import com.neatorobotics.sdk.android.models.*
import com.neatorobotics.sdk.android.robotservices.cleaning.CleaningParams
import com.neatorobotics.sdk.android.testutils.RobotTestFactory
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking

import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

import java.util.ArrayList

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue

@RunWith(MockitoJUnitRunner::class)
class HouseCleaningBasic3ServiceTest {

    @Mock
    lateinit var mockNucleoRepository: NucleoRepository

    lateinit var service: HouseCleaningBasic3Service

    @Before
    fun setup() {
        service = HouseCleaningBasic3Service()

        MockitoAnnotations.initMocks(this)
        service.nucleoRepository = mockNucleoRepository
        runBlocking {
            whenever(mockNucleoRepository.executeCommand(any(), any())).thenReturn(Resource.success(JSONObject()))
        }
    }

    @Test
    fun testEcoMode() {
        assertTrue(service.isEcoModeSupported)
    }

    @Test
    fun testCleaningArea() {
        assertFalse(service.isCleaningAreaSupported)
    }

    @Test
    fun testTurboMode() {
        assertTrue(service.isTurboModeSupported)
    }

    @Test
    fun testModifier() {
        assertFalse(service.isCleaningFrequencySupported)
    }

    @Test
    fun testExtraCare() {
        assertTrue(service.isExtraCareModeSupported)
    }

    @Test
    fun testStartCleaningCommandDefaultsWithoutAFloorPlan() {

        runBlocking {
            service.startCleaning(Robot("serial"))
            argumentCaptor<JSONObject>().apply {
                verify(mockNucleoRepository, times(1)).executeCommand(any(), capture())
                assertNotNull(firstValue)
                val params = firstValue.getJSONObject("params")
                assertNotNull(params)
                assertEquals("startCleaning", firstValue.get("cmd"))
                assertEquals(CleaningCategory.HOUSE.value, params.get("category"))
                assertTrue(params.has("mode"))
                assertFalse(params.has("modifier"))
                assertTrue(params.has("category"))
                assertTrue(params.has("navigationMode"))
            }
        }
    }

    @Test
    fun testStartCleaningCommandOverrideWithoutAFloorPlan() {

        val params = CleaningParams().apply {
            navigationMode = NavigationMode.EXTRA_CARE
            mode = CleaningMode.ECO
            modifier = CleaningModifier.DOUBLE
        }

        runBlocking {
            service.startCleaning(Robot("serial"), params)
            argumentCaptor<JSONObject>().apply {
                verify(mockNucleoRepository, times(1)).executeCommand(any(), capture())
                assertNotNull(firstValue)
                val p = firstValue.getJSONObject("params")
                assertTrue(p.has("mode"))
                assertEquals(CleaningMode.ECO.value, p.getInt("mode"))
                assertFalse(p.has("modifier"))
                assertTrue(p.has("navigationMode"))
                assertEquals(NavigationMode.EXTRA_CARE.value, p.getInt("navigationMode"))
            }
        }
    }

    @Test
    fun testStartCleaningWithAFloorPlanAndWithoutRobotService() {
        
        val robot = RobotTestFactory.idleRobot
        val mapsIds = ArrayList<String>()
        mapsIds.add("1234")
        robot.persistentMapsIds = mapsIds

        runBlocking {
            service.startCleaning(robot)
            argumentCaptor<JSONObject>().apply {
                verify(mockNucleoRepository, times(1)).executeCommand(any(), capture())
                assertNotNull(firstValue)
                val params = firstValue.getJSONObject("params")
                assertNotNull(params)
                assertEquals("startCleaning", firstValue.get("cmd"))
                assertEquals(CleaningCategory.HOUSE.value, params.get("category"))
                assertTrue(params.has("mode"))
                assertFalse(params.has("modifier"))
                assertTrue(params.has("category"))
                assertTrue(params.has("navigationMode"))
            }
        }
    }
}
