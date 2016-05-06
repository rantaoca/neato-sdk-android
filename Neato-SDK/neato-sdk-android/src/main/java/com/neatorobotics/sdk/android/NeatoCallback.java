package com.neatorobotics.sdk.android;

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */
public class NeatoCallback<T> {
    public void done(T result){}
    public void fail(NeatoError error){}
}
