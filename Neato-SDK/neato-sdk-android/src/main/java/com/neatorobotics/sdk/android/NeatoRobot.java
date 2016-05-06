package com.neatorobotics.sdk.android;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.neatorobotics.sdk.android.models.Robot;
import com.neatorobotics.sdk.android.models.RobotState;
import com.neatorobotics.sdk.android.nucleo.NucleoBaseClient;
import com.neatorobotics.sdk.android.nucleo.RobotCommands;
import com.neatorobotics.sdk.android.nucleo.NucleoResponse;

import org.json.JSONObject;

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 *
 * This class represents a Robot and must be used to invoke commands. You can obtains your NeatoRobot
 * from the NeatoUser.loadRobots method.
 */
public class NeatoRobot{

    private static final String TAG = "NeatoRobot";

    private Context context;
    //the serializable model
    //use it to save and restore the NeatoRobot state
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

    //this method is automatically invoked when a Nucleo call contains the robot state, so the
    //developer can immediatly use the updated robot state into the done() callbacks.
    private void setRobotState(JSONObject json) {
        robot.state = new RobotState(json);
    }

    /**
     * Update the robot state.
     * @param callback
     */
    public void updateRobotState(final NeatoCallback<Void> callback) {
        JSONObject command = RobotCommands.get(RobotCommands.GET_ROBOT_STATE_COMMAND);
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

    /**
     * Start the robot cleaning.
     * @param params the JSON "params" property of the input commands.
     * @param callback
     */
    public void startCleaning(String params, final NeatoCallback<Void> callback) {
        JSONObject command = RobotCommands.get(RobotCommands.START_CLEANING_COMMAND, params);
        if(command == null) {
            callback.fail(NeatoError.INVALID_JSON);
            return;
        }
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

    /**
     * Pause the robot cleaning.
     * @param callback
     */
    public void pauseCleaning(final NeatoCallback<Void> callback) {
        JSONObject command = RobotCommands.get(RobotCommands.PAUSE_CLEANING_COMMAND);
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

    /**
     * Stop the robot cleaning.
     * @param callback
     */
    public void stopCleaning(final NeatoCallback<Void> callback) {
        JSONObject command = RobotCommands.get(RobotCommands.STOP_CLEANING_COMMAND);
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

    /**
     * Resume the robot cleaning.
     * @param callback
     */
    public void resumeCleaning(final NeatoCallback<Void> callback) {
        JSONObject command = RobotCommands.get(RobotCommands.RESUME_CLEANING_COMMAND);
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

    /**
     * Return the robot to is charging base.
     * @param callback
     */
    public void goToBase(final NeatoCallback<Void> callback) {
        JSONObject command = RobotCommands.get(RobotCommands.SEND_TO_BASE_CLEANING_COMMAND);
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
    /**
     * Obtain a serializable version of this robot.
     * You can deserialize it with the deserialize method.
     * @return a Serializable object in order to eventually store this robot.
     */
    public Object serialize() {
        return robot;
    }

    /**
     *
     * @param context
     * @param serializable the previously Serializable object obtained from the serialize method.
     * @return a state restored NeatoRobot instance ready to be used.
     */
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

    @Nullable
    public RobotState getState() {
        if(robot!=null) return robot.state;
        else return null;
    }
    //endregion

    //region async call
    private class AsyncCall {
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
