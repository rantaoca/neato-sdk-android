package com.neatorobotics.sdk.android.authentication;

import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class NeatoAuthSessionTest {

    @Test
    public void singletonTest() throws Exception {
        NeatoAuthSession session = NeatoAuthSession.getInstance();
        assertNotNull(session);

        NeatoAuthSession session2 = NeatoAuthSession.getInstance();
        assertNotNull(session2);

        assertEquals(session,session2);
    }

}