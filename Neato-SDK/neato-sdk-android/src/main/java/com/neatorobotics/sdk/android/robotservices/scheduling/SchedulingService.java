package com.neatorobotics.sdk.android.robotservices.scheduling;

import android.util.Log;

import com.neatorobotics.sdk.android.NeatoCallback;
import com.neatorobotics.sdk.android.NeatoError;
import com.neatorobotics.sdk.android.models.Robot;
import com.neatorobotics.sdk.android.models.RobotState;
import com.neatorobotics.sdk.android.models.ScheduleEvent;
import com.neatorobotics.sdk.android.nucleo.NucleoResponse;
import com.neatorobotics.sdk.android.nucleo.RobotCommands;
import com.neatorobotics.sdk.android.robotservices.RobotService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Neato
 * Created by Marco on 09/01/2017.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */

public abstract class SchedulingService extends RobotService{

    private static final String TAG = "SchedulingService";

    public abstract boolean isEcoModeSupported();
    public abstract boolean isTurboModeSupported();

    public void disableScheduling(Robot robot, HashMap<String, String> params, final NeatoCallback<Void> callback) {
        JSONObject command = RobotCommands.get(RobotCommands.DISABLE_SCHEDULE_COMMAND);
        nucleoClient.sendCommandAsync(robot, command, new NeatoCallback<NucleoResponse>(){
            @Override
            public void done(NucleoResponse result) {
                super.done(result);
                callback.done(null);
            }

            @Override
            public void fail(NeatoError errorCode) {
                super.fail(errorCode);
                callback.fail(errorCode);
            }
        });
    }

    public void enableScheduling(Robot robot, HashMap<String, String> params, final NeatoCallback<Void> callback) {
        JSONObject command = RobotCommands.get(RobotCommands.ENABLE_SCHEDULE_COMMAND);
        nucleoClient.sendCommandAsync(robot, command, new NeatoCallback<NucleoResponse>(){
            @Override
            public void done(NucleoResponse result) {
                super.done(result);
                callback.done(null);
            }

            @Override
            public void fail(NeatoError errorCode) {
                super.fail(errorCode);
                callback.fail(errorCode);
            }
        });
    }

    public void setSchedule(Robot robot, ArrayList<ScheduleEvent> events, final NeatoCallback<Void> callback) {

        JSONArray array = new JSONArray();
        for(int i=0; i<events.size(); i++) {
            try {
                ScheduleEvent event = events.get(i);
                JSONObject jEvent = new JSONObject();
                jEvent.put("startTime", event.startTime);
                if (isEcoModeSupported() && isTurboModeSupported()) {
                    jEvent.put("mode", event.mode);
                }
                jEvent.put("day", event.day);
                array.put(jEvent);
            }catch (JSONException e) {Log.e(TAG, "Exception", e);}
        }

        String eventsParams = array.toString();
        JSONObject command = RobotCommands.get(RobotCommands.SET_SCHEDULE_COMMAND.replace("EVENTS_PLACEHOLDER","events:"+eventsParams));

        nucleoClient.sendCommandAsync(robot, command, new NeatoCallback<NucleoResponse>(){
            @Override
            public void done(NucleoResponse result) {
                super.done(result);
                callback.done(null);
            }

            @Override
            public void fail(NeatoError errorCode) {
                super.fail(errorCode);
                callback.fail(errorCode);
            }
        });
    }

    public void getSchedule(Robot robot, HashMap<String,String> params, final NeatoCallback<ArrayList<ScheduleEvent>> callback) {
        JSONObject command = RobotCommands.get(RobotCommands.GET_ROBOT_SCHEDULE_COMMAND);

        nucleoClient.sendCommandAsync(robot, command, new NeatoCallback<NucleoResponse>(){
            @Override
            public void done(NucleoResponse result) {
                super.done(result);
                RobotState state = new RobotState(result.getJSON());
                ArrayList<ScheduleEvent> events = state.getScheduleEvents();
                callback.done(events);
            }

            @Override
            public void fail(NeatoError errorCode) {
                super.fail(errorCode);
                callback.fail(errorCode);
            }
        });
    }
}
