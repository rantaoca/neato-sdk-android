package com.neatorobotics.sdk.android.beehive;

import android.test.suitebuilder.annotation.SmallTest;

import com.neatorobotics.sdk.android.MockJSON;
import com.neatorobotics.sdk.android.NeatoCallback;
import com.neatorobotics.sdk.android.NeatoError;
import com.neatorobotics.sdk.android.model.NeatoRobot;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class BeehiveJSONParserTest {

    @Before
    public void setup() {}

    @Test
    public void parseRobots() throws Exception {
        JSONObject json = new JSONObject(MockJSON.loadJSON(this,"json/robots/robots_list.json"));

        ArrayList<NeatoRobot> robots = BeehiveJSONParser.parseRobots(json);
        assertNotNull(robots);
        assertEquals(2,robots.size());

        NeatoRobot robot = robots.get(0);
        assertEquals("Robot 1",robot.getName());
        assertEquals("botvac-85",robot.getModel());
        assertEquals("robot1",robot.getSerial());
        assertEquals("04a0fbe6b1f..2572d",robot.getSecretKey());
    }

    @Test
    public void parseRobotsInvalidJSON() throws Exception {
        JSONObject json = new JSONObject("{\"value\":{}}");

        ArrayList<NeatoRobot> robots = BeehiveJSONParser.parseRobots(json);
        assertNotNull(robots);
        assertEquals(0,robots.size());
    }

    @Test
    public void parseRobotsNullLinkedAt() throws Exception {
        JSONObject json = new JSONObject(MockJSON.loadJSON(this,"json/robots/robots_list_null_linked_at.json"));

        ArrayList<NeatoRobot> robots = BeehiveJSONParser.parseRobots(json);
        assertNotNull(robots);
        assertEquals(1,robots.size());
    }

    @Test
    public void parseRobotsNoLinkedAt() throws Exception {
        JSONObject json = new JSONObject(MockJSON.loadJSON(this,"json/robots/robots_list_no_linked_at.json"));

        ArrayList<NeatoRobot> robots = BeehiveJSONParser.parseRobots(json);
        assertNotNull(robots);
        assertEquals(1,robots.size());
    }
}
