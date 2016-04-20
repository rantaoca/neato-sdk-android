package com.neatorobotics.sdk.android.beehive;

import android.test.suitebuilder.annotation.SmallTest;

import com.neatorobotics.sdk.android.NeatoCallback;
import com.neatorobotics.sdk.android.NeatoError;

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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class BeehiveClientTest {

    @Mock
    BeehiveBaseClient mockBaseClient;

    @Mock
    NeatoCallback<Boolean> mockNeatoCallback;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void logoutOK() throws Exception {
        //class under test
        BeehiveClient client = new BeehiveClient("http://example.com");
        client.baseClient = mockBaseClient;

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
    public void logoutMalformedURL() throws Exception {
        //class under test
        BeehiveClient client = new BeehiveClient("malformed_url");
        client.baseClient = mockBaseClient;

        final BeehiveResponse response = new BeehiveResponse(HttpURLConnection.HTTP_OK, new JSONObject());

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((NeatoCallback) invocation.getArguments()[4]).done(response);
                return null;
            }
        }).when(mockBaseClient).executeCall(anyString(),anyString(),anyString(),any(JSONObject.class),any(NeatoCallback.class));

        client.logout("fakeToken",mockNeatoCallback);

        verify(mockNeatoCallback,never()).done(true);
        verify(mockNeatoCallback).fail(NeatoError.MALFORMED_URL);
    }
}
