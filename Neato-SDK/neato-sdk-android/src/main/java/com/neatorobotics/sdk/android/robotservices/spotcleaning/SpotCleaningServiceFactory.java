package com.neatorobotics.sdk.android.robotservices.spotcleaning;

/**
 * Neato
 * Created by Marco on 04/01/2017.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */

public class SpotCleaningServiceFactory {
    public static SpotCleaningService get(String serviceVersion) {
        if("basic-1".equalsIgnoreCase(serviceVersion)) return new SpotCleaningBasic1Service();
        else if("basic-2".equalsIgnoreCase(serviceVersion)) return new SpotCleaningBasic2Service();
        else if("minimal-2".equalsIgnoreCase(serviceVersion)) return new SpotCleaningMinimal2Service();
        else if("micro-2".equalsIgnoreCase(serviceVersion)) return new SpotCleaningMicro2Service();
        else return null;
    }
}
