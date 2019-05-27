/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.dsl

import com.neatorobotics.sdk.android.models.*
import com.neatorobotics.sdk.android.robotservices.RobotServices
import org.junit.Assert
import org.junit.Test

class RobotStateDSLTest {

    @Test
    fun `test robot state DSL simple properties`() {

        val r = robot {
            serial = "123"
            secret_key = "mySecretKey"
            state {
                state = State.IDLE
                action = Action.SPOT_CLEANING
                cleaningCategory = CleaningCategory.HOUSE
                cleaningModifier = CleaningModifier.DOUBLE
                cleaningMode = CleaningMode.ECO
                navigationMode = NavigationMode.EXTRA_CARE
                isCharging = true
                isDocked = true
                isDockHasBeenSeen = true
                isScheduleEnabled = true
                isStartAvailable = true
                isStopAvailable = true
                isPauseAvailable = true
                isResumeAvailable = true
                isGoToBaseAvailable = true
                spotSize = 150
                mapId = "mapId123"
                message = "my nice message"
                error = "my error is here"
                alert = "I warn you man"
                charge = 10.toDouble()
                serial = "my serial"
                result = "there is no result here"
                robotRemoteProtocolVersion = 1
                robotModelName = "boomba"
                firmware = "3.2.1"
            }
        }

        val state = r.state?.apply {
            Assert.assertTrue(10.0 == charge)
            Assert.assertEquals(State.IDLE, state)
            Assert.assertEquals(Action.SPOT_CLEANING, action)
            Assert.assertEquals(CleaningModifier.DOUBLE, cleaningModifier)
            Assert.assertEquals(CleaningMode.ECO, cleaningMode)
            Assert.assertEquals(NavigationMode.EXTRA_CARE, navigationMode)
            Assert.assertTrue(isCharging)
            Assert.assertTrue(isDocked)
            Assert.assertTrue(isDockHasBeenSeen)
            Assert.assertTrue(isScheduleEnabled)
            Assert.assertTrue(isStartAvailable)
            Assert.assertTrue(isStopAvailable)
            Assert.assertTrue(isPauseAvailable)
            Assert.assertTrue(isResumeAvailable)
            Assert.assertTrue(isGoToBaseAvailable)
            Assert.assertEquals(150, cleaningSpotHeight)
            Assert.assertEquals(150, cleaningSpotWidth)
            Assert.assertTrue(isPauseAvailable)
            Assert.assertEquals("mapId123", mapId)
            Assert.assertEquals("my nice message", message)
            Assert.assertEquals("I warn you man", alert)
            Assert.assertEquals("my serial", serial)
            Assert.assertEquals("there is no result here", result)
            Assert.assertEquals(1, robotRemoteProtocolVersion)
            Assert.assertEquals("boomba", robotModelName)
            Assert.assertEquals("3.2.1", firmware)
        }

        Assert.assertNotNull(state)
    }

    @Test
    fun `test robot state boundary DSL`() {

        val r = robot {
            serial = "123"
            secret_key = "mySecretKey"
            state {
                boundary {
                    id = "b123"
                    name = "myBoundary"
                }
            }
        }

        Assert.assertNotNull(r.state?.boundary)
        Assert.assertEquals("b123", r.state?.boundary?.id)
        Assert.assertEquals("myBoundary", r.state?.boundary?.name)
    }

    @Test
    fun `test robot state boundaries DSL`() {

        val r = robot {
            serial = "123"
            secret_key = "mySecretKey"
            state {
                boundaries {
                    boundary {
                        id = "b123"
                        name = "myBoundary"
                    }
                    boundary {
                        id = "b456"
                        name = "myBoundary2"
                    }
                }
            }
        }

        Assert.assertNotNull(r.state?.boundaries)
        Assert.assertEquals(2, r.state?.boundaries?.size)
        Assert.assertEquals("b123", r.state?.boundaries!![0].id)
        Assert.assertEquals("myBoundary", r.state?.boundaries!![0].name)
        Assert.assertEquals("b456", r.state?.boundaries!![1].id)
        Assert.assertEquals("myBoundary2", r.state?.boundaries!![1].name)
    }

    @Test
    fun `test robot state schedule events DSL`() {

        val r = robot {
            serial = "123"
            secret_key = "mySecretKey"
            state {
                scheduleEvents = arrayListOf(ScheduleEvent(startTime = "10:00"))
            }
        }

        Assert.assertNotNull(r.state?.scheduleEvents)
    }

    @Test
    fun `test robot state available services DSL`() {

        val r = robot {
            serial = "123"
            secret_key = "mySecretKey"
            state {
                services {
                    service {
                        name = RobotServices.SERVICE_FIND_ME
                        version = RobotServices.VERSION_BASIC_1
                    }
                    service {
                        name = RobotServices.SERVICE_SPOT_CLEANING
                        version = RobotServices.VERSION_ADVANCED_2
                    }
                }
            }
        }

        Assert.assertNotNull(r.state?.availableServices)
        Assert.assertEquals(2, r.state?.availableServices?.size)
        Assert.assertTrue(r.state?.availableServices?.containsKey(RobotServices.SERVICE_FIND_ME)?:false)
        Assert.assertTrue(r.state?.availableServices?.containsKey(RobotServices.SERVICE_SPOT_CLEANING)?:false)
        Assert.assertEquals(RobotServices.VERSION_BASIC_1, r.state?.availableServices!![RobotServices.SERVICE_FIND_ME])
        Assert.assertEquals(RobotServices.VERSION_ADVANCED_2, r.state?.availableServices!![RobotServices.SERVICE_SPOT_CLEANING])
    }
}
