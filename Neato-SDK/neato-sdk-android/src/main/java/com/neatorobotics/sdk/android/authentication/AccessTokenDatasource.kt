/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.authentication

import java.util.Date

interface AccessTokenDatasource {
    val isTokenValid: Boolean
    fun storeToken(token: String, expires: Date)
    fun loadToken(): String?
    fun clearToken()
}
