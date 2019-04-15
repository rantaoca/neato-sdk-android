/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.clients.nucleo


import com.neatorobotics.sdk.android.MockJSON
import com.neatorobotics.sdk.android.clients.nucleo.json.NucleoJSONParser

import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

import org.junit.Assert.*

@RunWith(MockitoJUnitRunner::class)
class NucleoJSONParserTest {

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testGetRobotConfiguredNetwork() {

        val json = JSONObject(MockJSON.loadJSON(this, "json/robots/wifi_networks_basic_1.json"))

        val list = NucleoJSONParser.parseWifiNetworks(json)

        //Number of networks
        assertTrue(list.size == 3)

        //Connected NETWORK
        assertTrue(list[0].isConnected)
        assertEquals(list[0].ipAddress, "100.200.200.200")
        assertEquals(list[0].subnetMask, "255.255.255.0")
        assertEquals(list[0].router, "10.0.0.2")
        assertEquals(list[0].macAddress, "48-2C-6A-1E-59-3D")

        //Not connected network
        assertFalse(list[1].isConnected)
        assertNull(list[1].ipAddress)
        assertNull(list[1].subnetMask)
        assertNull(list[1].router)
        assertNull(list[1].macAddress)
    }
}
