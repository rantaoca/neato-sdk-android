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

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class AccessTokenDatasourceTest {

    @Mock
    Context context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void customAccessTokenDatasourceTest() throws Exception {
        NeatoClient.getInstance(context).getSession().setAccessTokenDatasource(new CustomAccessTokenDatasource());
        NeatoAuthSession session = NeatoClient.getInstance(context).getSession();
        Date expires = new Date();
        session.setAccessToken("123", expires);
        assertEquals("123", session.getAccessToken());
        assertTrue(session.isAuthenticated());
    }

    /**
     * Custom accessTokenDatasource for testing
     */
    private class CustomAccessTokenDatasource implements AccessTokenDatasource{

        public String token;
        public Date expires;

        @Override
        public void saveToken(String token, Date expires) {
            this.token = token;
            this.expires = expires;
        }

        @Override
        public String getToken() {
            return token;
        }

        @Override
        public boolean isTokenValid() {
            return true;
        }
    }
}