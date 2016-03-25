package com.neatorobotics.sdk.android.authentication;

/**
 * Created by Marco on 24/03/16.
 */
public enum NeatoOAuth2Scope {
    READ("read"),
    WRITE("write"),
    ROBOT_COMMANDS("robot_commands");

    private final String permission;

    NeatoOAuth2Scope(final String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return permission;
    }
}