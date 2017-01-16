package com.neatorobotics.sdk.android.robotservices.spotcleaning;

import com.neatorobotics.sdk.android.nucleo.RobotConstants;
import com.neatorobotics.sdk.android.robotservices.cleaning.CleaningService;


/**
 * Neato
 * Created by Marco on 04/01/2017.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */

public abstract class SpotCleaningService extends CleaningService {

    public SpotCleaningService() {
        this.cleaningType = RobotConstants.ROBOT_CLEANING_CATEGORY_SPOT;
    }
}
