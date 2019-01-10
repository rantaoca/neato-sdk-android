package com.neatorobotics.sdk.android.robotservices.maps;

/**
 * Neato
 * Created by Marco on 04/01/2017.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */

public class MapsServiceFactory {
    public static MapsService get(String serviceVersion) {
        if("advanced-1".equalsIgnoreCase(serviceVersion)) return new MapsAdvanced1Service();
        else if("macro-1".equalsIgnoreCase(serviceVersion)) return new MapsMacro1Service();
        else if("basic-2".equalsIgnoreCase(serviceVersion)) return new MapsBasic2Service();
        else if("basic-1".equalsIgnoreCase(serviceVersion)) return new MapsBasic1Service();
        else return null;
    }
}
