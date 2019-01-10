package com.neatorobotics.sdk.android.robotservices.housecleaning;

/**
 * Neato
 * Created by Marco on 08/01/2019.
 * Copyright © 2019 Neato Robotics. All rights reserved.
 */

public class HouseCleaningBasic4Service extends HouseCleaningService {

    private static final String TAG = "HouseCleaningBasic4";

    @Override
    public boolean isEcoModeSupported() {
        return true;
    }

    @Override
    public boolean isTurboModeSupported() {
        return true;
    }

    @Override
    public boolean isExtraCareModeSupported() {
        return true;
    }

    @Override
    public boolean isCleaningAreaSupported() {
        return false;
    }

    @Override
    public boolean isCleaningFrequencySupported() {
        return false;
    }

    @Override
    public boolean isFloorPlanSupported() {
        return true;
    }

    @Override
    public boolean areZonesSupported() {
        return true;
    }

}
