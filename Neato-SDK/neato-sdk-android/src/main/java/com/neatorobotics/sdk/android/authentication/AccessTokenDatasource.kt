package com.neatorobotics.sdk.android.authentication

import java.util.Date

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */
interface AccessTokenDatasource {
    val isTokenValid: Boolean
    fun storeToken(token: String, expires: Date)
    fun loadToken(): String?
    fun clearToken()
}
