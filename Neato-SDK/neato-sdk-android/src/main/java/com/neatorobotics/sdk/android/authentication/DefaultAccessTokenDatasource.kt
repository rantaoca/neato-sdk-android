package com.neatorobotics.sdk.android.authentication

import android.content.Context
import android.content.SharedPreferences

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */
class DefaultAccessTokenDatasource(private val context: Context) : AccessTokenDatasource {

    private val TOKEN_KEY = "TOKEN_KEY"
    private val DATE_EXPIRES_TOKEN_KEY = "DATE_EXPIRES_TOKEN_KEY"

    override val isTokenValid: Boolean
        get() {
            if (loadToken() == null) return false
            val settings = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
            val expirationDateStr = settings.getString(DATE_EXPIRES_TOKEN_KEY, null)
            if (expirationDateStr != null) {
                try {
                    val date = deserializeDate(expirationDateStr)
                    val expire = Calendar.getInstance()
                    expire.time = date

                    val cal = Calendar.getInstance()
                    if (cal.after(expire)) return false
                } catch (e: ParseException) {
                    e.printStackTrace()
                    return false
                }

            } else
                return false
            return true
        }

    override fun storeToken(token: String, expires: Date) {
        val sharedPref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(TOKEN_KEY, token)
        editor.putString(DATE_EXPIRES_TOKEN_KEY, serializeDate(expires))
        editor.commit()
    }

    override fun loadToken(): String? {
        val settings = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
        return settings.getString(TOKEN_KEY, null)
    }

    override fun clearToken() {
        val sharedPref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        editor.commit()
    }

    companion object {

        private val TAG = "DefaultAccessTokenDatasource"

        fun serializeDate(date: Date): String {
            val dateFormat = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US
            )
            return dateFormat.format(date)
        }

        @Throws(ParseException::class)
        fun deserializeDate(dateStr: String): Date {
            val inDateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
            val inFormat = SimpleDateFormat(inDateFormat, Locale.US)
            return inFormat.parse(dateStr)
        }
    }
}
