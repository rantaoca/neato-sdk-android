/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.testutils

import com.neatorobotics.sdk.android.models.Action
import com.neatorobotics.sdk.android.models.Robot
import com.neatorobotics.sdk.android.models.RobotState
import com.neatorobotics.sdk.android.models.State
import com.neatorobotics.sdk.android.robotservices.RobotServices

import java.util.HashSet

object RobotTestFactory {

    val offlineRobot: Robot
        get() {
            return Robot().apply {
                name = "MyNeato"
                serial = "my-serial"
                secret_key = "my-secret-key"
                model = "BotVacConnected"
                state = RobotState(serial = "my-serial").apply {
                    robotModelName = "BotVacConnected"
                    robotRemoteProtocolVersion = 1
                    result = "ok"
                }
            }
        }

    val connectingRobot: Robot
        get() {
            return Robot().apply {
                name = "MyNeato"
                serial = "my-serial"
                secret_key = "my-secret-key"
                model = "BotVacConnected"
                state = RobotState(serial = "my-serial").apply {
                    robotModelName = "BotVacConnected"
                    robotRemoteProtocolVersion = 1
                    result = "ok"
                }
            }
        }

    val notAvailableRobot: Robot
        get() {
            return Robot().apply {
                name = "MyNeato"
                model = "BotVacConnected"
                state = RobotState(serial = "123").apply {
                    result = "ko"
                    state = State.INVALID
                    robotModelName = "BotVacConnected"
                    robotRemoteProtocolVersion = 1
                }

            }
        }

    val robotWithoutState: Robot
        get() {
            return Robot().apply {
                name = "MyNeato"
                model = "BotVacConnected"
                state = null
            }
        }

    val houseCleaningRobot: Robot
        get() {
            return busyRobot.apply {
                state?.action = Action.HOUSE_CLEANING
            }
        }

    val mapCleaningRobot: Robot
        get() {
            return busyRobot.apply {
                state?.action = Action.MAP_CLEANING
                state?.availableServices = hashMapOf(Pair(RobotServices.SERVICE_MAPS, RobotServices.VERSION_BASIC_2))
            }
        }

    val idleRobot: Robot
        get() {
            return Robot().apply {
                name = "MyNeato"
                model = "BotVacConnected"
                state = RobotState(serial = "123").apply {
                    firmware = "2.0.0"
                    result = "ok"
                    charge = 80.0
                    state = State.IDLE
                    action = Action.HOUSE_CLEANING
                    robotRemoteProtocolVersion = 1
                    robotModelName = "BotVacConnected"
                }
                serial = "my-serial"
                secret_key = "my-secret-key"
            }
        }

    val busyRobot: Robot
        get() {
            return Robot().apply {
                name = "MyNeato"
                serial = "my-serial"
                secret_key = "my-secret-key"
                model = "BotVacConnected"
                state = RobotState(serial = "123").apply {
                    firmware = "2.0.0"
                    result = "ok"
                    charge = 80.0
                    state = State.BUSY
                    robotRemoteProtocolVersion = 1
                    robotModelName = "BotVacConnected"
                }

            }
        }

    val needToChargeStateRobot: Robot
        get() {
            val robot = idleRobot.apply {
                state?.charge = 1.0
                state?.isCharging = false
                state?.isDocked = false
            }
            return robot
        }

    val needToChargeAndChargingStateRobot: Robot
        get() {
            val robot = idleRobot.apply {
                state?.charge = 1.0
                state?.isCharging = true
                state?.isDocked = true
            }
            return robot
        }

    val errorStateRobot: Robot
        get() {
            val robot = idleRobot.apply {
                state?.state = State.ERROR
                state?.action = Action.HOUSE_CLEANING
            }
            return robot
        }

    val updatingRobot: Robot
        get() {
            val robot = idleRobot.apply {
                state?.state = State.BUSY
                state?.action = Action.UPDATING
            }
            return robot
        }

    val copyingLogRobot: Robot
        get() {
            val robot = idleRobot.apply {
                state?.state = State.BUSY
                state?.action = Action.COPYING_LOGS
            }
            return robot
        }

    val relocalizingRobot: Robot
        get() {
            val robot = idleRobot.apply {
                state?.state = State.BUSY
                state?.action = Action.RECOVERING_LOCATION
            }
            return robot
        }

    val robotWithStateTimeout: Robot
        get() {
            return Robot().apply {
                name = "MyNeato"
                model = "BotVacConnected"
                state = RobotState(serial = "123")
            }
        }

    fun getIdleRobotWithService(service: String, version: String): Robot {
        return idleRobot.apply {
            state!!.availableServices[service] = version
        }
    }

    fun getOfflineRobotWithTrait(trait: String): Robot {
        return offlineRobot.apply {
            traits = object : HashSet<String>() {}
            traits.add(trait)
        }
    }
}
