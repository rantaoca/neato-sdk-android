/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.utils

import android.util.Log
import kotlinx.coroutines.delay

const val TAG = "RetryUtils.kt"

suspend fun <T> retryWithException(
        times: Int = Int.MAX_VALUE,
        initialDelay: Long = 100,
        maxDelay: Long = 3000,
        factor: Double = 2.0,
        block: suspend () -> T): T
{
    var currentDelay = initialDelay
    repeat(times - 1) {
        try {
            return block()
        } catch (e: Exception) {
            Log.e(TAG, e.message?:"")
            // you can log an error here and/or make a more finer-grained
            // analysis of the cause to see if retry is needed
        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
    return block() // last attempt
}

suspend fun <T> retry(
        times: Int = Int.MAX_VALUE,
        initialDelay: Long = 100,
        maxDelay: Long = 3000,
        factor: Double = 2.0,
        block: suspend () -> T,
        exitBlock: (T) -> Boolean,
        default: T): T
{
    var currentDelay = initialDelay
    repeat(times) {
        try {
            val result = block()
            if(exitBlock(result)) return result
        } catch (e: Exception) {
            // you can log an error here
        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
    return default // last attempt
}