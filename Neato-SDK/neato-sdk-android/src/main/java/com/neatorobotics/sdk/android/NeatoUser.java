package com.neatorobotics.sdk.android;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.VisibleForTesting;

import com.neatorobotics.sdk.android.authentication.AccessTokenDatasource;
import com.neatorobotics.sdk.android.authentication.NeatoAuthentication;
import com.neatorobotics.sdk.android.beehive.BeehiveBaseClient;
import com.neatorobotics.sdk.android.beehive.BeehiveJSONParser;
import com.neatorobotics.sdk.android.beehive.BeehiveResponse;
import com.neatorobotics.sdk.android.constants.NeatoError;
import com.neatorobotics.sdk.android.models.Robot;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Marco on 11/03/16.
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
        this.baseUrl = context.getString(R.string.beehive_endpoint);
        this.asyncCall = new AsyncCall();
    }

    /**
     * Revoke the current access token
     * @param callback
     */
    public void logout(final NeatoCallback<Boolean> callback) {
        asyncCall.executeCall(neatoAuthentication.getOauth2AccessToken(), "POST", baseUrl+"/oauth2/revoke", null, new NeatoCallback<BeehiveResponse>(){
            @Override
            public void done(BeehiveResponse result) {
                super.done(result);
                neatoAuthentication.clearAccessToken();
                callback.done(true);
            }
        });
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
                if(result == null) {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }else if(result.isUnauthorized()) {
                    callback.fail(NeatoError.INVALID_TOKEN);
                }else if(result.isHttpOK()) {
                    ArrayList<NeatoRobot> neatoRobots = new ArrayList<NeatoRobot>();
                    for (Robot model : BeehiveJSONParser.parseRobots(result.getJSON())) {
                        neatoRobots.add(new NeatoRobot(context,model));
                    }
                    callback.done(neatoRobots);
                }else {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }
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
                    callback.done(response);
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
