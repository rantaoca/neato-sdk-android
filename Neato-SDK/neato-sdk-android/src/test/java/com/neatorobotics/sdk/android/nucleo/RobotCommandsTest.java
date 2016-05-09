package com.neatorobotics.sdk.android.nucleo;

import android.test.suitebuilder.annotation.SmallTest;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
/**
 * Neato-SDK
 * Created by Marco on 09/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */
@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class RobotCommandsTest {
    @Test
    public void getCommandTest() throws Exception {
        JSONObject json = RobotCommands.get(RobotCommands.GET_ROBOT_STATE_COMMAND);
        assertEquals(new JSONObject(RobotCommands.GET_ROBOT_STATE_COMMAND).toString(),json.toString());
    }

    @Test
    public void getCommandWithParamsTest() throws Exception {
        String params = "{\"mode\":2,\"anotherProperty\":\"hello\"}";
        JSONObject json = RobotCommands.get(RobotCommands.START_CLEANING_COMMAND,params);
        assertTrue(json.has("params"));
        assertEquals(2,json.getJSONObject("params").getInt("mode"));
        assertEquals("hello",json.getJSONObject("params").getString("anotherProperty"));
    }
}
