/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.scheduling


import com.neatorobotics.sdk.android.models.CleaningMode
import com.neatorobotics.sdk.android.models.ScheduleEvent
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

import org.junit.Assert.*

@RunWith(MockitoJUnitRunner::class)
class SchedulingBasic1ServiceTest {

    lateinit var service: SchedulingBasic1Service
    lateinit var event: ScheduleEvent

    @Before
    fun setup() {
        service = SchedulingBasic1Service()

        event = ScheduleEvent("12:30",
                CleaningMode.TURBO, 1,
                mutableListOf("1","2","3"), mutableListOf("n1","n2","n3"),
                "456")
    }


    @Test
    fun `test event should contain cleaning mode, start time and day`() {
        val convertedJson = service.convertEventToJSON(event)
        assertTrue(convertedJson!!.has("mode"))
        assertTrue(convertedJson.has("startTime"))
        assertTrue(convertedJson.has("day"))
    }

    @Test
    fun `test event should not contain boundary or boundaries`() {
        val convertedJson = service.convertEventToJSON(event)
        assertFalse(convertedJson!!.has("boundary"))
        assertFalse(convertedJson.has("boundaries"))
        assertFalse(convertedJson.has("boundaryIds"))
    }
}
