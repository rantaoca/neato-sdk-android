package com.neatorobotics.sdk.android.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */
public class ScheduleEvent implements Serializable {

    private static final String TAG = "ScheduleEvent";

    //public String eventId = "0";
    public String startTime;//24hours format
    public int mode, day;//0 is sunday

    public void loadData(JSONObject json) {
        if(json == null) return;
        try {
            if(json.has("startTime") && !json.isNull("startTime")){
                startTime = json.getString("startTime");
            }
            if(json.has("mode") && !json.isNull("mode")){
                mode = json.getInt("mode");
            }else mode = -1;
            if(json.has("day") && !json.isNull("day")){
                day = json.getInt("day");
            }
        }catch(JSONException e) {Log.e(TAG, "Exception", e);}
    }

    public JSONObject getJSON(String scheduleVersion) {
        JSONObject json = new JSONObject();
        try {
            //json.put("eventId",eventId);
            json.put("startTime",startTime);
            if(!scheduleVersion.equalsIgnoreCase("minimal-1")) {
                json.put("mode", mode);
            }
            json.put("day",day);
        }catch(JSONException e) {Log.e(TAG, "Exception", e);return null;}
        return json;
    }
}