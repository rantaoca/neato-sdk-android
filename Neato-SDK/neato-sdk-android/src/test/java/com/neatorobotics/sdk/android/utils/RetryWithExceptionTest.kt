/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.utils

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import java.lang.RuntimeException
import java.util.concurrent.atomic.AtomicInteger

class RetryWithExceptionTest {

    @Test
    fun `test retryWithException executes up to N times`() = runBlocking {

        val times = 4
        val atomic = AtomicInteger(0)

        val output: Int = retryWithException(times = times, block = {
            atomic.set(atomic.get()+1)
            if(atomic.get() < times) throw RuntimeException("skip")
            atomic.get()
        })

        Assert.assertEquals(output, atomic.get())
        Assert.assertEquals(times, atomic.get())
    }

    @Test(expected = RuntimeException::class)
    fun `test retryWithException last attempt will raise an exception`() = runBlocking {

        val times = 4
        val atomic = AtomicInteger(0)

        val output: Int = retryWithException(times = times, block = {
            atomic.set(atomic.get()+1)
            if(atomic.get() <= times) throw RuntimeException("skip")
            atomic.get()
        })
    }

    @Test
    fun `test retryWithException executes less than N times if possible`() = runBlocking {

        val times = 4
        val atomic = AtomicInteger(0)

        val output: Int = retryWithException(times = times, block = {
            atomic.set(atomic.get()+1)
            atomic.get()
        })

        Assert.assertEquals(output, atomic.get())
        Assert.assertTrue(atomic.get() < times)
    }
}
