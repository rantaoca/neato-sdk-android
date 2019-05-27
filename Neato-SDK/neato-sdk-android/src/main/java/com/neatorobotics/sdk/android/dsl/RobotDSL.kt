/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.dsl

import com.neatorobotics.sdk.android.models.PersistentMap
import com.neatorobotics.sdk.android.models.Robot
import com.neatorobotics.sdk.android.models.RobotState
import java.util.ArrayList
import java.util.HashSet

fun robot(block: RobotBuilder.() -> Unit): Robot {
    val r = RobotBuilder()
    block(r)
    return r.build()
}

class RobotBuilder {

    var serial: String? = null
    var prefix: String? = null
    var name: String? = null
    var mac: String? = null
    var model: String? = null
    var model_readable: String? = null
    var firmware: String? = null
    var tz_info: String? = null
    var created_at: String? = null
    var provisioned_at: String? = null
    var secret_key: String? = null
    var lat: Double = 0.toDouble()
    var lon: Double = 0.toDouble()
    var linkedAt: String? = null
    var nucleoUrl: String? = null
    var state: RobotState? = null
    var traits: MutableSet<String> = HashSet()
    var explorationId: String? = null
    var persistentMapsIds: MutableList<String> = ArrayList()
    var persistentMapsNames: MutableList<String> = ArrayList()

    fun build(): Robot {
        return Robot(serial = serial, prefix = prefix, name = name, mac = mac, secret_key = secret_key,
                model = model, model_readable = model_readable, firmware = firmware,
                tz_info = tz_info, created_at = created_at, provisioned_at = provisioned_at, lat = lat,
                lon = lon, linkedAt = linkedAt, nucleoUrl = nucleoUrl, state = state, explorationId = explorationId, persistentMapsIds = persistentMapsIds,
                persistentMapsNames = persistentMapsNames, traits = traits)
    }

}

fun RobotBuilder.traits(block: TraitsBuilder.() -> Unit) {
    val tb = TraitsBuilder()
    block(tb)
    this.traits = tb.build()
}

fun RobotBuilder.state(block: RobotStateBuilder.() -> Unit) {
    val rs = RobotStateBuilder()
    block(rs)
    this.state = rs.build()
}

fun RobotBuilder.persistentMaps(block: PersistentMapsBuilder.() -> Unit) {
    val pmb = PersistentMapsBuilder()
    block(pmb)
    this.persistentMapsIds = pmb.buildIds()
    this.persistentMapsNames = pmb.buildNames()
}

class TraitBuilder {

    var name = ""

    fun build(): String {
        return name
    }
}

class TraitsBuilder {

    var traits = mutableSetOf<String>()

    fun trait(block: TraitBuilder.() -> Unit) {
        val tb = TraitBuilder()
        block(tb)
        traits.add(tb.build())
    }

    fun build(): MutableSet<String> {
        return traits
    }
}

class PersistentMapsBuilder {

    var ids = mutableListOf<String>()
    var names = mutableListOf<String>()

    fun persistentMap(block: PersistentMapBuilder.() -> Unit) {
        val tb = PersistentMapBuilder()
        block(tb)
        ids.add(tb.build().id?:"")
        names.add(tb.build().name?:"")
    }

    fun buildIds(): MutableList<String> {
        return ids
    }

    fun buildNames(): MutableList<String> {
        return names
    }
}

class PersistentMapBuilder {

    var id = ""
    var name = ""

    fun build(): PersistentMap {
        return PersistentMap(id = id, name = name)
    }
}