/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.clients.beehive

import com.neatorobotics.sdk.android.clients.ResourceState

open class BeehiveErrorsProvider {
    open fun description(code: ResourceState): String {
        return code.name
    }
}