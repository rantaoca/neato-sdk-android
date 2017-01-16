package com.neatorobotics.sdk.android;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.neatorobotics.sdk.android.models.Robot;
import com.neatorobotics.sdk.android.models.RobotState;
import com.neatorobotics.sdk.android.models.ScheduleEvent;
import com.neatorobotics.sdk.android.nucleo.Nucleo;
import com.neatorobotics.sdk.android.nucleo.NucleoBaseClient;
import com.neatorobotics.sdk.android.nucleo.RobotCommands;
import com.neatorobotics.sdk.android.nucleo.NucleoResponse;
import com.neatorobotics.sdk.android.robotservices.cleaning.CleaningService;
import com.neatorobotics.sdk.android.robotservices.housecleaning.HouseCleaningService;
import com.neatorobotics.sdk.android.robotservices.housecleaning.HouseCleaningServiceFactory;
import com.neatorobotics.sdk.android.robotservices.scheduling.SchedulingService;
import com.neatorobotics.sdk.android.robotservices.scheduling.SchedulingServiceFactory;
import com.neatorobotics.sdk.android.robotservices.spotcleaning.SpotCleaningService;
import com.neatorobotics.sdk.android.robotservices.spotcleaning.SpotCleaningServiceFactory;
import com.neatorobotics.sdk.android.utils.SchedulingUtils;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
        this.baseUrl = Nucleo.URL;
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
     * Start House Cleaning
     * @param params
     * @param callback
     */
    public void startHouseCleaning(HashMap<String, String> params, NeatoCallback<RobotState> callback){
        HouseCleaningService hcs = getHouseCleaningService();
        if(hcs != null) {
            hcs.startCleaning(this.robot, params, callback);
        }else callback.fail(NeatoError.GENERIC_ERROR);
    }

    /**
     * Start Spot Cleaning
     * @param params
     * @param callback
     */
    public void startSpotCleaning(HashMap<String, String> params, NeatoCallback<RobotState> callback){
        SpotCleaningService hcs = getSpotCleaningService();
        if(hcs != null) {
            hcs.startCleaning(this.robot, params, callback);
        }else callback.fail(NeatoError.GENERIC_ERROR);
    }

    /**
     * Pause the robot cleaning.
     * @param callback
     */
    public void pauseCleaning(final NeatoCallback<RobotState> callback) {
        CleaningService hcs = getCleaningService();
        if(hcs != null) {
            hcs.pauseCleaning(this.robot, null, callback);
        }else callback.fail(NeatoError.GENERIC_ERROR);
    }

    /**
     * Stop the robot cleaning.
     * @param callback
     */
    public void stopCleaning(final NeatoCallback<RobotState> callback) {
        CleaningService hcs = getCleaningService();
        if(hcs != null) {
            hcs.stopCleaning(this.robot, null, callback);
        }else callback.fail(NeatoError.GENERIC_ERROR);
    }

    /**
     * Resume the robot cleaning.
     * @param callback
     */
    public void resumeCleaning(final NeatoCallback<RobotState> callback) {
        CleaningService hcs = getCleaningService();
        if(hcs != null) {
            hcs.resumeCleaning(this.robot, null, callback);
        }else callback.fail(NeatoError.GENERIC_ERROR);
    }

    /**
     * Return the robot to is charging base.
     * @param callback
     */
    public void goToBase(final NeatoCallback<RobotState> callback) {
        CleaningService hcs = getCleaningService();
        if(hcs != null) {
            hcs.returnToBase(this.robot, null, callback);
        }else callback.fail(NeatoError.GENERIC_ERROR);
    }

    /**
     * Find the robot (the robot should beep)
     * @param callback
     */
    public void findMe(final NeatoCallback<Void> callback) {
        JSONObject command = RobotCommands.get(RobotCommands.FIND_ME_COMMAND);
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
     * Enable scheduling on the robot
     * @param callback
     */
    public void enableScheduling(final NeatoCallback<Void> callback) {
        JSONObject command = RobotCommands.get(RobotCommands.ENABLE_SCHEDULE_COMMAND);
        asyncCall.executeCall(this,context, baseUrl, robot.serial,command, robot.secret_key, new NeatoCallback<NucleoResponse>(){
            @Override
            public void done(NucleoResponse result) {
                super.done(result);
                if(result == null) {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }else if(result.isHttpOK()) {
                    updateRobotState(new NeatoCallback<Void>(){
                        @Override
                        public void done(Void result) {
                            super.done(result);
                            callback.done(null);
                        }

                        @Override
                        public void fail(NeatoError error) {
                            super.fail(error);
                            callback.fail(NeatoError.GENERIC_ERROR);
                        }
                    });
                }else {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }
            }
        });
    }

    /**
     * Disable scheduling on the robot
     * @param callback
     */
    public void disableScheduling(final NeatoCallback<Void> callback) {
        JSONObject command = RobotCommands.get(RobotCommands.DISABLE_SCHEDULE_COMMAND);
        asyncCall.executeCall(this,context, baseUrl, robot.serial,command, robot.secret_key, new NeatoCallback<NucleoResponse>(){
            @Override
            public void done(NucleoResponse result) {
                super.done(result);
                if(result == null) {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }else if(result.isHttpOK()) {
                    updateRobotState(new NeatoCallback<Void>(){
                        @Override
                        public void done(Void result) {
                            super.done(result);
                            callback.done(null);
                        }

                        @Override
                        public void fail(NeatoError error) {
                            super.fail(error);
                            callback.fail(NeatoError.GENERIC_ERROR);
                        }
                    });
                }else {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }
            }
        });
    }

    /**
     * Set the robot complete scheduling program.
     * This override the previous scheduling program. You cannot add incremental schedule, but all
     * the schedule events in one time.
     * @param events
     * @param callback
     */
    public void setSchedule(ArrayList<ScheduleEvent> events, final NeatoCallback<Void> callback) {
        String eventsParams = SchedulingUtils.getEventsJSON(events,getState().getAvailableServices().get("schedule"));
        JSONObject command = RobotCommands.get(RobotCommands.SET_SCHEDULE_COMMAND.replace("EVENTS_PLACEHOLDER","events:"+eventsParams));
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
     * Return the robot complete scheduling program.
     * @param callback
     */
    public void getSchedule(final NeatoCallback<ArrayList<ScheduleEvent>> callback) {
        JSONObject command = RobotCommands.get(RobotCommands.GET_ROBOT_SCHEDULE_COMMAND);
        asyncCall.executeCall(this,context, baseUrl, robot.serial,command, robot.secret_key, new NeatoCallback<NucleoResponse>(){
            @Override
            public void done(NucleoResponse result) {
                super.done(result);
                if(result == null) {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }else if(result.isHttpOK()) {
                    RobotState state = new RobotState(result.getJSON());
                    ArrayList<ScheduleEvent> events = state.getScheduleEvents();
                    callback.done(events);
                }else {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }
            }
        });
    }

    /**
     * Return the robot maps list.
     * @param callback
     */
    public void getMaps(final NeatoCallback<JSONObject> callback) {
        NeatoUser.getInstance(context).getMaps(robot.serial, new NeatoCallback<JSONObject>() {
            @Override
            public void done(JSONObject result) {
                super.done(result);
                callback.done(result);
            }

            @Override
            public void fail(NeatoError error) {
                super.fail(error);
                callback.fail(error);
            }
        });
    }

    /**
     * Return the robot map details.
     * @param mapId the ID of the map to retrieve the details
     * @param callback
     */
    public void getMapDetails(final String mapId, final NeatoCallback<JSONObject> callback) {
        NeatoUser.getInstance(context).getMap(robot.serial, mapId, new NeatoCallback<JSONObject>() {
            @Override
            public void done(JSONObject result) {
                super.done(result);
                callback.done(result);
            }

            @Override
            public void fail(NeatoError error) {
                super.fail(error);
                callback.fail(error);
            }
        });
    }

    /**
     * Execute a raw robot call
     *
     * This method exists if the developer need to invoke commands not yet implemented into the SDK.
     * @param command the full JSON input command to send to the robot
     * @param callback the full JSON response from the robot
     */
    public void executeRobotCall(JSONObject command, final NeatoCallback<JSONObject> callback) {
        asyncCall.executeCall(this,context, baseUrl, robot.serial,command, robot.secret_key, new NeatoCallback<NucleoResponse>(){
            @Override
            public void done(NucleoResponse result) {
                super.done(result);
                if(result == null) {
                    callback.fail(NeatoError.GENERIC_ERROR);
                }else if(result.isHttpOK()) {
                    callback.done(result.getJSON());
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
    public Serializable serialize() {
        return robot;
    }

    /**
     *
     * @param context
     * @param serializable the previously Serializable object obtained from the serialize method.
     * @return a state restored NeatoRobot instance ready to be used.
     */
    public static NeatoRobot deserialize(Context context, Serializable serializable) {
        return new NeatoRobot(context,(Robot) serializable);
    }
    //endregion

    //region available services
    /**
     *
     * @param serviceName
     * @return true if the robot support this service, any version
     */
    public boolean hasService(String serviceName) {
        return robot.hasService(serviceName);
    }

    /**
     *
     * @param serviceName
     * @param serviceVersion
     * @return true if the robot support this specific service version
     */
    public boolean hasService(String serviceName, String serviceVersion) {
        return robot.hasService(serviceName,serviceVersion);
    }

    /**
     *
     * @param serviceName
     * @return serviceVersion if exists otherwise null
     */
    public String getServiceVersion(String serviceName) {
        return robot.getServiceVersion(serviceName);
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
                    if(response != null  && response.getJSON() != null && response.isOK()) {
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

    // region supported services
    public CleaningService getCleaningService() {
        return new CleaningService() {
            @Override
            public boolean isEcoModeSupported() {
                return false;
            }

            @Override
            public boolean isTurboModeSupported() {
                return false;
            }

            @Override
            public boolean isExtraCareModeSupported() {
                return false;
            }

            @Override
            public boolean isCleaningAreaSupported() {
                return false;
            }

            @Override
            public boolean isCleaningFrequencySupported() {
                return false;
            }
        };
    }

    public HouseCleaningService getHouseCleaningService() {
        if(this.robot.state == null) return null;//offline or no state available yet
        if(this.hasService("houseCleaning")) {
            return HouseCleaningServiceFactory.get(this.robot.getServiceVersion("houseCleaning"));
        }else return null;//service not supported
    }

    public SpotCleaningService getSpotCleaningService() {
        if(this.robot.state == null) return null;//offline or no state available yet
        if(this.hasService("spotCleaning")) {
            return SpotCleaningServiceFactory.get(this.robot.getServiceVersion("spotCleaning"));
        }else return null;//service not supported
    }

    public SchedulingService getSchedulingService() {
        if(this.robot.state == null) return null;//offline or no state available yet
        if(this.hasService("schedule")) {
            return SchedulingServiceFactory.get(this.robot.getServiceVersion("schedule"));
        }else return null;//service not supported
    }
    // endregion
}
