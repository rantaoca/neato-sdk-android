/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList
import java.util.HashMap

@Parcelize
data class RobotState(var state: State = State.INVALID,
                      var action: Action = Action.INVALID,
                      var cleaningCategory: CleaningCategory = CleaningCategory.HOUSE,
                      var cleaningModifier: CleaningModifier = CleaningModifier.NORMAL,
                      var cleaningMode: CleaningMode = CleaningMode.TURBO,
                      var navigationMode: NavigationMode = NavigationMode.NORMAL,
                      var isCharging: Boolean = false,
                      var isDocked: Boolean = false,
                      var isDockHasBeenSeen: Boolean = false,
                      var isScheduleEnabled: Boolean = false,
                      var isStartAvailable: Boolean = false,
                      var isStopAvailable: Boolean = false,
                      var isPauseAvailable: Boolean = false,
                      var isResumeAvailable: Boolean = false,
                      var isGoToBaseAvailable: Boolean = false,
                      var cleaningSpotWidth: Int = 0,
                      var cleaningSpotHeight: Int = 0,
                      var mapId: String? = null,
                      var boundary: Boundary? = null,
                      var boundaries: MutableList<Boundary> = mutableListOf(),
                      var scheduleEvents: ArrayList<ScheduleEvent> = ArrayList(),
                      var availableServices: HashMap<String, String> = HashMap(),
                      var message: String? = null,
                      var error: String? = null,
                      var alert: String? = null,
                      var charge: Double = 0.toDouble(),
                      var serial: String? = null,
                      var result: String? = null,
                      var robotRemoteProtocolVersion: Int = 0,
                      var robotModelName: String? = null,
                      var firmware: String? = null) : Parcelable

enum class State(val value: Int) {
    INVALID(0),
    IDLE(1),
    BUSY(2),
    PAUSED(3),
    ERROR(4);

    companion object {
        fun fromValue(value: Int): State {
            val state = State.values().firstOrNull { it.value == value }
            return state?:INVALID
        }
    }
}

enum class Action(val value: Int) {
    INVALID(0),
    HOUSE_CLEANING(1),
    SPOT_CLEANING(2),
    MANUAL_CLEANING(3),
    DOCKING(4),
    USER_MENU_ACTIVE(5),
    SUSPENDED_CLEANING(6),
    UPDATING(7),
    COPYING_LOGS(8),
    RECOVERING_LOCATION(9),
    IEC_TEST(10),
    MAP_CLEANING(11),
    EXPLORING_MAP(12),
    MAP_RETRIEVING(13),
    MAP_CREATING(14),
    EXPLORATION_SUSPENDED(15);

    companion object {
        fun fromValue(value: Int): Action {
            val action = Action.values().firstOrNull { it.value == value }
            return action?: Action.INVALID
        }
    }
}

enum class CleaningCategory(val value: Int) {
    INVALID(0),
    MANUAL(1),
    HOUSE(2),
    SPOT(3),
    MAP(4);

    companion object {
        fun fromValue(value: Int): CleaningCategory {
            val cc = CleaningCategory.values().firstOrNull { it.value == value }
            return cc?:INVALID
        }
    }
}

enum class CleaningModifier(val value: Int) {
    INVALID(0),
    NORMAL(1),
    DOUBLE(2);

    companion object {
        fun fromValue(value: Int): CleaningModifier {
            val cm = CleaningModifier.values().firstOrNull { it.value == value }
            return cm?:INVALID
        }
    }
}

enum class CleaningMode(val value: Int) {
    INVALID(0),
    ECO(1),
    TURBO(2);

    companion object {
        fun fromValue(value: Int): CleaningMode {
            val cm = CleaningMode.values().firstOrNull { it.value == value }
            return cm?:INVALID
        }
    }
}

enum class NavigationMode(val value: Int) {
    NORMAL(1),
    EXTRA_CARE(2),
    DEEP(3);

    companion object {
        fun fromValue(value: Int): NavigationMode {
            val nm = NavigationMode.values().firstOrNull { it.value == value }
            return nm?:NORMAL
        }
    }
}



