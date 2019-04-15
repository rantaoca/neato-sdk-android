/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.maps

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.junit.Assert.assertFalse

@RunWith(MockitoJUnitRunner::class)
class MapsBasic1ServiceTest {

    lateinit var service: MapsBasic1Service

    @Before
    fun setup() {
        service = MapsBasic1Service()
    }

    @Test
    fun testSupportFloorPlan() {
        assertFalse(service.isFloorPlanSupported)
    }

    @Test
    fun testSupportMultipleFloorPlans() {
        assertFalse(service.areMultipleFloorPlanSupported())
    }

    @Test
    fun testSupportZones() {
        assertFalse(service.areZonesSupported())
    }
}
