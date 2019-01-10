package com.neatorobotics.sdk.android.models;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class RobotTest {

    @Test
    public void testSetRecentFirmwares() throws Exception{

        String FIRMWARES_JSON = "{\n" +
                "    \"BotVacConnected\": {\n" +
                "      \"version\": \"1.2.3\",\n" +
                "      \"url\": \"https://neatorobotics.s3.amazonaws.com/firmwares/1.2.3.zip\"\n" +
                "    },\n" +
                "    \"Huey\": {\n" +
                "      \"version\": \"2.3.4\",\n" +
                "      \"url\": \"https://neatorobotics.s3.amazonaws.com/firmwares/2.3.4.zip\"\n" +
                "    }\n" +
                "}";
        Robot robotTest = new Robot(new JSONObject("{}"));
        RobotState state = new RobotState(new JSONObject("{}"));
        state.robotModelName = "BotVacConnected";
        state.firmware = "2.0.0";
        robotTest.state = state;

        robotTest.setRecentFirmwares(new JSONObject(FIRMWARES_JSON));

        assertNotNull(robotTest.recentFirmware);
        assertTrue(robotTest.recentFirmware.size() > 0);
        assertTrue(robotTest.recentFirmware.size() == 2);
        assertNotNull(robotTest.recentFirmware.get("botvacconnected"));
        assertNotNull(robotTest.recentFirmware.get("huey"));
        Assert.assertEquals(robotTest.recentFirmware.get("botvacconnected").version, "1.2.3");
        Assert.assertEquals(robotTest.recentFirmware.get("huey").version, "2.3.4");
        Assert.assertEquals(robotTest.recentFirmware.get("botvacconnected").model, "BotVacConnected");
        Assert.assertEquals(robotTest.recentFirmware.get("huey").model, "Huey");
        Assert.assertEquals(robotTest.recentFirmware.get("botvacconnected").url, "https://neatorobotics.s3.amazonaws.com/firmwares/1.2.3.zip");
        Assert.assertEquals(robotTest.recentFirmware.get("huey").url, "https://neatorobotics.s3.amazonaws.com/firmwares/2.3.4.zip");
    }

    @Test
    public void testIsUpdateAvailable() throws Exception{

        String FIRMWARES_JSON = "{\n" +
                "    \"BotVacConnected\": {\n" +
                "      \"version\": \"1.2.3\",\n" +
                "      \"filename\": \"https://neatorobotics.s3.amazonaws.com/firmwares/1.2.3.zip\"\n" +
                "    },\n" +
                "    \"Huey\": {\n" +
                "      \"version\": \"2.3.4\",\n" +
                "      \"filename\": \"https://neatorobotics.s3.amazonaws.com/firmwares/2.3.4.zip\"\n" +
                "    }\n" +
                "}";
        Robot robotTest = new Robot(new JSONObject("{}"));
        RobotState state = new RobotState(new JSONObject("{}"));
        state.robotModelName = "BotVacConnected";
        state.firmware = "2.0.0";
        robotTest.state = state;

        robotTest.setRecentFirmwares(new JSONObject(FIRMWARES_JSON));

        //version more rencent
        assertFalse(robotTest.isUpdateAvailable());

        //version same
        state.firmware = "1.2.3";
        assertFalse(robotTest.isUpdateAvailable());

        //version less recent
        state.firmware = "1.2.2";
        assertTrue(robotTest.isUpdateAvailable());
    }

    @Test
    public void hasService() throws Exception{
        Robot robot = new Robot(null);
        RobotState robotState = new RobotState(null);
        robot.state = robotState;

        robotState.availableServices.put("findMe","basic-1");

        assertTrue(robot.hasService("findMe"));
        assertFalse(robot.hasService("flyAndClean"));
    }

    @Test
    public void hasServiceSpecificVersion() throws Exception{
        Robot robot = new Robot(null);
        RobotState robotState = new RobotState(null);
        robot.state = robotState;

        robotState.availableServices.put("findMe","basic-1");

        assertTrue(robot.hasService("findMe","basic-1"));
        assertFalse(robot.hasService("findMe","minimal-1"));
    }
}
