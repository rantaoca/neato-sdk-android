/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.scheduling

import com.neatorobotics.sdk.android.models.CleaningMode
import com.neatorobotics.sdk.android.models.ScheduleEvent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse

@RunWith(MockitoJUnitRunner::class)
class SchedulingBasic2ServiceTest {

    lateinit var service: SchedulingBasic2Service
    lateinit var event: ScheduleEvent

    @Before
    fun setup() {
        service = SchedulingBasic2Service()

        event = ScheduleEvent("12:30",
                CleaningMode.TURBO, 1,
                mutableListOf("1","2","3"), mutableListOf("n1","n2","n3"),
                "456")
    }


    @Test
    fun `test event should contain cleaning mode, start time, day and single boundary info`() {

        val convertedJson = service.convertEventToJSON(event)
        assertTrue(convertedJson!!.has("mode"))
        assertTrue(convertedJson.has("startTime"))
        assertTrue(convertedJson.has("day"))
        assertTrue(convertedJson.has("boundary"))
        assertEquals("1", convertedJson.getJSONObject("boundary").get("id"))
        assertEquals("n1", convertedJson.getJSONObject("boundary").get("name"))
    }

    @Test
    fun `test event to send to the robot should contain cleaning mode, start time, day and only boundaryId`() {

        val convertedJson = service.convertEventToJSON(event, true)
        assertTrue(convertedJson!!.has("mode"))
        assertTrue(convertedJson.has("startTime"))
        assertTrue(convertedJson.has("day"))
        assertFalse(convertedJson.has("boundary"))// <--- important
        assertTrue(convertedJson.has("boundaryId"))
        assertEquals("1", convertedJson.get("boundaryId"))
    }
}
