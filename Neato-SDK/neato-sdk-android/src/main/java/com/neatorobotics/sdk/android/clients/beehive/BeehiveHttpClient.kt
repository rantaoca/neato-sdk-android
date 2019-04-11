/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.clients.beehive

import android.util.Log
import com.neatorobotics.sdk.android.NeatoSDK
import com.neatorobotics.sdk.android.NeatoUser
import com.neatorobotics.sdk.android.authentication.NeatoAuthentication
import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.ResourceState
import com.neatorobotics.sdk.android.utils.DELETE
import com.neatorobotics.sdk.android.utils.DeviceUtils
import com.neatorobotics.sdk.android.utils.GET
import com.neatorobotics.sdk.android.utils.StringUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener

import java.io.BufferedInputStream
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.SocketTimeoutException
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLHandshakeException

class BeehiveHttpClient {

    val HTTP_UNPROCESSABLE_ENTITY = 422

    suspend fun call(verb: String, url: String, input: JSONObject? = null) : Resource<JSONObject> {

        lateinit var resource: Resource<JSONObject>

        withContext(Dispatchers.IO) {
            lateinit var urlConnection: HttpsURLConnection
            var os: OutputStream? = null
            var writer: BufferedWriter? = null
            var ins: InputStream? = null
            try {
                val url1 = URL(url)
                urlConnection = url1.openConnection() as HttpsURLConnection
                urlConnection.connectTimeout = 10000
                urlConnection.readTimeout = 10000
                urlConnection.doOutput = !(verb == GET || verb == DELETE)
                urlConnection.requestMethod = verb

                val accessToken = NeatoAuthentication.getInstance(NeatoSDK.applicationContext!!).oauth2AccessToken
                if (accessToken != null) {
                    urlConnection.setRequestProperty("Authorization", "Bearer $accessToken")
                }
                urlConnection.setRequestProperty("Accept", "application/vnd.neato.beehive.v1+json")
                urlConnection.setRequestProperty("Content-type", "application/json")
                urlConnection.setRequestProperty("X-Agent", DeviceUtils.xAgentString)

                // disable caching
                urlConnection.defaultUseCaches = false
                urlConnection.useCaches = false

                if (input != null) {
                    os = urlConnection.outputStream
                    writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                    writer.write(input.toString())
                    writer.flush()
                }

                ins = try
                //temp fix for 401 and WWW-AUTH header bug
                {
                    BufferedInputStream(urlConnection.inputStream)
                } catch (e: IOException) {
                    BufferedInputStream(urlConnection.errorStream)
                }

                var outputJson = StringUtils.getStringFromInputStream(ins!!)

                val httpResponseCode = urlConnection.responseCode

                Log.d(TAG, "BEEHIVE ENDPOINT: $verb $url BEEHIVE INPUT: $input BEEHIVE OUTPUT: $outputJson BEEHIVE RESPONSE HTTP CODE : $httpResponseCode")

                val json = JSONTokener(outputJson).nextValue()
                if (json is JSONArray) {
                    outputJson = "{\"value\":$outputJson}"
                }

                if (httpResponseCode in listOf(HttpURLConnection.HTTP_CREATED, HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_ACCEPTED)) {
                    resource = Resource.success(JSONObject(outputJson))
                } else if (httpResponseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    resource = Resource.error(ResourceState.HTTP_NOT_FOUND, null, JSONObject(outputJson))
                } else if (httpResponseCode == HttpURLConnection.HTTP_GATEWAY_TIMEOUT) {
                    resource = Resource.error(ResourceState.HTTP_TIMEOUT, null, JSONObject(outputJson))
                } else if (httpResponseCode == HttpURLConnection.HTTP_BAD_GATEWAY) {
                    resource = Resource.error(ResourceState.HTTP_BAD_GATEWAY, null, JSONObject(outputJson))
                } else if (httpResponseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                    resource = Resource.error(ResourceState.HTTP_FORBIDDEN, null, JSONObject(outputJson))
                } else if (httpResponseCode == HTTP_UNPROCESSABLE_ENTITY) {
                    resource = Resource.error(ResourceState.HTTP_UNPROCESSABLE_ENTITY, null, JSONObject(outputJson))
                } else if (httpResponseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    resource = Resource.error(ResourceState.HTTP_UNAUTHORIZED, null, JSONObject(outputJson))
                } else {
                    resource = Resource.error(ResourceState.HTTP_OTHER_ERROR, null, JSONObject(outputJson))
                }
            } catch (e: SSLHandshakeException) {
                Log.e(TAG, "Exception", e)
                resource = Resource.error(ResourceState.SSL_EXCEPTION, null, null)
            } catch (e: JSONException) {
                Log.e(TAG, "Exception", e)
                resource = Resource.error(ResourceState.JSON_EXCEPTION, null, null)
            } catch (e: MalformedURLException) {
                Log.e(TAG, "Exception", e)
                resource = Resource.error(ResourceState.MALFORMED_URL_EXCEPTION, null, null)
            } catch (e: UnsupportedEncodingException) {
                Log.e(TAG, "Exception", e)
                resource = Resource.error(ResourceState.UNSUPPORTED_ENCODING_EXCEPTION, null, null)
            } catch (e: ProtocolException) {
                Log.e(TAG, "Exception", e)
                resource = Resource.error(ResourceState.NET_PROTOCOL_EXCEPTION, null, null)
            } catch (e: SocketTimeoutException) {
                Log.e(TAG, "Exception", e)
                resource = Resource.error(ResourceState.HTTP_TIMEOUT, null, null)
            } catch (e: IOException) {
                Log.e(TAG, "Exception", e)
                resource = Resource.error(ResourceState.NO_INTERNET, null, null)
            } catch (e: Exception) {
                Log.e(TAG, "Exception", e)
                resource = Resource.error(ResourceState.EXCEPTION, null, null)
            } finally {
                try {
                    writer?.close()
                } catch (e: IOException) {
                    Log.e(TAG, "Exception", e)
                }

                try {
                    ins?.close()
                } catch (e: IOException) {
                    Log.e(TAG, "Exception", e)
                }

                try {
                    os?.close()
                } catch (e: IOException) {
                    Log.e(TAG, "Exception", e)
                }

                urlConnection.disconnect()
            }
        }
        return resource
    }

    companion object {

        private const val TAG = "BeehiveHttpClient"
    }
}