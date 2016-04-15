package com.neatorobotics.sdk.android;

/**
 * Created by Marco on 04/04/16.
 */
public class NeatoCallback<T> {
    public void done(T result){}
    public void fail(NeatoError error){}
}
