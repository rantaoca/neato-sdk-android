/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.housecleaning


import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

import org.junit.Assert.assertTrue

@RunWith(MockitoJUnitRunner::class)
class HouseCleaningAdvanced4ServiceTest {

    lateinit var service: HouseCleaningAdvanced4Service

    @Before
    fun setup() {
        service = HouseCleaningAdvanced4Service()
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testMultipleZonesSupport() {
        assertTrue(service.isMultipleZonesCleaningSupported)
    }

}
