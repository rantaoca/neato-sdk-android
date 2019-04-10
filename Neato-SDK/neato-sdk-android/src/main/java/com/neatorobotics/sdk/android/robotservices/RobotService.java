package com.neatorobotics.sdk.android.robotservices;

import androidx.annotation.VisibleForTesting;

import com.neatorobotics.sdk.android.nucleo.NucleoClient;


/**
 * Neato
 * Created by Marco on 04/01/2017.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */

public abstract class RobotService {
    @VisibleForTesting
    public NucleoClient nucleoClient = new NucleoClient();
}
