package com.neatorobotics.sdk.android;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.VisibleForTesting;

import com.neatorobotics.sdk.android.authentication.AccessTokenDatasource;
import com.neatorobotics.sdk.android.authentication.NeatoAuthentication;
import com.neatorobotics.sdk.android.beehive.Beehive;
import com.neatorobotics.sdk.android.beehive.BeehiveBaseClient;
import com.neatorobotics.sdk.android.beehive.BeehiveJSONParser;
import com.neatorobotics.sdk.android.beehive.BeehiveResponse;
import com.neatorobotics.sdk.android.models.Robot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */
public class NeatoUser {

    private Context context;
    private static NeatoUser instance = null;
    @VisibleForTesting
    protected NeatoAuthentication neatoAuthentication;

    private String baseUrl;
    @VisibleForTesting
    protected AsyncCall asyncCall;

    /**
     * Use this method to get the singleton neato user.
     * @param context
     * @return
     */
    public static NeatoUser getInstance(Context context) {
        if(instance == null) {
            instance = new NeatoUser(context);
        }
        return instance;
    }

    /**
     * Use this method to get the singleton neato user that use a custom access token datasource.
     * @param context
     * @param accessTokenDatasource
     * @return
     */
    public static NeatoUser getInstance(Context context, AccessTokenDatasource accessTokenDatasource) {
        NeatoUser client = getInstance(context);
        client.neatoAuthentication = NeatoAuthentication.getInstance(context,accessTokenDatasource);
        return client;
    }

    /**
     * Private singleton constructor
     * @param context
     */
    private NeatoUser(Context context) {
        this.context = context;
        neatoAuthentication = NeatoAuthentication.getInstance(context);
        this.baseUrl = Beehive.URL;
        this.asyncCall = new AsyncCall();
    }

    /**
     * Revoke the current access token
     * @param callback
     */
    public void logout(final NeatoCallback<Boolean> callback) {
        JSONObject params = null;
        try {
            params = new JSONObject("{\"token\":\""+neatoAuthentication.getOauth2AccessToken()+"\"}");
            asyncCall.executeCall(neatoAuthentication.getOauth2AccessToken(), "POST", baseUrl+"/oauth2/revoke", params, new NeatoCallback<BeehiveResponse>(){
                @Override
                public void done(BeehiveResponse result) {
                    super.done(result);
                    neatoAuthentication.clearAccessToken();
                    callback.done(true);
                }
            });
        } catch (JSONException e) {
            callback.fail(NeatoError.GENERIC_ERROR);
        }
    }

    /**
     * Retrieve the user robots list.
     * @param callback
     */
    public void loadRobots(final NeatoCallback<ArrayList<NeatoRobot>> callback) {
        asyncCall.executeCall(neatoAuthentication.getOauth2AccessToken(), "GET", baseUrl+"/users/me/robots", null, new NeatoCallback<BeehiveResponse>(){
            @Override
            public void done(BeehiveResponse result) {
                super.done(result);
                ArrayList<NeatoRobot> neatoRobots = new ArrayList<NeatoRobot>();
                for (Robot model : BeehiveJSONParser.parseRobots(result.getJSON())) {
                    neatoRobots.add(new NeatoRobot(context,model));
                }
                callback.done(neatoRobots);
            }

            @Override
            public void fail(NeatoError error) {
                super.fail(error);
                callback.fail(error);
            }
        });
    }

    /**
     * Get information about the user currently authenticated.
     * @param callback
     */
    public void getUserInfo(final NeatoCallback<JSONObject> callback) {
        asyncCall.executeCall(neatoAuthentication.getOauth2AccessToken(), "GET", baseUrl+"/users/me", null, new NeatoCallback<BeehiveResponse>(){
            @Override
            public void done(BeehiveResponse result) {
                super.done(result);
                callback.done(result.getJSON());
            }

            @Override
            public void fail(NeatoError error) {
                super.fail(error);
                callback.fail(error);
            }
        });
    }

    /**
     * Get robot available maps/stats.
     * @param callback
     */
    protected void getMaps(final String robotSerial, final NeatoCallback<JSONObject> callback) {
        asyncCall.executeCall(neatoAuthentication.getOauth2AccessToken(), "GET", baseUrl+"/users/me/robots/"+robotSerial+"/maps", null, new NeatoCallback<BeehiveResponse>(){
            @Override
            public void done(BeehiveResponse result) {
                super.done(result);
                callback.done(result.getJSON());
            }

            @Override
            public void fail(NeatoError error) {
                super.fail(error);
                callback.fail(error);
            }
        });
    }

    /**
     * Get robot available maps/stats.
     * @param callback
     */
    protected void getMap(final String robotSerial, final String mapId, final NeatoCallback<JSONObject> callback) {
        asyncCall.executeCall(neatoAuthentication.getOauth2AccessToken(), "GET", baseUrl+"/users/me/robots/"+robotSerial+"/maps/"+mapId, null, new NeatoCallback<BeehiveResponse>(){
            @Override
            public void done(BeehiveResponse result) {
                super.done(result);
                callback.done(result.getJSON());
            }

            @Override
            public void fail(NeatoError error) {
                super.fail(error);
                callback.fail(error);
            }
        });
    }

    //region async call
    protected class AsyncCall {

        private static final String TAG = "AsyncCall";

        public void executeCall(final String accessToken, final String verb, final String url, final JSONObject input, final NeatoCallback<BeehiveResponse> callback) {
            final AsyncTask<Void, Void, BeehiveResponse> task = new AsyncTask<Void, Void, BeehiveResponse>() {
                protected void onPreExecute() {}
                protected BeehiveResponse doInBackground(Void... unused) {
                    return BeehiveBaseClient.executeCall(accessToken,verb,url,input);
                }
                protected void onPostExecute(BeehiveResponse response) {
                    if(response != null && response.getJSON() != null && response.isHttpOK()) {
                        callback.done(response);
                    }else if(response != null && response.isUnauthorized()) {
                        callback.fail(NeatoError.INVALID_TOKEN);
                    }else {
                        callback.fail(NeatoError.GENERIC_ERROR);
                    }
                }
            };
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }else {
                task.execute();
            }
        }
    }
    //endregion
}
