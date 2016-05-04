package com.neatorobotics.sdk.android.nucleo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Marco on 04/05/16.
 */
public class NucleoCommands {
    //commands
    public static String START_CLEANING_COMMAND =           "{\"reqId\": \"77\",\"cmd\": \"startCleaning\",\"params\": {\"category\": 2,\"mode\": ECO_MODE_PLACEHOLDER,\"modifier\": 1}}";
    public static String PAUSE_CLEANING_COMMAND =           "{\"reqId\": \"77\",\"cmd\": \"pauseCleaning\"}";
    public static String RESUME_CLEANING_COMMAND =          "{\"reqId\": \"77\",\"cmd\": \"resumeCleaning\"}";
    public static String STOP_CLEANING_COMMAND =            "{\"reqId\": \"77\",\"cmd\": \"stopCleaning\"}";
    public static String SEND_TO_BASE_CLEANING_COMMAND =    "{\"reqId\": \"77\",\"cmd\": \"sendToBase\"}";
    public static String GET_ROBOT_STATE_COMMAND =          "{\"reqId\": \"77\",\"cmd\": \"getRobotState\"}";
    public static String ENABLE_SCHEDULE_COMMAND =          "{\"reqId\": \"77\",\"cmd\": \"enableSchedule\"}";
    public static String DISABLE_SCHEDULE_COMMAND =         "{\"reqId\": \"77\",\"cmd\": \"disableSchedule\"}";
    public static String GET_ROBOT_SCHEDULE_COMMAND =       "{\"reqId\": \"77\",\"cmd\": \"getSchedule\"}";
    public static String SET_SCHEDULE_COMMAND =             "{\"reqId\": \"77\",\"cmd\": \"setSchedule\",\"params\":{\"scheduleId\":\"0\",\"serverId\":0,\"type\":0,\"version\":0,EVENTS_PLACEHOLDER}}";
    public static String CONNECT_TO_ROBOT_INFO_COMMAND =    "{\"reqId\": \"77\",\"cmd\": \"getRobotManualCleaningInfo\"}";
    public static String START_ROBOT_UPDATE =               "{\"reqId\": \"77\",\"cmd\": \"startSoftwareUpdate\",\"params\": {\"version\": \"VERSION_PLACEHOLDER\",\"url\": \"URL_PLACEHOLDER\",\"filesize\": SIZE_PLACEHOLDER}}";
    public static String GET_ROBOT_UPDATE_STATUS =          "{\"reqId\": \"77\",\"cmd\": \"getSoftwareUpdateState\"}";
    public static String FIND_ME_COMMAND =                  "{\"reqId\": \"77\",\"cmd\": \"findMe\"}";
    public static String GET_GENERAL_INFO_COMMAND =         "{\"reqId\": \"77\",\"cmd\": \"getGeneralInfo\"}";
    public static String GET_PREFERENCES_COMMAND =          "{\"reqId\": \"77\",\"cmd\": \"getPreferences\"}";
    public static String SET_PREFERENCES_COMMAND =          "{\"reqId\": \"77\",\"cmd\": \"setPreferences\"}";
    public static String GET_NETWORKS_COMMAND =             "{\"reqId\": \"77\",\"cmd\": \"getWifiNetworks\"}";
    public static String ADD_NETWORKS_COMMAND =             "{\"reqId\": \"77\",\"cmd\": \"addWifiNetwork\",\"params\": {\"ssid\": \"SSID_PLACEHOLDER\",\"password\": \"PWD_PLACEHOLDER\"}}";
    public static String DELETE_NETWORKS_COMMAND =          "{\"reqId\": \"77\",\"cmd\": \"removeWifiNetwork\",\"params\": {\"ssid\": \"SSID_PLACEHOLDER\"}}";
    public static String CONNECT_TO_NETWORKS_COMMAND =      "{\"reqId\": \"77\",\"cmd\": \"setWifiNetwork\",\"params\": {\"ssid\": \"SSID_PLACEHOLDER\"}}";
    public static String DISMISS_CURRENT_ALERT_COMMAND =    "{\"reqId\": \"77\",\"cmd\": \"dismissCurrentAlert\"}";
    public static String GET_VISIBLE_NETWORKS_COMMAND =     "{\"reqId\": \"77\",\"cmd\": \"getAvailableWifiNetworks\"}";
    public static String START_IEC_TEST_COMMAND =           "{\"reqId\": \"77\",\"cmd\": \"startIECTest\",\"params\": {\"speed\": SPEED_PLACEHOLDER,\"distance\": DISTANCE_PLACEHOLDER,\"carpet\": CARPET_PLACEHOLDER}}";
    public static String GET_ROBOT_STATS_COMMAND =           "{\"reqId\": \"77\",\"cmd\": \"getLocalStats\"}";

    //return the JSONObject command
    public static JSONObject get(String command) {
        try {
            return new JSONObject(command);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //return the JSONObject command with filled params
    public static JSONObject get(String command, String params) {
        try {
            JSONObject json = new JSONObject(command);
            json.put("params", new JSONObject(params));
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
