package com.neatorobotics.sdk.android.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Marco on 04/05/16.
 */
public class State implements Serializable{

    private static final String TAG = "State";

    public enum RobotState {
        INVALID(0),
        IDLE(1),
        BUSY(2),
        PAUSED(3),
        ERROR(4);

        private final int value;
        private RobotState(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    public enum RobotAction {
        HOUSE_CLEANING(1),
        SPOT_CLEANING(2),
        MANUAL_CLEANING(3),
        DOCKING(4),
        MENU_ACTIVE(5),
        SUSPENDED_CLEANING(6),
        UPDATING(7),
        COPYING_LOGS(8),
        RECOVERY_LOCATION(9),
        INVALID(0);

        private final int value;
        private RobotAction(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    public enum RobotCleaningCategory {
        MANUAL(1),
        HOUSE(2),
        SPOT(3);

        private final int value;
        private RobotCleaningCategory(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    public enum RobotCleaningMode {
        ECO(1),
        TURBO(2);

        private final int value;
        private RobotCleaningMode(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    public enum RobotCleaningModifier {
        NORMAL(1),
        DOUBLE(2);

        private final int value;
        private RobotCleaningModifier(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    public enum RobotCleaningSpotSize {
        NORMAL(200),
        DOUBLE(400);

        private final int value;
        private RobotCleaningSpotSize(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    //DETAILS
    public boolean isCharging, isDocked, dockHasBeenSeen, isCleaning, isScheduleEnabled;
    //AVAILABLE COMMANDS
    public boolean startAvailable, stopAvailable, pauseAvailable, resumeAvailable, goToBaseAvailable;
    //CLEANING
    public int cleaningModifier,cleaningMode, cleaningSpotWidth, cleaningSpotHeight;

    //SCHEDULE EVENTS
    public ArrayList<ScheduleEvent> scheduleEvents = new ArrayList<>();

    //AVAILABLE SERVICES
    public HashMap<String, String> availableServices = new HashMap<>();

    public String message;
    public int state;
    public String error;
    public String alert;
    public double charge;
    public int action;
    public String result;

    //Robot remote protocol version
    public int robotRemoteProtocolVersion;

    //Robot model
    public String robotModelName;
    //Firmware
    public String firmware;

    //constructor
    public State(JSONObject json) {
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
                isCleaning = action == RobotAction.HOUSE_CLEANING.value || action == RobotAction.SPOT_CLEANING.value || action == RobotAction.MANUAL_CLEANING.value;
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
}
