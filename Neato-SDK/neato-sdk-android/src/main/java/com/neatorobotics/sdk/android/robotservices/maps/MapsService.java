package com.neatorobotics.sdk.android.robotservices.maps;

import android.util.Log;

import com.neatorobotics.sdk.android.NeatoCallback;
import com.neatorobotics.sdk.android.NeatoError;
import com.neatorobotics.sdk.android.NeatoUser;
import com.neatorobotics.sdk.android.models.Boundary;
import com.neatorobotics.sdk.android.models.CleaningMap;
import com.neatorobotics.sdk.android.models.PersistentMap;
import com.neatorobotics.sdk.android.models.Robot;
import com.neatorobotics.sdk.android.robotservices.RobotService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Neato
 * Created by Marco on 08/01/2019.
 * Copyright © 2019 Neato Robotics. All rights reserved.
 */

public abstract class MapsService extends RobotService {

    private static final String TAG = "MapsService";

    // public abstract void startPersistentMapExploration(Robot robot, NeatoCallback<Boolean> callback);

    // public abstract void getMapBoundaries(Robot robot, String mapId, NeatoCallback<List<Boundary>> callback);

    // public abstract void setMapBoundaries(Robot robot, String mapId, List<Boundary> boundaries, NeatoCallback<List<Boundary>> callback);

    public abstract boolean isFloorPlanSupported();

    // public abstract boolean areZonesSupported();

    // public abstract boolean areMultipleFloorPlanSupported();

    public void getPersistentMaps(NeatoUser user, Robot robot, final NeatoCallback<List<PersistentMap>> callback) {
        if(isFloorPlanSupported()) {
            user.getPersistentMaps(robot.serial, new NeatoCallback<JSONObject>() {
                @Override
                public void done(JSONObject result) {
                    super.done(result);
                    callback.done(parsePersistentMaps(result));
                }

                @Override
                public void fail(NeatoError error) {
                    super.fail(error);
                    callback.fail(error);
                }
            });
        }else callback.fail(NeatoError.SERVICE_NOT_SUPPORTED);
    }

    public void deletePersistentMap(NeatoUser user, Robot robot, String mapId, final NeatoCallback<Void> callback) {
        if(isFloorPlanSupported()) {
            user.deletePersistentMap(robot.serial, mapId, new NeatoCallback<JSONObject>() {
                @Override
                public void done(JSONObject result) {
                    super.done(result);
                    callback.done(null);
                }

                @Override
                public void fail(NeatoError error) {
                    super.fail(error);
                    callback.fail(error);
                }
            });
        }else callback.fail(NeatoError.SERVICE_NOT_SUPPORTED);
    }

    public void getCleaningMaps(NeatoUser user, Robot robot, final NeatoCallback<List<CleaningMap>> callback) {
        user.getMaps(robot.serial, new NeatoCallback<JSONObject>() {
            @Override
            public void done(JSONObject result) {
                super.done(result);
                callback.done(parseCleaningMaps(result));
            }

            @Override
            public void fail(NeatoError error) {
                super.fail(error);
                callback.fail(error);
            }
        });
    }

    public void getCleaningMapDetails(NeatoUser user, Robot robot, String mapId, final NeatoCallback<CleaningMap> callback) {
        user.getMap(robot.serial, mapId, new NeatoCallback<JSONObject>() {
            @Override
            public void done(JSONObject result) {
                super.done(result);
                callback.done(parseCleaningMap(result));
            }

            @Override
            public void fail(NeatoError error) {
                super.fail(error);
                callback.fail(error);
            }
        });
    }

    // region utility methods

    private List<PersistentMap> parsePersistentMaps(JSONObject json) {
        List<PersistentMap> maps = new ArrayList<>();
        if(json != null) {
            try {
                JSONArray list = json.getJSONArray("value");
                for (int i = 0; i < list.length(); i++) {
                    JSONObject mapJson = list.getJSONObject(i);
                    maps.add(parsePersistentMap(mapJson));
                }
            } catch (JSONException e) {
                Log.e(TAG, "Exception", e);
            }
        }
        return maps;
    }

    private PersistentMap parsePersistentMap(JSONObject json) {
        PersistentMap map = new PersistentMap();
        map.setName(json.optString("name"));
        map.setId(json.optString("id"));
        map.setUrl(json.optString("url"));
        map.setRawMapUrl(json.optString("raw_floor_map_url"));
        //compute the expiration date
        int urlValidForSeconds = json.optInt("url_valid_for_seconds", 300);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, urlValidForSeconds);
        map.setExpireAt(calendar.getTime());
        return map;
    }


    private CleaningMap parseCleaningMap(JSONObject json) {
        CleaningMap item = new CleaningMap();

        item.setUrl(json.optString("url", ""));
        item.setId(json.optString("id", ""));
        item.setStartAt(json.optString("start_at", ""));
        item.setEndAt(json.optString("end_at", ""));
        item.setError(json.optString("error", ""));
        item.setDocked(json.optBoolean("is_docked", false));
        item.setDelocalized(json.optBoolean("delocalized", false));
        item.setArea(json.optDouble("cleaned_area", 0));
        item.setChargingTimeSeconds(json.optDouble("time_in_suspended_cleaning", 0));
        item.setTimeInError(json.optDouble("time_in_error", 0));
        item.setTimeInPause(json.optDouble("time_in_pause", 0));
        //compute the expiration date
        int urlValidForSeconds = json.optInt("url_valid_for_seconds",300);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, urlValidForSeconds);
        item.setUrlExpiresAt(calendar.getTime());

        return item;
    }

    private List<CleaningMap> parseCleaningMaps(JSONObject json) {
        ArrayList<CleaningMap> maps = new ArrayList<>();
        try {
            JSONArray arr = json.getJSONArray("maps");
            for (int i = 0; i < arr.length(); i++) {
                CleaningMap item = parseCleaningMap(arr.getJSONObject(i));
                maps.add(item);
            }
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }
        return maps;
    }

    //endregion utility methods
}
