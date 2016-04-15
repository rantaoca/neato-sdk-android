package com.neatorobotics.sdk.android.authentication;

import android.content.Context;
import android.test.suitebuilder.annotation.SmallTest;

import com.neatorobotics.sdk.android.NeatoClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class NeatoClientTest {

    @Mock
    Context context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void singletonTest() throws Exception {
        NeatoClient neatoClient1 = NeatoClient.getInstance(context);
        assertNotNull(neatoClient1);

        NeatoClient neatoClient2 = NeatoClient.getInstance(context);
        assertNotNull(neatoClient2);

        assertEquals(neatoClient1, neatoClient2);
    }
}