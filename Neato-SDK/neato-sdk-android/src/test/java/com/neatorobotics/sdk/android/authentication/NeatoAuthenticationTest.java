package com.neatorobotics.sdk.android.authentication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class NeatoAuthenticationTest {

    private final String FORMATTED_AUTH_URL = "https://beehive.neatocloud.com/oauth2/authorize?client_id=%1$s&scope=%2$s&response_type=token&redirect_uri=%3$s";

    @Test
    public void buildOAuthAuthenticationUrl() throws Exception {
        String clientId = "12323232";
        String redirectUri = "myapp://neato";
        NeatoOAuth2Scope[] scopes = new NeatoOAuth2Scope[]{NeatoOAuth2Scope.CONTROL_ROBOTS};

        NeatoAuthentication neatoAuthentication = NeatoAuthentication.getInstance(null);

        assertTrue(neatoAuthentication.buildOAuthAuthenticationUrl(FORMATTED_AUTH_URL,
                clientId,
                neatoAuthentication.buildScopesParameter(scopes),
                redirectUri).equals(
                "https://beehive.neatocloud.com/oauth2/authorize?client_id=12323232&scope=control_robots&response_type=token&redirect_uri=myapp://neato"));
    }

    @Test
    public void buildOAuthAuthenticationUrlWithNullValues() throws Exception {
        String clientId = null;
        String redirectUri = null;
        NeatoOAuth2Scope[] scopes = null;

        NeatoAuthentication neatoAuthentication = NeatoAuthentication.getInstance(null);

        assertTrue(neatoAuthentication.buildOAuthAuthenticationUrl(FORMATTED_AUTH_URL,
                clientId,
                neatoAuthentication.buildScopesParameter(scopes),
                redirectUri).equals(
                "https://beehive.neatocloud.com/oauth2/authorize?client_id=null&scope=&response_type=token&redirect_uri=null"));
    }

    @Test
    public void buildScopesParameter() throws Exception {
        NeatoAuthentication neatoAuthentication = NeatoAuthentication.getInstance(null);

        NeatoOAuth2Scope[] scopes = new NeatoOAuth2Scope[]{NeatoOAuth2Scope.CONTROL_ROBOTS};
        assertTrue(neatoAuthentication.buildScopesParameter(scopes).equals("control_robots"));

        scopes = new NeatoOAuth2Scope[]{NeatoOAuth2Scope.CONTROL_ROBOTS, NeatoOAuth2Scope.PUBLIC_PROFILE};
        assertTrue(neatoAuthentication.buildScopesParameter(scopes).equals("control_robots+public_profile"));

        scopes = new NeatoOAuth2Scope[]{NeatoOAuth2Scope.PUBLIC_PROFILE, NeatoOAuth2Scope.CONTROL_ROBOTS};
        assertTrue(neatoAuthentication.buildScopesParameter(scopes).equals("public_profile+control_robots"));

        scopes = new NeatoOAuth2Scope[]{};
        assertTrue(neatoAuthentication.buildScopesParameter(scopes).equals(""));

        scopes = null;
        assertTrue(neatoAuthentication.buildScopesParameter(scopes).equals(""));
    }
}
