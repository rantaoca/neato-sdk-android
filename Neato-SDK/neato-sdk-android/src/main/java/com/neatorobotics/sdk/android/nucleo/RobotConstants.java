package com.neatorobotics.sdk.android.nucleo;

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */
public class RobotConstants {
    public final static int ROBOT_STATE_IDLE = 1;
    public final static int ROBOT_STATE_BUSY = 2;
    public final static int ROBOT_STATE_PAUSED = 3;
    public final static int ROBOT_STATE_ERROR = 4;

    public final static int ROBOT_ACTION_INVALID = 0;
    public final static int ROBOT_ACTION_HOUSE_CLEANING = 1;
    public final static int ROBOT_ACTION_SPOT_CLEANING = 2;
    public final static int ROBOT_ACTION_MANUAL_CLEANING = 3;
    public final static int ROBOT_ACTION_DOCKING = 4;
    public final static int ROBOT_ACTION_USER_MENU_ACTIVE = 5;
    public final static int ROBOT_ACTION_SUSPENDED_CLEANING = 6;
    public final static int ROBOT_ACTION_UPDATING = 7;
    public final static int ROBOT_ACTION_COPYING_LOGS = 8;
    public final static int ROBOT_ACTION_RECOVERING_LOCATION = 9;

    public final static int ROBOT_CLEANING_CATEGORY_HOUSE = 2;
    public final static int ROBOT_CLEANING_CATEGORY_SPOT = 3;
    public final static int ROBOT_CLEANING_CATEGORY_MANUAL = 1;

    public final static int ROBOT_CLEANING_MODE_ECO = 1;
    public final static int ROBOT_CLEANING_MODE_TURBO = 2;

    public final static int ROBOT_CLEANING_MODIFIER_NORMAL = 1;
    public final static int ROBOT_CLEANING_MODIFIER_DOUBLE = 2;

    public final static int ROBOT_CLEANING_SPOT_SIZE_200 = 200;
    public final static int ROBOT_CLEANING_SPOT_SIZE_400 = 400;
}
