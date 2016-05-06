package com.neatorobotics.sdk.android.models;

import android.util.Log;

import com.neatorobotics.sdk.android.utils.RobotVersionComparator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */
public class Robot implements Serializable{

    private static final String TAG = "Robot";

    public String serial, prefix, name, model, model_readable, firmware, tz_info, created_at, provisioned_at,secret_key;
    public double lat, lon;
    public String linkedAt;

    public RobotState state;
    public HashMap<String, RobotFirmware> recentFirmware = new HashMap<>();

    //region .ctor
    public Robot(JSONObject json){
        if(json != null) {
            this.serial = json.optString("serial");
            this.prefix = json.optString("prefix");
            this.name = json.optString("name");
            this.model = json.optString("model");
            this.model_readable = json.optString("model_readable");
            this.firmware = json.optString("firmware");
            this.tz_info = json.optString("tz_info");
            this.created_at = json.optString("created_at");
            this.provisioned_at = json.optString("provisioned_at");
            this.lat = json.optDouble("lat", -1);
            this.lon = json.optDouble("lon",-1);
            this.linkedAt = json.optString("linked_at");
            this.secret_key = json.optString("secret_key");
        }
    }
    //endregion

    //region service discovery
    public boolean hasService(String serviceName) {
        if(this.state == null) return false;
        if(this.state.availableServices == null) return false;
        if(this.state.availableServices.containsKey(serviceName)) return true;
        else return false;
    }

    public boolean hasService(String serviceName, String serviceVersion) {
        if(this.state == null) return false;
        if(this.state.availableServices == null) return false;
        if(this.state.availableServices.containsKey(serviceName)) {
            String version = this.state.availableServices.get(serviceName);
            if(version.equalsIgnoreCase(serviceVersion)) return true;
            else return false;
        }
        else return false;
    }
    //endregion

    //region robot update stuff
    public void setRecentFirmwares(JSONObject jsonFirmwares){
        if(jsonFirmwares != null) {
            Iterator<String> modelIterator = jsonFirmwares.keys();
            while(modelIterator.hasNext()) {
                try {
                    String model = modelIterator.next();
                    String version = jsonFirmwares.getJSONObject(model).optString("version", "");
                    String url = jsonFirmwares.getJSONObject(model).optString("url", "");
                    String changelogurl = jsonFirmwares.getJSONObject(model).optString("changelog_url", "");
                    int size = jsonFirmwares.getJSONObject(model).optInt("filesize", 0);
                    recentFirmware.put(model.toLowerCase(),new RobotFirmware(model,version,url, size,changelogurl));
                } catch (JSONException e){
                    Log.e(TAG,"Exception",e);}
            }
        }
    }

    public RobotFirmware getLatestAvailableFirmware() {
        if(state != null && state.robotModelName != null && state.firmware != null) {
            String model = state.robotModelName.toLowerCase();
            if(recentFirmware.containsKey(model)) {
                return recentFirmware.get(model);
            }
        }
        return null;
    }

    public boolean isUpdateAvailable() {
        if(state != null && state.robotModelName != null && state.firmware != null) {
            String model = state.robotModelName.toLowerCase();
            if(recentFirmware.containsKey(model)) {
                try {
                    if (RobotVersionComparator.versionCompare(state.firmware, recentFirmware.get(model).version) < 0) {
                        return true;
                    }
                }catch (NumberFormatException e) {
                    Log.e(TAG,"Exception",e);
                }
            }
        }
        return false;
    }
    //endregion
}
