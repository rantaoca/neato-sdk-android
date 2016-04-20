package com.neatorobotics.sdk.android.beehive;

import android.os.AsyncTask;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.neatorobotics.sdk.android.NeatoCallback;
import com.neatorobotics.sdk.android.NeatoError;
import com.neatorobotics.sdk.android.model.NeatoRobot;

import java.net.MalformedURLException;
import java.net.URL;
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
        try {
            baseClient.executeCall(oauth2AccessToken, "POST", new URL(baseUrl+"/oauth2/revoke").toString(), null, new NeatoCallback<BeehiveResponse>(){
                @Override
                public void done(BeehiveResponse result) {
                    super.done(result);
                    callback.done(true);
                }
            });
        } catch (MalformedURLException e) {
            Log.e(TAG,"Exception",e);
            callback.fail(NeatoError.MALFORMED_URL);
        }
    }

    public void loadRobots(final String oauth2AccessToken, final NeatoCallback<ArrayList<NeatoRobot>> callback) {
        try {
            baseClient.executeCall(oauth2AccessToken, "GET", new URL(baseUrl+"/users/me/robots").toString(), null, new NeatoCallback<BeehiveResponse>(){
                @Override
                public void done(BeehiveResponse result) {
                    super.done(result);
                    if(result.isUnauthorized()) {
                        callback.fail(NeatoError.INVALID_TOKEN);
                        return;
                    }
                    else if(result.isHttpOK()) {
                        callback.done(BeehiveJSONParser.parseRobots(result.getJSON()));
                        return;
                    }else {
                        callback.fail(NeatoError.GENERIC_ERROR);
                        return;
                    }
                }
            });
        } catch (MalformedURLException e) {
            Log.e(TAG,"Exception",e);
            callback.fail(NeatoError.MALFORMED_URL);
        }
    }
}
