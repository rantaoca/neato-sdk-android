/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.dsl

import android.graphics.PointF
import com.neatorobotics.sdk.android.models.*
import java.util.ArrayList
import java.util.HashMap

class RobotStateBuilder {

    var state: State = State.INVALID
    var action: Action = Action.INVALID
    var cleaningCategory: CleaningCategory = CleaningCategory.HOUSE
    var cleaningModifier: CleaningModifier = CleaningModifier.NORMAL
    var cleaningMode: CleaningMode = CleaningMode.TURBO
    var navigationMode: NavigationMode = NavigationMode.NORMAL
    var isCharging: Boolean = false
    var isDocked: Boolean = false
    var isDockHasBeenSeen: Boolean = false
    var isScheduleEnabled: Boolean = false
    var isStartAvailable: Boolean = false
    var isStopAvailable: Boolean = false
    var isPauseAvailable: Boolean = false
    var isResumeAvailable: Boolean = false
    var isGoToBaseAvailable: Boolean = false
    var spotSize: Int = 0
    var mapId: String? = null
    var boundary: Boundary? = null
    var boundaries: MutableList<Boundary> = mutableListOf()
    var scheduleEvents: ArrayList<ScheduleEvent> = ArrayList()
    var availableServices: HashMap<String, String> = HashMap()
    var message: String? = null
    var error: String? = null
    var alert: String? = null
    var charge: Double = 0.toDouble()
    var serial: String? = null
    var result: String? = null
    var robotRemoteProtocolVersion: Int = 0
    var robotModelName: String? = null
    var firmware: String? = null

    fun build(): RobotState {
        return RobotState(state = state, action = action, cleaningCategory = cleaningCategory, cleaningModifier = cleaningModifier,
                cleaningMode = cleaningMode, navigationMode = navigationMode, isCharging = isCharging,
                isDocked = isDocked, isDockHasBeenSeen = isDockHasBeenSeen, isScheduleEnabled = isScheduleEnabled,
                isStartAvailable = isStartAvailable, isStopAvailable = isStopAvailable, isPauseAvailable = isPauseAvailable,
                isResumeAvailable = isResumeAvailable, isGoToBaseAvailable = isGoToBaseAvailable, cleaningSpotHeight = spotSize,
                cleaningSpotWidth = spotSize, mapId = mapId, boundary = boundary, boundaries = boundaries,
                scheduleEvents = scheduleEvents, availableServices = availableServices, message = message,
                error = error, alert = alert, charge = charge, serial = serial, result = result,robotRemoteProtocolVersion = robotRemoteProtocolVersion,
                robotModelName = robotModelName, firmware = firmware)
    }

}

fun RobotStateBuilder.boundary(block: BoundaryBuilder.() -> Unit) {
    val bb = BoundaryBuilder()
    block(bb)
    this.boundary = bb.build()
}

class BoundaryBuilder {

    var id: String? = null
    var type: String = "polyline" //polyline or polygon
    var name: String = ""
    var color: String = "#000000"
    var isEnabled: Boolean = true
    var isSelected: Boolean = false
    var isValid: Boolean = true
    var vertices: MutableList<PointF>? = null
    var relevancy: PointF? = null //interest point inside a zone
    var state: BoundaryStatus = BoundaryStatus.NONE

    fun build(): Boundary {
        return Boundary(id = id, type = type, name = name, color = color, isEnabled = isEnabled,
                isSelected = isSelected, isValid = isValid, vertices = vertices,
                relevancy = relevancy, state = state)
    }

}

fun RobotStateBuilder.boundaries(block: BoundariesBuilder.() -> Unit) {
    val bb = BoundariesBuilder()
    block(bb)
    this.boundaries = bb.build()
}

class BoundariesBuilder {

    var boundaries = mutableListOf<Boundary>()

    fun boundary(block: BoundaryBuilder.() -> Unit) {
        val tb = BoundaryBuilder()
        block(tb)
        boundaries.add(tb.build())
    }

    fun build(): MutableList<Boundary> {
        return boundaries
    }

}

fun RobotStateBuilder.services(block: ServicesBuilder.() -> Unit) {
    val bb = ServicesBuilder()
    block(bb)
    this.availableServices = bb.build()
}

class ServicesBuilder {

    var services = hashMapOf<String, String>()

    fun service(block: ServiceBuilder.() -> Unit) {
        val tb = ServiceBuilder()
        block(tb)
        services[tb.buildName()]= tb.buildVersion()
    }

    fun build(): HashMap<String, String> {
        return services
    }
}

class ServiceBuilder {

    var name: String = ""
    var version: String = ""

    fun buildName(): String {
        return name
    }

    fun buildVersion(): String {
        return version
    }
}