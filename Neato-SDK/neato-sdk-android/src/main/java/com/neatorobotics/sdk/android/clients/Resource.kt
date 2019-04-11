/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.clients

//a generic class that describes a data with a status
class Resource<T> private constructor(val status: Status, val data: T?, val code: ResourceState, val message: String?) {

    enum class Status {
        SUCCESS, ERROR
    }

    companion object {

        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, ResourceState.OK, null)
        }

        fun <T> error(code: ResourceState, msg: String? = null, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, code, msg)
        }
    }
}
