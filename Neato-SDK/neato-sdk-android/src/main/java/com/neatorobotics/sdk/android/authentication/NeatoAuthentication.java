package com.neatorobotics.sdk.android.authentication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.neatorobotics.sdk.android.R;

/**
 * Created by Marco on 24/03/16.
 */
public class NeatoAuthentication {
    private String clientId;
    private String redirectUri;
    private NeatoOAuth2Scope[] scopes;

    /**
     *
     * @param clientId
     * @param redirectUri
     * @param scopes
     */
    public NeatoAuthentication(String clientId, String redirectUri, NeatoOAuth2Scope[] scopes){
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.scopes = scopes;
    }

    /**
     * Start the OAuth 2 authentication flow
     */
    public void openLoginInBrowser(Context context) {
        String authUrl = buildOAuthAuthenticationUrl(context.getString(R.string.oauth2_authentication_url),clientId,buildScopesParameter(scopes), redirectUri);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl));
        context.startActivity(intent);
    }

    /**
     *
     * @return the OAuth 2 authentication string URL
     */
    protected String buildOAuthAuthenticationUrl(String formattedUrl, String clientId, String scopes, String redirectUri) {
        String authUrl = String.format(formattedUrl, clientId, scopes, redirectUri);
        return authUrl;
    }

    /**
     *
     * @param scopes
     * @return the comma separated string of scopes
     */
    protected String buildScopesParameter(NeatoOAuth2Scope[] scopes) {
        StringBuffer scopesBuffer = new StringBuffer("");
        if(scopes != null && scopes.length > 0) {
            for (NeatoOAuth2Scope scope : scopes) {
                scopesBuffer.append(scope);
                scopesBuffer.append(",");
            }
            scopesBuffer.deleteCharAt(scopesBuffer.length()-1);
        }
        return scopesBuffer.toString();
    }
}
