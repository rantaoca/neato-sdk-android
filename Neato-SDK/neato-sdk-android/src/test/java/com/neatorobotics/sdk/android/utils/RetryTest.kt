/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.utils

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger

class RetryTest {

    @Test
    fun `test retry doesn't return the default value`() = runBlocking {

        val block = suspend {9}
        val exit: (Int) -> Boolean = {it -> it == 9}
        val default = 123

        val output: Int = retry(times = 4, block = block, exitBlock = exit, default = default)

        Assert.assertEquals(9, output)
    }

    @Test
    fun `test retry returns the default value`() = runBlocking {

        val block = suspend {9}
        val exit: (Int) -> Boolean = {it -> it == 7}
        val default = 123

        val output: Int = retry(times = 4, block = block, exitBlock = exit, default = default)

        Assert.assertEquals(default, output)
    }

    @Test
    fun `test retry returns the default value if an exception is always thrown`() = runBlocking {

        val block = suspend {throw IOException("cannot load data")}
        val exit: (Int) -> Boolean = {it -> true}
        val default = 123

        val output: Int = retry(times = 4, block = block, exitBlock = exit, default = default)

        Assert.assertEquals(default, output)
    }

    @Test
    fun `test retry doesn't return the default value if an exception is thrown`() = runBlocking {

        val atom = AtomicInteger(0 )

        val block = suspend {
            if(atom.get() == 0) {
                atom.set(atom.get()+1)
                throw IOException("cannot load data")
            }
            else 9
        }
        val exit: (Int) -> Boolean = {it -> it == 9}
        val default = 123

        val output: Int = retry(times = 4, block = block, exitBlock = exit, default = default)

        Assert.assertEquals(1, atom.get())
        Assert.assertEquals(9, output)
    }
}
