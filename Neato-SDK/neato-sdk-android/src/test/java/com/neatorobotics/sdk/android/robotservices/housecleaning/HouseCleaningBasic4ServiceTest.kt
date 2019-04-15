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

import org.junit.Assert.assertFalse

@RunWith(MockitoJUnitRunner::class)
class HouseCleaningBasic4ServiceTest {

    lateinit var service: HouseCleaningBasic4Service

    @Before
    fun setup() {
        service = HouseCleaningBasic4Service()
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testMultipleZonesSupport() {
        assertFalse(service.isMultipleZonesCleaningSupported)
    }

}
