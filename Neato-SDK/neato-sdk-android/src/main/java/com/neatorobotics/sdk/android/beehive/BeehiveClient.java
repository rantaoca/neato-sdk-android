package com.neatorobotics.sdk.android.beehive;

import android.support.annotation.VisibleForTesting;

import com.neatorobotics.sdk.android.NeatoCallback;
import com.neatorobotics.sdk.android.NeatoError;
import com.neatorobotics.sdk.android.model.NeatoRobot;

import java.util.ArrayList;

/**
 * Created by Marco on 24/03/16.
 */
public class BeehiveClient {

    private static final String TAG = "BeehiveClient";

    private String baseUrl;
    @VisibleForTesting
    public BeehiveBaseClient baseClient;

    //constructor
    public BeehiveClient(String beehiveBaseUrl) {
        this.baseClient = new BeehiveBaseClient();
        this.baseUrl = beehiveBaseUrl;
    }

    public void logout(final String oauth2AccessToken, final NeatoCallback<Boolean> callback) {
        baseClient.executeCall(oauth2AccessToken, "POST", baseUrl+"/oauth2/revoke", null, new NeatoCallback<BeehiveResponse>(){
            @Override
            public void done(BeehiveResponse result) {
                super.done(result);
                callback.done(true);
            }
        });
    }

    public void loadRobots(final String oauth2AccessToken, final NeatoCallback<ArrayList<NeatoRobot>> callback) {
        baseClient.executeCall(oauth2AccessToken, "GET", baseUrl+"/users/me/robots", null, new NeatoCallback<BeehiveResponse>(){
            @Override
            public void done(BeehiveResponse result) {
                super.done(result);
                if(result == null) {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }else if(result.isUnauthorized()) {
                    callback.fail(NeatoError.INVALID_TOKEN);
                }else if(result.isHttpOK()) {
                    callback.done(BeehiveJSONParser.parseRobots(result.getJSON()));
                }else {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }
            }
        });
    }
}
