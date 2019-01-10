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
    public String boundaryId; //Optional. If provided, the ID of the zone to clean.
    public String boundaryName; //Optional. If provided, the name of the zone to clean.

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
            if(json.has("boundary")){
                JSONObject boundary = json.getJSONObject("boundary");
                if(boundary != null && boundary.has("id")) {
                    boundaryId = boundary.getString("id");
                }
                if(boundary != null && boundary.has("name")) {
                    boundaryName = boundary.getString("name");
                }
            }
        }catch(JSONException e) {Log.e(TAG, "Exception", e);}
    }
}