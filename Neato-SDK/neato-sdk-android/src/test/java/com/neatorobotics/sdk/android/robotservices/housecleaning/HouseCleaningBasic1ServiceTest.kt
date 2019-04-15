/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.housecleaning

import com.neatorobotics.sdk.android.clients.nucleo.Nucleo
import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.nucleo.NucleoRepository
import com.neatorobotics.sdk.android.models.*
import com.neatorobotics.sdk.android.robotservices.cleaning.CleaningParams
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking

import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

import java.util.HashMap

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse

@RunWith(MockitoJUnitRunner::class)
class HouseCleaningBasic1ServiceTest {

    @Mock
    lateinit var mockNucleoRepository: NucleoRepository

    lateinit var service: HouseCleaningBasic1Service

    @Before
    fun setup() {
        service = HouseCleaningBasic1Service()

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
        assertFalse(service.isExtraCareModeSupported)
    }

    @Test
    fun testStopCleaningReqIdAsString() {
        runBlocking {
            service.stopCleaning(Robot("serial"))
            argumentCaptor<JSONObject>().apply {
                verify(mockNucleoRepository, times(1)).executeCommand(any(), capture())
                assertNotNull(firstValue)
                assertTrue(firstValue.get("reqId") is String)
            }
        }
    }

    @Test
    fun testStartCleaningReqIdAsString() {
        runBlocking {
            service.startCleaning(Robot("serial"))
            argumentCaptor<JSONObject>().apply {
                verify(mockNucleoRepository, times(1)).executeCommand(any(), capture())
                assertNotNull(firstValue)
                assertTrue(firstValue.get("reqId") is String)
            }
        }
    }

    @Test
    fun testPauseCleaningReqIdAsString() {
        runBlocking {
            service.pauseCleaning(Robot("serial"))
            argumentCaptor<JSONObject>().apply {
                verify(mockNucleoRepository, times(1)).executeCommand(any(), capture())
                assertNotNull(firstValue)
                assertTrue(firstValue.get("reqId") is String)
            }
        }
    }

    @Test
    fun testResumeCleaningReqIdAsString() {
        runBlocking {
            service.resumeCleaning(Robot("serial"))
            argumentCaptor<JSONObject>().apply {
                verify(mockNucleoRepository, times(1)).executeCommand(any(), capture())
                assertNotNull(firstValue)
                assertTrue(firstValue.get("reqId") is String)
            }
        }
    }

    @Test
    fun testStopCleaningCommandDefaults() {

        runBlocking {
            service.stopCleaning(Robot("serial"))
            argumentCaptor<JSONObject>().apply {
                verify(mockNucleoRepository, times(1)).executeCommand(any(), capture())
                assertNotNull(firstValue)
                assertEquals("stopCleaning", firstValue.get("cmd"))
            }
        }

    }

    @Test
    fun testStartCleaningCommandDefaults() {

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
                assertTrue(params.has("modifier"))
                assertTrue(params.has("category"))
                assertFalse(params.has("navigationMode"))
            }
        }
    }

    @Test
    fun testStartCleaningIgnoreUnsupportedParam() {

        val params = CleaningParams().apply {
            navigationMode = NavigationMode.EXTRA_CARE
        }

        runBlocking {
            service.startCleaning(Robot("serial"), params)
            argumentCaptor<JSONObject>().apply {
                verify(mockNucleoRepository, times(1)).executeCommand(any(), capture())
                assertNotNull(firstValue)
                val params = firstValue.getJSONObject("params")
                assertFalse(params.has("navigationMode"))
            }
        }
    }

    @Test
    fun testStartCleaningCommandOverride() {

        val params = CleaningParams().apply {
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
                assertTrue(p.has("modifier"))
                assertEquals(CleaningModifier.NORMAL.value, p.getInt("modifier")) //yes this is an exception
            }
        }
    }
}
