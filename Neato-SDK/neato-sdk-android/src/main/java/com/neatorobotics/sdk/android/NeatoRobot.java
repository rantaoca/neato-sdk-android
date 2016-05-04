package com.neatorobotics.sdk.android;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.VisibleForTesting;

import com.neatorobotics.sdk.android.models.Robot;
import com.neatorobotics.sdk.android.models.State;
import com.neatorobotics.sdk.android.nucleo.NucleoBaseClient;
import com.neatorobotics.sdk.android.nucleo.NucleoCommands;
import com.neatorobotics.sdk.android.nucleo.NucleoResponse;

import org.json.JSONObject;

/**
 * Created by Marco on 11/03/16.
 */
public class NeatoRobot{

    private static final String TAG = "NeatoRobot";

    private Context context;
    //the serializable model
    private Robot robot;

    private String baseUrl;
    @VisibleForTesting
    protected AsyncCall asyncCall;

    //constructor
    public NeatoRobot(Context context, Robot robot){
        this.context = context;
        this.baseUrl = context.getString(R.string.nucleo_endpoint);
        this.asyncCall = new AsyncCall();
        this.robot = robot;
    }

    private void setRobotState(JSONObject json) {
        robot.state = new State(json);
    }

    /**
     * Retrieve the user robots list.
     * @param callback
     */
    public void updateRobotState(final NeatoCallback<Void> callback) {
        JSONObject command = NucleoCommands.get(NucleoCommands.GET_ROBOT_STATE_COMMAND);
        asyncCall.executeCall(this,context, baseUrl, robot.serial,command, robot.secret_key, new NeatoCallback<NucleoResponse>(){
            @Override
            public void done(NucleoResponse result) {
                super.done(result);
                if(result == null) {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }else if(result.isHttpOK()) {
                    callback.done(null);
                }else {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }
            }
        });
    }

    public void startCleaning(String params, final NeatoCallback<Void> callback) {
        JSONObject command = NucleoCommands.get(NucleoCommands.START_CLEANING_COMMAND, params);
        asyncCall.executeCall(this,context, baseUrl, robot.serial,command, robot.secret_key, new NeatoCallback<NucleoResponse>(){
            @Override
            public void done(NucleoResponse result) {
                super.done(result);
                if(result == null) {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }else if(result.isHttpOK()) {
                    callback.done(null);
                }else {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }
            }
        });
    }

    public void pauseCleaning(final NeatoCallback<Void> callback) {
        JSONObject command = NucleoCommands.get(NucleoCommands.PAUSE_CLEANING_COMMAND);
        asyncCall.executeCall(this,context, baseUrl, robot.serial,command, robot.secret_key, new NeatoCallback<NucleoResponse>(){
            @Override
            public void done(NucleoResponse result) {
                super.done(result);
                if(result == null) {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }else if(result.isHttpOK()) {
                    callback.done(null);
                }else {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }
            }
        });
    }

    public void stopCleaning(final NeatoCallback<Void> callback) {
        JSONObject command = NucleoCommands.get(NucleoCommands.STOP_CLEANING_COMMAND);
        asyncCall.executeCall(this,context, baseUrl, robot.serial,command, robot.secret_key, new NeatoCallback<NucleoResponse>(){
            @Override
            public void done(NucleoResponse result) {
                super.done(result);
                if(result == null) {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }else if(result.isHttpOK()) {
                    callback.done(null);
                }else {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }
            }
        });
    }

    public void resumeCleaning(final NeatoCallback<Void> callback) {
        JSONObject command = NucleoCommands.get(NucleoCommands.RESUME_CLEANING_COMMAND);
        asyncCall.executeCall(this,context, baseUrl, robot.serial,command, robot.secret_key, new NeatoCallback<NucleoResponse>(){
            @Override
            public void done(NucleoResponse result) {
                super.done(result);
                if(result == null) {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }else if(result.isHttpOK()) {
                    callback.done(null);
                }else {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }
            }
        });
    }

    public void goToBase(final NeatoCallback<Void> callback) {
        JSONObject command = NucleoCommands.get(NucleoCommands.SEND_TO_BASE_CLEANING_COMMAND);
        asyncCall.executeCall(this,context, baseUrl, robot.serial,command, robot.secret_key, new NeatoCallback<NucleoResponse>(){
            @Override
            public void done(NucleoResponse result) {
                super.done(result);
                if(result == null) {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }else if(result.isHttpOK()) {
                    callback.done(null);
                }else {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }
            }
        });
    }

    //region serialization
    public Object serialize() {
        return robot;
    }

    public static NeatoRobot deserialize(Context context, Object serializable) {
        return new NeatoRobot(context,(Robot) serializable);
    }
    //endregion

    //region robot getters and setters
    public String getName() {
        return robot.name;
    }

    public String getSerial() {
        return robot.serial;
    }

    public String getSecretKey() {
        return robot.secret_key;
    }

    public String getModel() {
        return robot.model;
    }

    public String getLinkedAt() {
        return robot.linkedAt;
    }

    public int getRobotState() {
        if(robot != null && robot.state != null) {
            return robot.state.state;
        }else return -1;
    }

    public int getRobotAction() {
        if(robot != null && robot.state != null) {
            return robot.state.action;
        }else return -1;
    }
    //endregion

    //region async call
    private class AsyncCall {

        private static final String TAG = "AsyncCall";

        public void executeCall(final NeatoRobot neatoRobot, final Context context, final String url, final String robot_serial, final JSONObject command, final String robotSecretKey, final NeatoCallback<NucleoResponse> callback) {
            final AsyncTask<Void, Void, NucleoResponse> task = new AsyncTask<Void, Void, NucleoResponse>() {
                protected void onPreExecute() {}
                protected NucleoResponse doInBackground(Void... unused) {
                    return NucleoBaseClient.executeNucleoCall(url,robot_serial,command,robotSecretKey);
                }
                protected void onPostExecute(NucleoResponse response) {
                    if(response != null && response.isOK()) {
                        if(response.isStateResponse()) neatoRobot.setRobotState(response.getJSON());
                        callback.done(response);
                    }
                    else if(response != null && response.isHttpOK() && !response.isOK()) {
                        //TODO extract the specific error code
                        callback.fail(NeatoError.ROBOT_ERROR);
                    }
                    else {
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
