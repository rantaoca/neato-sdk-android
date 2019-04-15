/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android

import android.content.Context

class NeatoSDK {

    companion object {
        fun init(ctx: Context?) {
            applicationContext = ctx
        }

        var applicationContext: Context? = null
    }
}