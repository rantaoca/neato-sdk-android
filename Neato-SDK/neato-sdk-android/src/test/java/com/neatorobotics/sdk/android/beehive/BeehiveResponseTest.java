package com.neatorobotics.sdk.android.beehive;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.HttpURLConnection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class BeehiveResponseTest {
    @Test
    public void isUnauthorized() throws Exception {
        BeehiveResponse response = new BeehiveResponse(HttpURLConnection.HTTP_UNAUTHORIZED,new JSONObject());
        assertTrue(response.isUnauthorized());
    }

    @Test
    public void notIsUnauthorized() throws Exception {
        BeehiveResponse response = new BeehiveResponse(HttpURLConnection.HTTP_OK,new JSONObject());
        assertFalse(response.isUnauthorized());
    }
}
