/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.maps

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

import org.junit.Assert.*

@RunWith(MockitoJUnitRunner::class)
class MapsMacro1ServiceTest {

    lateinit var service: MapsMacro1Service

    @Before
    fun setup() {
        service = MapsMacro1Service()
    }

    @Test
    fun testSupportFloorPlan() {
        assertTrue(service.isFloorPlanSupported)
    }

    @Test
    fun testSupportMultipleFloorPlans() {
        assertTrue(service.areMultipleFloorPlanSupported())
    }

    @Test
    fun testSupportZones() {
        assertFalse(service.areZonesSupported())
    }
}
