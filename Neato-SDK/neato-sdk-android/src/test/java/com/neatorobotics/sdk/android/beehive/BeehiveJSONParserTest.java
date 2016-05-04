package com.neatorobotics.sdk.android.beehive;

import android.content.Context;
import android.test.suitebuilder.annotation.SmallTest;

import com.neatorobotics.sdk.android.MockJSON;
import com.neatorobotics.sdk.android.models.Robot;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class BeehiveJSONParserTest {

    @Mock
    Context ctx;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void parseRobots() throws Exception {
        JSONObject json = new JSONObject(MockJSON.loadJSON(this,"json/robots/robots_list.json"));

        ArrayList<Robot> robots = BeehiveJSONParser.parseRobots(json);
        assertNotNull(robots);
        assertEquals(2,robots.size());

        Robot robot = robots.get(0);
        assertEquals("Robot 1",robot.name);
        assertEquals("botvac-85",robot.model);
        assertEquals("robot1",robot.serial);
        assertEquals("04a0fbe6b1f..2572d",robot.secret_key);
    }

    @Test
    public void parseRobotsInvalidJSON() throws Exception {
        JSONObject json = new JSONObject("{\"value\":{}}");

        ArrayList<Robot> robots = BeehiveJSONParser.parseRobots(json);
        assertNotNull(robots);
        assertEquals(0,robots.size());
    }

    @Test
    public void parseRobotsNullLinkedAt() throws Exception {
        JSONObject json = new JSONObject(MockJSON.loadJSON(this,"json/robots/robots_list_null_linked_at.json"));

        ArrayList<Robot> robots = BeehiveJSONParser.parseRobots(json);
        assertNotNull(robots);
        assertEquals(1,robots.size());
    }

    @Test
    public void parseRobotsNoLinkedAt() throws Exception {
        JSONObject json = new JSONObject(MockJSON.loadJSON(this,"json/robots/robots_list_no_linked_at.json"));

        ArrayList<Robot> robots = BeehiveJSONParser.parseRobots(json);
        assertNotNull(robots);
        assertEquals(1,robots.size());
    }
}
