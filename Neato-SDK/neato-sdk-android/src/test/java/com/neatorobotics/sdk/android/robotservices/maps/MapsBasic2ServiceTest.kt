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
class MapsBasic2ServiceTest {

    lateinit var service: MapsBasic2Service

    @Before
    fun setup() {
        service = MapsBasic2Service()
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
        assertTrue(service.areZonesSupported())
    }
}
