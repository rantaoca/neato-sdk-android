package com.neatorobotics.sdk.android.models;

import android.util.Log;

import com.neatorobotics.sdk.android.nucleo.RobotConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */
public class RobotState implements Serializable{

    private static final String TAG = "State";

    //DETAILS
    protected boolean isCharging, isDocked, dockHasBeenSeen, isCleaning, isScheduleEnabled;
    //AVAILABLE COMMANDS
    protected boolean startAvailable, stopAvailable, pauseAvailable, resumeAvailable, goToBaseAvailable;
    //CLEANING
    protected int cleaningModifier,cleaningMode, cleaningSpotWidth, cleaningSpotHeight;

    //SCHEDULE EVENTS
    protected ArrayList<ScheduleEvent> scheduleEvents = new ArrayList<>();

    //AVAILABLE SERVICES
    protected HashMap<String, String> availableServices = new HashMap<>();

    protected String message;
    protected int state;
    protected String error;
    protected String alert;
    protected double charge;
    protected int action;
    protected String result;

    //Robot remote protocol version
    protected int robotRemoteProtocolVersion;

    //Robot model
    protected String robotModelName;
    //Firmware
    protected String firmware;

    //constructor
    public RobotState(JSONObject json) {
        loadData(json);
    }

    private void loadData(JSONObject json) {

        if(json == null) return;//no data

        //root
        try {
            message = json.optString("message", ""); message = message==null?"":message;
            result = json.optString("result", "");
            robotRemoteProtocolVersion = json.optInt("version",-1);
            state = json.optInt("state", -1);
            error = json.optString("error", "");
            alert = json.isNull("alert")?null:json.optString("alert", null);
            if(json.has("action")){
                action = json.optInt("action",-1);
                isCleaning = action == RobotConstants.ROBOT_ACTION_HOUSE_CLEANING ||
                                action == RobotConstants.ROBOT_ACTION_SPOT_CLEANING ||
                                action == RobotConstants.ROBOT_ACTION_MANUAL_CLEANING;
            }

            //details
            if(json.has("details")) {
                JSONObject details = json.getJSONObject("details");

                charge = details.optDouble("charge",-1);
                isCharging = details.optBoolean("isCharging",false);
                isDocked = details.optBoolean("isDocked",false);
                dockHasBeenSeen = details.optBoolean("dockHasBeenSeen", false);
                isScheduleEnabled = details.optBoolean("isScheduleEnabled",false);
            }

            //cleaning
            if(json.has("cleaning")) {
                JSONObject cleaning = json.getJSONObject("cleaning");

                cleaningModifier = cleaning.optInt("modifier", -1);
                cleaningMode = cleaning.optInt("mode", -1);
                cleaningSpotWidth = cleaning.optInt("spotWidth", -1);
                cleaningSpotHeight = cleaning.optInt("spotHeight", -1);
            }

            //available commands
            if(json.has("availableCommands")){
                JSONObject availableCommands = json.getJSONObject("availableCommands");
                startAvailable = availableCommands.optBoolean("start",false);
                stopAvailable = availableCommands.optBoolean("stop", false);
                pauseAvailable = availableCommands.optBoolean("pause", false);
                resumeAvailable = availableCommands.optBoolean("resume", false);
                goToBaseAvailable = availableCommands.optBoolean("goToBase", false);
            }

            //available services
            if(json.has("availableServices")){
                JSONObject availableServicesJSON = json.getJSONObject("availableServices");
                Iterator<String> iterator = availableServicesJSON.keys();
                while (iterator.hasNext()) {
                    String serviceName = iterator.next();
                    availableServices.put(serviceName, availableServicesJSON.optString(serviceName));
                }
            }

            //meta
            if(json.has("meta")){
                JSONObject meta = json.getJSONObject("meta");
                robotModelName = meta.optString("modelName", "");
                firmware = meta.optString("firmware","");
            }

            //schedule events
            scheduleEvents.clear();
            if(json.has("data")){
                JSONObject data = json.getJSONObject("data");
                if(data.has("events")) {
                    JSONArray events = data.getJSONArray("events");
                    for(int e=0; e<events.length(); e++) {
                        ScheduleEvent se = new ScheduleEvent();
                        se.loadData((JSONObject)events.get(e));
                        scheduleEvents.add(se);
                    }
                }
            }
        }catch(JSONException e) {Log.e(TAG,"Exception",e);}
    }

    //region getters

    public boolean isCharging() {
        return isCharging;
    }

    public boolean isDocked() {
        return isDocked;
    }

    public boolean isDockHasBeenSeen() {
        return dockHasBeenSeen;
    }

    public boolean isCleaning() {
        return isCleaning;
    }

    public boolean isScheduleEnabled() {
        return isScheduleEnabled;
    }

    public boolean isStartAvailable() {
        return startAvailable;
    }

    public boolean isStopAvailable() {
        return stopAvailable;
    }

    public boolean isPauseAvailable() {
        return pauseAvailable;
    }

    public boolean isResumeAvailable() {
        return resumeAvailable;
    }

    public boolean isGoToBaseAvailable() {
        return goToBaseAvailable;
    }

    public int getCleaningModifier() {
        return cleaningModifier;
    }

    public int getCleaningMode() {
        return cleaningMode;
    }

    public int getCleaningSpotWidth() {
        return cleaningSpotWidth;
    }

    public int getCleaningSpotHeight() {
        return cleaningSpotHeight;
    }

    public ArrayList<ScheduleEvent> getScheduleEvents() {
        return scheduleEvents;
    }

    public HashMap<String, String> getAvailableServices() {
        return availableServices;
    }

    public String getMessage() {
        return message;
    }

    public int getState() {
        return state;
    }

    public String getError() {
        return error;
    }

    public String getAlert() {
        return alert;
    }

    public double getCharge() {
        return charge;
    }

    public int getAction() {
        return action;
    }

    public String getResult() {
        return result;
    }

    public int getRobotRemoteProtocolVersion() {
        return robotRemoteProtocolVersion;
    }

    public String getRobotModelName() {
        return robotModelName;
    }

    public String getFirmware() {
        return firmware;
    }

    //endregion
}
