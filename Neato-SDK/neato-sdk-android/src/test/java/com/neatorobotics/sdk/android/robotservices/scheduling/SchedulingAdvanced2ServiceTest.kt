/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.scheduling


import com.neatorobotics.sdk.android.models.CleaningMode
import com.neatorobotics.sdk.android.models.ScheduleEvent
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SchedulingAdvanced2ServiceTest {

    lateinit var service: SchedulingAdvanced2Service
    lateinit var event: ScheduleEvent

    @Before
    fun setup() {
        service = SchedulingAdvanced2Service()

        event = ScheduleEvent("12:30",
                CleaningMode.TURBO, 1,
                mutableListOf("1","2","3"), mutableListOf("Kitchen","Bedroom","n3"),
                "456")
    }

    @Test
    fun `test event should contain cleaning mode, start time, day and multiple zones info`() {

        val convertedJson = service.convertEventToJSON(event)
        assertTrue(convertedJson!!.has("mode"))
        assertTrue(convertedJson.has("startTime"))
        assertTrue(convertedJson.has("day"))
        assertFalse(convertedJson.has("boundary"))
        assertTrue(convertedJson.has("boundaries"))
        assertEquals("1", convertedJson.getJSONArray("boundaries").getJSONObject(0).getString("id"))
        assertEquals("Kitchen", convertedJson.getJSONArray("boundaries").getJSONObject(0).getString("name"))
        assertEquals("2", convertedJson.getJSONArray("boundaries").getJSONObject(1).getString("id"))
        assertEquals("Bedroom", convertedJson.getJSONArray("boundaries").getJSONObject(1).getString("name"))
        assertEquals("456", convertedJson.getString("mapId"))
    }

    @Test
    fun `test event to send to the robot should contain cleaning mode, start time, day only multiple zones ids`() {

        val convertedJson = service.convertEventToJSON(event, true)
        assertTrue(convertedJson!!.has("mode"))
        assertTrue(convertedJson.has("startTime"))
        assertTrue(convertedJson.has("day"))
        assertFalse(convertedJson.has("boundary"))
        assertFalse(convertedJson.has("boundaries"))
        assertTrue(convertedJson.has("boundaryIds"))

        assertEquals("1", convertedJson.getJSONArray("boundaryIds").getString(0))
        assertEquals("2", convertedJson.getJSONArray("boundaryIds").getString(1))
    }
}
