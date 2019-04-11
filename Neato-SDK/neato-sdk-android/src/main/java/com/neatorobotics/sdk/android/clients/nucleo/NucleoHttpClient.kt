/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.clients.nucleo

import android.util.Log
import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.ResourceState
import com.neatorobotics.sdk.android.utils.*
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
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLHandshakeException

class NucleoHttpClient {

    val HTTP_UNPROCESSABLE_ENTITY = 422

    suspend fun call(verb: String, url: String, robot_serial: String, command: String?, robotSecretKey: String): Resource<JSONObject> {

        lateinit var resource: Resource<JSONObject>

        withContext(Dispatchers.IO) {

            var urlConnection: HttpsURLConnection? = null
            var os: OutputStream? = null
            var writer: BufferedWriter? = null
            var ins: InputStream? = null
            try {

                val full_url = "/vendors/neato/robots/$robot_serial/messages"

                val url1 = URL(url + full_url)
                urlConnection = url1.openConnection() as HttpsURLConnection
                //since we use self signed certificate, we have to trust it
                //urlConnection.setSSLSocketFactory(SSLSocketFactoryHelper.get(SSLSocketFactoryHelper.NUCLEO));

                urlConnection.connectTimeout = 10000
                urlConnection.readTimeout = 10000
                urlConnection.doOutput = verb != GET
                urlConnection.requestMethod = verb

                //compute authorization header
                val date_header = DateUtils.getHTTP11DateStringHeader(Calendar.getInstance())
                val string_to_sign = robot_serial.toLowerCase() + "\n" + date_header + "\n" + command
                val mac = Mac.getInstance("HmacSha256")
                val secret = SecretKeySpec(robotSecretKey.toByteArray(), "HmacSha256")
                mac.init(secret)
                val signature = StringUtils.toHex(mac.doFinal(string_to_sign.toByteArray(charset("UTF-8"))))!!.toLowerCase()

                urlConnection.setRequestProperty("Accept", "application/vnd.neato.nucleo.v1")
                urlConnection.setRequestProperty("Content-type", "application/json")
                urlConnection.setRequestProperty("Date", date_header)
                urlConnection.setRequestProperty("Authorization", "NEATOAPP $signature")
                urlConnection.setRequestProperty("X-Agent", DeviceUtils.xAgentString)

                // disable caching
                urlConnection.defaultUseCaches = false
                urlConnection.useCaches = false

                if (command != null) {
                    os = urlConnection.outputStream
                    writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                    writer.write(command)
                    writer.flush()
                }

                val httpResponseCode = urlConnection.responseCode

                ins = try
                //temp fix for 401 and WWW-AUTH header bug
                {
                    BufferedInputStream(urlConnection.inputStream)
                } catch (e: IOException) {
                    BufferedInputStream(urlConnection.errorStream)
                }

                var outputJson = StringUtils.getStringFromInputStream(ins)
                val json = JSONTokener(outputJson).nextValue()
                if (json is JSONArray) {
                    outputJson = "{\"value\":$outputJson}"
                }

                Log.d(TAG, "NUCLEO SIGNATURE HEADER: NEATOAPP $signature")
                Log.d(TAG, "NUCLEO ROBOT SERIAL: $robot_serial NUCLEO ENDPOINT: $verb $url NUCLEO INPUT: $command NUCLEO OUTPUT: $outputJson NUCLEO RESPONSE HTTP CODE : $httpResponseCode")

                val outputJsonObj = JSONObject(outputJson)

                if (httpResponseCode == HttpURLConnection.HTTP_CREATED || httpResponseCode == HttpURLConnection.HTTP_OK) {
                    resource = if (RobotResults.isRobotResponseOK(outputJsonObj)) {
                        Resource.success(outputJsonObj)
                    } else {
                        Resource.error(RobotResults.getRobotResultResourceState(outputJsonObj), null, outputJsonObj)
                    }
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
                    resource = Resource.error(ResourceState.HTTP_OTHER_ERROR, null, outputJsonObj)
                }
            } catch (e: SSLHandshakeException) {
                Log.e(TAG, "Exception", e)
                resource = Resource.error(ResourceState.SSL_EXCEPTION, null, null)
            } catch (e: NoSuchAlgorithmException) {
                Log.e(TAG, "Exception", e)
                resource = Resource.error(ResourceState.NO_SUCH_ALGORITHM_EXCEPTION, null, null)
            } catch (e: JSONException) {
                Log.e(TAG, "Exception", e)
                resource = Resource.error(ResourceState.JSON_EXCEPTION, null, null)
            } catch (e: InvalidKeyException) {
                Log.e(TAG, "Exception", e)
                resource = Resource.error(ResourceState.INVALID_KEY_EXCEPTION, null, null)
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

                urlConnection?.disconnect()
            }
        }
        return resource
    }

    companion object {

        private const val TAG = "NucleoHttpClient"
    }
}