package com.neatorobotics.sdk.android.utils;

import com.neatorobotics.sdk.android.models.ScheduleEvent;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Neato-SDK
 * Created by Marco on 01/06/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */
public class SchedulingUtils {
    public static String getEventsJSON(ArrayList<ScheduleEvent> events, String scheduleVersion) {
        JSONArray array = new JSONArray();
        for(int i=0; i<events.size(); i++) {
            array.put(events.get(i).getJSON(scheduleVersion));
        }
        return array.toString();
    }
}
