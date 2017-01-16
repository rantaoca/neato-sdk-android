package com.neatorobotics.sdk.android.robotservices.housecleaning;

import com.neatorobotics.sdk.android.nucleo.RobotConstants;
import com.neatorobotics.sdk.android.robotservices.cleaning.CleaningService;


/**
 * Neato
 * Created by Marco on 04/01/2017.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */

public abstract class HouseCleaningService extends CleaningService {

    public HouseCleaningService() {
        this.cleaningType = RobotConstants.ROBOT_CLEANING_CATEGORY_HOUSE;
    }
}
