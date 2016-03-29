package com.neatorobotics.sdk.android.authentication;

import android.content.Context;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class NeatoAuthSessionTest {

    @Mock
    Context context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void singletonTest() throws Exception {
        NeatoAuthSession session = NeatoAuthSession.getInstance(context);
        assertNotNull(session);

        NeatoAuthSession session2 = NeatoAuthSession.getInstance(context);
        assertNotNull(session2);

        assertEquals(session,session2);
    }

}