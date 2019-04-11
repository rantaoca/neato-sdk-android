package com.neatorobotics.sdk.android.authentication

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */
enum class NeatoOAuth2Scope private constructor(private val permission: String) {

    PUBLIC_PROFILE("public_profile"),
    CONTROL_ROBOTS("control_robots"),
    MAPS("maps");

    override fun toString(): String {
        return permission
    }
}