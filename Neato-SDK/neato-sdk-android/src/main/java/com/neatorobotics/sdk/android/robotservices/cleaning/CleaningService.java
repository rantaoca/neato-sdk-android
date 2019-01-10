package com.neatorobotics.sdk.android.robotservices.cleaning;

import android.util.Log;

import com.neatorobotics.sdk.android.NeatoCallback;
import com.neatorobotics.sdk.android.NeatoError;
import com.neatorobotics.sdk.android.models.Robot;
import com.neatorobotics.sdk.android.models.RobotState;
import com.neatorobotics.sdk.android.nucleo.NucleoResponse;
import com.neatorobotics.sdk.android.nucleo.RobotConstants;
import com.neatorobotics.sdk.android.robotservices.RobotService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Neato
 * Created by Marco on 05/01/2017.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */

public abstract class CleaningService extends RobotService {

    private static final String TAG = "CleaningService";

    protected int cleaningType = RobotConstants.ROBOT_CLEANING_CATEGORY_HOUSE;

    public abstract boolean isEcoModeSupported();
    public abstract boolean isTurboModeSupported();
    public abstract boolean isExtraCareModeSupported();
    public abstract boolean isCleaningAreaSupported();
    public abstract boolean isCleaningFrequencySupported();
    public abstract boolean isFloorPlanSupported();
    public abstract boolean areZonesSupported();

    public void stopCleaning(final Robot robot, HashMap<String,String> params, final NeatoCallback<RobotState> callback) {
        JSONObject command = new JSONObject();
        try {
            command.put("reqId", "77");
            command.put("cmd", "stopCleaning");
        }catch (JSONException e) {
            Log.e(TAG, "Exception", e);
        }

        nucleoClient.sendCommandAsync(robot, command, new NeatoCallback<NucleoResponse>(){
            @Override
            public void done(NucleoResponse result) {
                super.done(result);
                RobotState robotState = new RobotState(result.getJSON());
                callback.done(robotState);
            }

            @Override
            public void fail(NeatoError error) {
                super.fail(error);
                callback.fail(error);
            }
        });
    }

    public void pauseCleaning(final Robot robot, HashMap<String,String> params, final NeatoCallback<RobotState> callback) {
        JSONObject command = new JSONObject();
        try {
            command.put("reqId", "77");
            command.put("cmd", "pauseCleaning");
        }catch (JSONException e) {
            Log.e(TAG, "Exception", e);
        }

        nucleoClient.sendCommandAsync(robot, command, new NeatoCallback<NucleoResponse>(){
            @Override
            public void done(NucleoResponse result) {
                super.done(result);
                RobotState robotState = new RobotState(result.getJSON());
                callback.done(robotState);
            }

            @Override
            public void fail(NeatoError errorCode) {
                super.fail(errorCode);
                callback.fail(errorCode);
            }
        });
    }

    public void resumeCleaning(final Robot robot, HashMap<String,String> params, final NeatoCallback<RobotState> callback) {
        JSONObject command = new JSONObject();
        try {
            command.put("reqId", "77");
            command.put("cmd", "resumeCleaning");
        }catch (JSONException e) {
            Log.e(TAG, "Exception", e);
        }

        nucleoClient.sendCommandAsync(robot, command, new NeatoCallback<NucleoResponse>(){
            @Override
            public void done(NucleoResponse result) {
                super.done(result);
                RobotState robotState = new RobotState(result.getJSON());
                callback.done(robotState);
            }

            @Override
            public void fail(NeatoError errorCode) {
                super.fail(errorCode);
                callback.fail(errorCode);
            }
        });
    }

    public void returnToBase(final Robot robot, HashMap<String,String> params, final NeatoCallback<RobotState> callback) {
        JSONObject command = new JSONObject();
        try {
            command.put("reqId", "77");
            command.put("cmd", "sendToBase");
        }catch (JSONException e) {
            Log.e(TAG, "Exception", e);
        }

        nucleoClient.sendCommandAsync(robot, command, new NeatoCallback<NucleoResponse>(){
            @Override
            public void done(NucleoResponse result) {
                super.done(result);
                RobotState robotState = new RobotState(result.getJSON());
                callback.done(robotState);
            }

            @Override
            public void fail(NeatoError errorCode) {
                super.fail(errorCode);
                callback.fail(errorCode);
            }
        });
    }

    public void startCleaning(final Robot robot, HashMap<String, String> params, final NeatoCallback<RobotState> callback) {
        // default values
        int cleaningMode = RobotConstants.ROBOT_CLEANING_MODE_TURBO;

        int cleaningModifier = RobotConstants.ROBOT_CLEANING_MODIFIER_NORMAL;
        int cleaningNavigationMode = RobotConstants.ROBOT_EXTRA_CARE_MODE_OFF;
        int cleaningAreaWidth = RobotConstants.ROBOT_CLEANING_SPOT_SIZE_LARGE;
        int cleaningAreaHeight = RobotConstants.ROBOT_CLEANING_SPOT_SIZE_LARGE;

        // override default values, with user values
        // discard unsupported values
        if(params != null) {
            if(params.containsKey(RobotConstants.CLEANING_MODE_KEY)) {
                cleaningMode = Integer.parseInt(params.get(RobotConstants.CLEANING_MODE_KEY));
            }
            if(params.containsKey(RobotConstants.CLEANING_MODIFIER_KEY)) {
                cleaningModifier = Integer.parseInt(params.get(RobotConstants.CLEANING_MODIFIER_KEY));
            }
            if(params.containsKey(RobotConstants.CLEANING_NAVIGATION_MODE_KEY)) {
                cleaningNavigationMode = Integer.parseInt(params.get(RobotConstants.CLEANING_NAVIGATION_MODE_KEY));
            }
            if(params.containsKey(RobotConstants.CLEANING_AREA_SPOT_HEIGHT_KEY)) {
                cleaningAreaHeight = Integer.parseInt(params.get(RobotConstants.CLEANING_AREA_SPOT_HEIGHT_KEY));
            }
            if(params.containsKey(RobotConstants.CLEANING_AREA_SPOT_WIDTH_KEY)) {
                cleaningAreaWidth = Integer.parseInt(params.get(RobotConstants.CLEANING_AREA_SPOT_WIDTH_KEY));
            }
        }

        JSONObject command = new JSONObject();
        try {
            command.put("reqId", "77");
            command.put("cmd", "startCleaning");

            JSONObject commandParams = new JSONObject();
            commandParams.put(RobotConstants.CLEANING_CATEGORY_KEY, cleaningType);
            if(isEcoModeSupported() && isTurboModeSupported()) {
                commandParams.put(RobotConstants.CLEANING_MODE_KEY, cleaningMode);
            }
            if(isCleaningFrequencySupported()) {
                commandParams.put(RobotConstants.CLEANING_MODIFIER_KEY, cleaningModifier);
            }
            if(isExtraCareModeSupported()) {
                commandParams.put(RobotConstants.CLEANING_NAVIGATION_MODE_KEY, cleaningNavigationMode);
            }
            if(isCleaningAreaSupported()) {
                commandParams.put(RobotConstants.CLEANING_AREA_SPOT_HEIGHT_KEY, cleaningAreaHeight);
                commandParams.put(RobotConstants.CLEANING_AREA_SPOT_WIDTH_KEY, cleaningAreaWidth);
            }
            // check if floor plan cleaning is supported
            // and if we have some floor plan or zones related params
            if(isFloorPlanSupported()) {
                if(params.containsKey(RobotConstants.CLEANING_TYPE_KEY)) {
                    cleaningType = Integer.parseInt(params.get(RobotConstants.CLEANING_TYPE_KEY));
                    commandParams.put(RobotConstants.CLEANING_CATEGORY_KEY, cleaningType);// override house with floor plan if the case
                }
            }
            if(areZonesSupported()) {
                if(params.containsKey(RobotConstants.CLEANING_ZONE_KEY)) {
                    String boundaryId = params.get(RobotConstants.CLEANING_ZONE_KEY);
                    commandParams.put(RobotConstants.CLEANING_ZONE_KEY, boundaryId);
                }
            }
            command.put("params", commandParams);
        }catch (JSONException e) {
            Log.e(TAG, "Exception", e);
        }

        nucleoClient.sendCommandAsync(robot, command, new NeatoCallback<NucleoResponse>(){
            @Override
            public void done(NucleoResponse result) {
                super.done(result);
                RobotState robotState = new RobotState(result.getJSON());
                callback.done(robotState);
            }

            @Override
            public void fail(NeatoError errorCode) {
                super.fail(errorCode);
                callback.fail(errorCode);
            }
        });
    }
}
