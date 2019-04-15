/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.authentication

import android.net.Uri
import android.util.Log

import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.Calendar
import java.util.Date
import java.util.LinkedHashMap

class NeatoAuthenticationResponse private constructor(uri: Uri?) {

    /**
     *
     * @return the response type of the authentication request
     */
    var type: Response? = null
        private set
    /**
     *
     * @return the authentication token if the signin was successful or null otherwise
     */
    var token: String? = null
        private set
    /**
     *
     * @return the expiresIn time of the access token in seconds
     */
    var tokenExpirationDate: Date? = null
        private set
    /**
     *
     * @return the authentication error code if the signin failed
     */
    var error: String? = null
        private set
    /**
     *
     * @return the authentication error description if the signin failed
     */
    var errorDescription: String? = null
        private set

    enum class Response {
        TOKEN, ERROR, INVALID
    }

    init {
        parseUriResponse(uri)
    }

    /**
     *
     * @param uri
     * @return set the NeatoAuthenticationResponse instace fields
     */
    private fun parseUriResponse(uri: Uri?) {
        if (uri?.toString() == null) {
            type = Response.INVALID
            return
        }
        val rawUriStr = uri.toString()
        if (!rawUriStr.contains("#")) {
            type = Response.INVALID
            return
        }
        if (rawUriStr.split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size < 2) {
            type = Response.INVALID
            return
        }
        val queryStr = rawUriStr.split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        if (queryStr == null) {
            type = Response.INVALID
            return
        }
        try {
            val params = splitQuery(queryStr)
            if (params.containsKey("access_token")) {
                type = Response.TOKEN
                token = params["access_token"]
                if (params.containsKey("expires_in")) {
                    try {
                        tokenExpirationDate = getTokenExpirationDate(Date(), Integer.parseInt(params["expires_in"]!!))
                    } catch (e: Exception) {
                        //set a default expire date -> 1 month on
                        val cal = Calendar.getInstance()
                        cal.add(Calendar.MONTH, 1)
                        tokenExpirationDate = cal.time
                    }

                }
            } else if (params.containsKey("error")) {
                type = Response.ERROR
                error = params["error"]
                if (params.containsKey("error_description")) {
                    errorDescription = params["error_description"]
                }
            } else
                type = Response.INVALID
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            type = Response.INVALID
        }

    }

    /**
     *
     * @param query the uri query string eg. "key1=val1&key2=val2"
     * @return a map containing all the value-key pairs
     * @throws UnsupportedEncodingException
     */
    @Throws(UnsupportedEncodingException::class)
    fun splitQuery(query: String?): Map<String, String> {
        val query_pairs = LinkedHashMap<String, String>()
        if (query != null) {
            try {
                val pairs = query.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (pair in pairs) {
                    val idx = pair.indexOf("=")
                    if (idx != -1) {
                        query_pairs[URLDecoder.decode(pair.substring(0, idx), "UTF-8")] =
                            URLDecoder.decode(pair.substring(idx + 1), "UTF-8")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return query_pairs
    }

    /**
     *
     * @param expires_in seconds from now()
     * @return the date when the current access token will expire.
     */
    fun getTokenExpirationDate(now: Date, expires_in: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = now
        cal.add(Calendar.SECOND, expires_in)
        return cal.time
    }

    companion object {

        private const val TAG = "NeatoAuthResponse"

        /**
         *
         * @param uri
         * @return an authentication request object starting from the response Uri
         */
        fun fromUri(uri: Uri?): NeatoAuthenticationResponse {
            Log.d(TAG, uri.toString())
            return NeatoAuthenticationResponse(uri)
        }
    }
}
