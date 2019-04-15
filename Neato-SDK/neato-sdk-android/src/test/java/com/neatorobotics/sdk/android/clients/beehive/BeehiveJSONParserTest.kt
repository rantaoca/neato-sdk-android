/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.clients.beehive


import com.neatorobotics.sdk.android.MockJSON
import com.neatorobotics.sdk.android.clients.beehive.json.BeehiveJSONParser

import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

@RunWith(MockitoJUnitRunner::class)
class BeehiveJSONParserTest {

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testRobotListSize() {
        val json = JSONObject(MockJSON.loadJSON(this, "json/robots/robots_list.json"))
        val model = BeehiveJSONParser.parseRobotList(json)
        //Number of robots
        assertTrue(model.size == 2)
    }

    @Test
    fun testRobotListValues() {
        val json = JSONObject(MockJSON.loadJSON(this, "json/robots/robots_list.json"))
        val model = BeehiveJSONParser.parseRobotList(json)

        //name
        assertEquals(model[0].name, "Robot 1")
        assertEquals(model[1].name, "Robot 2")

        //serial
        assertEquals(model[0].serial, "robot1")
        assertEquals(model[1].serial, "robot2")

        //secret key
        assertEquals(model[0].secret_key, "123")
        assertEquals(model[1].secret_key, "456")

        //model
        assertEquals(model[0].model, "Model1")
        assertEquals(model[1].model, "Model2")
    }
}
