/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.authentication

enum class NeatoOAuth2Scope private constructor(private val permission: String) {

    PUBLIC_PROFILE("public_profile"),
    CONTROL_ROBOTS("control_robots"),
    MAPS("maps");

    override fun toString(): String {
        return permission
    }
}