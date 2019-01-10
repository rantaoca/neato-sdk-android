package com.neatorobotics.sdk.android.nucleo;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.HttpURLConnection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class NucleoResponseTest {
    @Test
    public void isResponseOK() throws Exception {
        NucleoResponse response = new NucleoResponse(HttpURLConnection.HTTP_OK,new JSONObject("{\"result\":\"ok\"}"));
        assertTrue(response.isResponseOK());
    }

    @Test
    public void isNotResponseOK() throws Exception {
        NucleoResponse response = new NucleoResponse(HttpURLConnection.HTTP_OK,new JSONObject("{\"result\":\"ko\"}"));
        assertFalse(response.isResponseOK());
    }

    @Test
    public void getStatusCode() throws Exception {
        NucleoResponse response = new NucleoResponse(HttpURLConnection.HTTP_OK,new JSONObject());
        assertEquals(response.getStatusCode(), HttpURLConnection.HTTP_OK);
    }

    @Test
    public void getJSON() throws Exception {
        JSONObject json = new JSONObject("{\"result\":\"ko\"}");
        NucleoResponse response = new NucleoResponse(HttpURLConnection.HTTP_OK,json);
        assertEquals(response.getJSON().toString(), json.toString());
    }

    @Test
    public void isHttpOK() throws Exception {
        NucleoResponse response = new NucleoResponse(HttpURLConnection.HTTP_OK,new JSONObject());
        assertTrue(response.isHttpOK());
    }

    @Test
    public void isOK() throws Exception {
        NucleoResponse response = new NucleoResponse(HttpURLConnection.HTTP_OK,new JSONObject("{\"result\":\"ok\"}"));
        assertTrue(response.isOK());
    }

    @Test
    public void isNotOK() throws Exception {
        NucleoResponse response = new NucleoResponse(HttpURLConnection.HTTP_UNAUTHORIZED,new JSONObject("{\"result\":\"ok\"}"));
        assertFalse(response.isOK());

        response = new NucleoResponse(HttpURLConnection.HTTP_OK,new JSONObject("{\"result\":\"error\"}"));
        assertFalse(response.isOK());
    }



    @Test
    public void isStateResponse() throws Exception {
        NucleoResponse response = new NucleoResponse(HttpURLConnection.HTTP_OK,new JSONObject("{\"result\":\"ok\",\"state\":1}"));
        assertTrue(response.isStateResponse());
    }

    @Test
    public void isNotStateResponse() throws Exception {
        NucleoResponse response = new NucleoResponse(HttpURLConnection.HTTP_OK,new JSONObject("{\"result\":\"ok\",\"action\":1}"));
        assertFalse(response.isStateResponse());
    }
}
