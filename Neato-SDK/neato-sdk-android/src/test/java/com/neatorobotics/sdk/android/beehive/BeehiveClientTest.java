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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class BeehiveClientTest {

    //class under test
    BeehiveClient client;

    @Mock
    BeehiveBaseClient mockBaseClient;

    @Mock
    NeatoCallback mockNeatoCallback;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        client = new BeehiveClient("http://example.com");
        client.baseClient = mockBaseClient;
    }

    @Test
    public void logout() throws Exception {
        final BeehiveResponse response = new BeehiveResponse(HttpURLConnection.HTTP_OK, new JSONObject());

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((NeatoCallback) invocation.getArguments()[4]).done(response);
                return null;
            }
        }).when(mockBaseClient).executeCall(anyString(),anyString(),anyString(),any(JSONObject.class),any(NeatoCallback.class));

        client.logout("fakeToken",mockNeatoCallback);

        verify(mockNeatoCallback).done(true);
    }

    @Test
    public void loadRobots_InvalidToken() throws Exception {
        final BeehiveResponse response = new BeehiveResponse(HttpURLConnection.HTTP_UNAUTHORIZED, new JSONObject());

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((NeatoCallback) invocation.getArguments()[4]).done(response);
                return null;
            }
        }).when(mockBaseClient).executeCall(anyString(),anyString(),anyString(),any(JSONObject.class),any(NeatoCallback.class));

        client.loadRobots("fakeToken",mockNeatoCallback);

        verify(mockNeatoCallback).fail(NeatoError.INVALID_TOKEN);
    }

    @Test
    public void loadRobots_Error() throws Exception {
        final BeehiveResponse response = new BeehiveResponse(HttpURLConnection.HTTP_NOT_FOUND, new JSONObject());

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((NeatoCallback) invocation.getArguments()[4]).done(response);
                return null;
            }
        }).when(mockBaseClient).executeCall(anyString(),anyString(),anyString(),any(JSONObject.class),any(NeatoCallback.class));

        client.loadRobots("fakeToken",mockNeatoCallback);

        verify(mockNeatoCallback).fail(NeatoError.GENERIC_ERROR);
    }

    @Test
    public void loadRobots_GenericError() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((NeatoCallback) invocation.getArguments()[4]).done(null);
                return null;
            }
        }).when(mockBaseClient).executeCall(anyString(),anyString(),anyString(),any(JSONObject.class),any(NeatoCallback.class));

        client.loadRobots("fakeToken",mockNeatoCallback);

        verify(mockNeatoCallback).fail(NeatoError.GENERIC_ERROR);
    }

    @Test
    public void loadRobots_OK_NullJSON() throws Exception {
        final BeehiveResponse response = new BeehiveResponse(HttpURLConnection.HTTP_OK, null);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((NeatoCallback) invocation.getArguments()[4]).done(response);
                return null;
            }
        }).when(mockBaseClient).executeCall(anyString(),anyString(),anyString(),any(JSONObject.class),any(NeatoCallback.class));

        client.loadRobots("fakeToken",mockNeatoCallback);

        verify(mockNeatoCallback, never()).fail(any(NeatoError.class));
        verify(mockNeatoCallback).done(any(ArrayList.class));//empty list, not null
    }

    @Test
    public void loadRobots_OK_Robot_In_List() throws Exception {
        final BeehiveResponse response = new BeehiveResponse(HttpURLConnection.HTTP_OK, new JSONObject(MockJSON.loadJSON(this,"json/robots/robots_list.json")));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((NeatoCallback) invocation.getArguments()[4]).done(response);
                return null;
            }
        }).when(mockBaseClient).executeCall(anyString(),anyString(),anyString(),any(JSONObject.class),any(NeatoCallback.class));

        client.loadRobots("fakeToken",mockNeatoCallback);

        verify(mockNeatoCallback, never()).fail(any(NeatoError.class));
        verify(mockNeatoCallback).done(any(ArrayList.class));
    }
}
