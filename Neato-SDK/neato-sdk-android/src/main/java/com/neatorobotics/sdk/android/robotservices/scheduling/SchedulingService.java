package com.neatorobotics.sdk.android.robotservices.scheduling;

import android.util.Log;

import com.neatorobotics.sdk.android.models.ScheduleEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Neato
 * Created by Marco on 09/01/2017.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */

public abstract class SchedulingService {

    private static final String TAG = "SchedulingService";

    public abstract boolean isEcoModeSupported();
    public abstract boolean isTurboModeSupported();

    public String convertEventsToJSON(ArrayList<ScheduleEvent> events) {
        JSONArray array = new JSONArray();
        for(int i=0; i<events.size(); i++) {
            array.put(convertEventToJSON(events.get(i)));
        }
        return array.toString();
    }

    public JSONObject convertEventToJSON(ScheduleEvent event) {
        JSONObject json = new JSONObject();
        try {
            json.put("startTime",event.startTime);
            if(isEcoModeSupported() && isTurboModeSupported()) {
                json.put("mode", event.mode);
            }
            json.put("day",event.day);
        }catch(JSONException e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
        return json;
    }
}
