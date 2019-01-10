package com.neatorobotics.sdk.android.robotservices.scheduling;

/**
 * Neato
 * Created by Marco on 04/01/2017.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */

public class SchedulingServiceFactory {
    public static SchedulingService get(String serviceVersion) {
        if("basic-1".equalsIgnoreCase(serviceVersion)) return new SchedulingBasic1Service();
        else if("basic-2".equalsIgnoreCase(serviceVersion)) return new SchedulingBasic2Service();
        else if("minimal-1".equalsIgnoreCase(serviceVersion)) return new SchedulingMinimal1Service();
        else return null;
    }
}
