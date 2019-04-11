/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.clients.nucleo

object Nucleo {

    const val URL = "https://nucleo.neatocloud.com:4443"

    // commands
    const val GET_ROBOT_STATE_COMMAND = "{\"reqId\": \"77\",\"cmd\": \"getRobotState\"}"
    const val GET_ROBOT_SCHEDULE_COMMAND = "{\"reqId\": \"77\",\"cmd\": \"getSchedule\"}"
    const val SET_SCHEDULE_COMMAND = "{\"reqId\": \"77\",\"cmd\": \"setSchedule\",\"params\":{\"type\":1,EVENTS_PLACEHOLDER}}"
    const val MANUAL_DRIVE_INFO_COMMAND = "{\"reqId\": \"77\",\"cmd\": \"getRobotManualCleaningInfo\"}"
    const val START_ROBOT_UPDATE = "{\"reqId\": \"77\",\"cmd\": \"startSoftwareUpdate\",\"params\": {\"version\": \"VERSION_PLACEHOLDER\",\"url\": \"URL_PLACEHOLDER\",\"filesize\": SIZE_PLACEHOLDER}}"
    const val FIND_ME_COMMAND = "{\"reqId\": \"77\",\"cmd\": \"findMe\"}"
    const val GET_GENERAL_INFO_COMMAND = "{\"reqId\": \"77\",\"cmd\": \"getGeneralInfo\"}"
    const val GET_PREFERENCES_COMMAND = "{\"reqId\": \"77\",\"cmd\": \"getPreferences\"}"
    const val SET_PREFERENCES_COMMAND = "{\"reqId\": \"77\",\"cmd\": \"setPreferences\"}"
    const val GET_WIFI_NETWORKS_COMMAND = "{\"reqId\": \"77\",\"cmd\": \"getWifiNetworks\"}"
    const val ADD_NETWORKS_COMMAND = "{\"reqId\": \"77\",\"cmd\": \"addWifiNetwork\",\"params\": {\"ssid\": \"SSID_PLACEHOLDER\",\"password\": \"PWD_PLACEHOLDER\"}}"
    const val REMOVE_WIFI_NETWORK_COMMAND = "{\"reqId\": \"77\",\"cmd\": \"removeWifiNetwork\",\"params\": {\"ssid\": \"SSID_PLACEHOLDER\"}}"
    const val CONNECT_TO_NETWORKS_COMMAND = "{\"reqId\": \"77\",\"cmd\": \"setWifiNetwork\",\"params\": {\"ssid\": \"SSID_PLACEHOLDER\"}}"
    const val DISMISS_CURRENT_ALERT_COMMAND = "{\"reqId\": \"77\",\"cmd\": \"dismissCurrentAlert\"}"
    const val GET_AVAILABLE_WIFI_NETWORKS_COMMAND = "{\"reqId\": \"77\",\"cmd\": \"getAvailableWifiNetworks\"}"
    const val RESET_WIFI_NETWORKS_COMMAND = "{\"reqId\": \"77\",\"cmd\": \"resetWifiNetworks\"}"
    const val GET_ROBOT_LOCAL_STATS_COMMAND = "{\"reqId\": \"77\",\"cmd\": \"getLocalStats\"}"

    // constants
    const val SPOT_AREA_SMALL = 200//cm
    const val SPOT_AREA_LARGE = 400//cm

    // input params keys
    const val CLEANING_MODE_KEY = "mode"
    const val CLEANING_CATEGORY_KEY = "category"
    const val CLEANING_MODIFIER_KEY = "modifier"
    const val CLEANING_NAVIGATION_MODE_KEY = "navigationMode"
    const val CLEANING_AREA_SPOT_WIDTH_KEY = "spotWidth"
    const val CLEANING_AREA_SPOT_HEIGHT_KEY = "spotHeight"
    const val ZONE_BOUNDARY_ID_KEY = "boundaryId"
    const val ZONE_BOUNDARIES_ID_KEY = "boundaryIds"

    const val IEC_TEST_SPEED_KEY = "speed"
    const val IEC_TEST_DISTANCE_KEY = "distance"
    const val IEC_TEST_CARPET_KEY = "carpet"
}
