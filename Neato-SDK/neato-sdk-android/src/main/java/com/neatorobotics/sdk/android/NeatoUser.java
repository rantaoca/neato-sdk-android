package com.neatorobotics.sdk.android;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.neatorobotics.sdk.android.authentication.AccessTokenDatasource;
import com.neatorobotics.sdk.android.authentication.NeatoAuthentication;
import com.neatorobotics.sdk.android.beehive.BeehiveBaseClient;
import com.neatorobotics.sdk.android.beehive.BeehiveJSONParser;
import com.neatorobotics.sdk.android.beehive.BeehiveResponse;
import com.neatorobotics.sdk.android.model.NeatoRobot;

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
    protected BeehiveBaseClient baseClient;

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
        this.baseClient = new BeehiveBaseClient();
    }

    /**
     * Revoke the current access token
     * @param callback
     */
    public void logout(final NeatoCallback<Boolean> callback) {
        baseClient.executeCall(neatoAuthentication.getOauth2AccessToken(), "POST", baseUrl+"/oauth2/revoke", null, new NeatoCallback<BeehiveResponse>(){
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
        baseClient.executeCall(neatoAuthentication.getOauth2AccessToken(), "GET", baseUrl+"/users/me/robots", null, new NeatoCallback<BeehiveResponse>(){
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
