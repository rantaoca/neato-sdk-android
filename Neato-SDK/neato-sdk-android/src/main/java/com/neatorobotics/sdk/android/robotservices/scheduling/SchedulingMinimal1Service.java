package com.neatorobotics.sdk.android.robotservices.scheduling;

/**
 * Neato
 * Created by Marco on 04/01/2017.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */

public class SchedulingMinimal1Service extends SchedulingService {

    private static final String TAG = "SchedulingMinimal1";

    @Override
    public boolean isEcoModeSupported() {
        return false;
    }

    @Override
    public boolean isTurboModeSupported() {
        return false;
    }

    @Override
    public boolean areZonesSupported() {
        return false;
    }

}
