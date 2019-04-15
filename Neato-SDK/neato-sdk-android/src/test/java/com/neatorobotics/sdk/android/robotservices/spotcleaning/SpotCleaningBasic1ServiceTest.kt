/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.spotcleaning

import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.nucleo.NucleoRepository
import com.neatorobotics.sdk.android.models.Robot
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking

import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue

@RunWith(MockitoJUnitRunner::class)
class SpotCleaningBasic1ServiceTest {

    @Mock
    lateinit var mockNucleoRepository: NucleoRepository

    lateinit var service: SpotCleaningBasic1Service

    @Before
    fun setup() {
        service = SpotCleaningBasic1Service()
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
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
    fun testTurboMode() {
        assertTrue(service.isTurboModeSupported)
    }

    @Test
    fun testExtraCare() {
        assertFalse(service.isExtraCareModeSupported)
    }

    @Test
    fun testArea() {
        assertTrue(service.isCleaningAreaSupported)
    }

    @Test
    fun testFrequency() {
        assertTrue(service.isCleaningFrequencySupported)
    }

    @Test
    fun testStartCleaningReqIdAsString() {
        runBlocking {
            service.startCleaning(Robot("serial"))
            argumentCaptor<JSONObject>().apply {
                com.nhaarman.mockitokotlin2.verify(mockNucleoRepository, com.nhaarman.mockitokotlin2.times(1)).executeCommand(any(), capture())
                assertNotNull(firstValue)
                assertTrue(firstValue.get("reqId") is String)
            }
        }
    }
}
