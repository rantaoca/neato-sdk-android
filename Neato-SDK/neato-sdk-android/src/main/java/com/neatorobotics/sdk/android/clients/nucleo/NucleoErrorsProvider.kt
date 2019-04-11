/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.clients.nucleo

import com.neatorobotics.sdk.android.clients.ResourceState

open class NucleoErrorsProvider {
    open fun description(code: ResourceState): String {
        return code.name
    }
}