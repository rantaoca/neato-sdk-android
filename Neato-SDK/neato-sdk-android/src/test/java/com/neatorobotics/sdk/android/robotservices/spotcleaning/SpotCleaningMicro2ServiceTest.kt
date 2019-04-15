/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.spotcleaning

import com.neatorobotics.sdk.android.clients.nucleo.NucleoRepository

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse

@RunWith(MockitoJUnitRunner::class)
class SpotCleaningMicro2ServiceTest {

    @Mock
    lateinit var mockNucleoRepository: NucleoRepository

    lateinit var service: SpotCleaningMicro2Service

    @Before
    fun setup() {
        service = SpotCleaningMicro2Service()
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this)
        service.nucleoRepository = mockNucleoRepository
    }

    @Test
    fun testEcoMode() {
        assertFalse(service.isEcoModeSupported)
    }

    @Test
    fun testTurboMode() {
        assertFalse(service.isTurboModeSupported)
    }

    @Test
    fun testExtraCare() {
        assertTrue(service.isExtraCareModeSupported)
    }

    @Test
    fun testArea() {
        assertFalse(service.isCleaningAreaSupported)
    }

    @Test
    fun testFrequency() {
        assertFalse(service.isCleaningFrequencySupported)
    }
}
