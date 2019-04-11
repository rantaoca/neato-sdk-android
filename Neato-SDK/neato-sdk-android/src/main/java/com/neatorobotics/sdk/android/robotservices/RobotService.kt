/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices

import com.neatorobotics.sdk.android.NeatoUser
import com.neatorobotics.sdk.android.clients.beehive.Beehive
import com.neatorobotics.sdk.android.clients.beehive.BeehiveRepository
import com.neatorobotics.sdk.android.clients.nucleo.NucleoRepository

abstract class RobotService(
        open var beehiveRepository: BeehiveRepository = BeehiveRepository(Beehive.URL),
        open var nucleoRepository: NucleoRepository = NucleoRepository())
