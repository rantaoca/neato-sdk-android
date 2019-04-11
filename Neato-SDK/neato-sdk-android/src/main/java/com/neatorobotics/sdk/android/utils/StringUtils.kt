package com.neatorobotics.sdk.android.utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.math.BigInteger

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */
object StringUtils {

    fun toHex(bytes: ByteArray?): String? {
        if (bytes == null) return null
        if (bytes.isEmpty()) return ""
        val bi = BigInteger(1, bytes)
        return String.format("%0" + (bytes.size shl 1) + "X", bi)
    }

    // convert InputStream to String
    fun getStringFromInputStream(`is`: InputStream): String {

        var br: BufferedReader? = null
        val sb = StringBuilder()

        var line: String? = null
        try {

            br = BufferedReader(InputStreamReader(`is`, "UTF-8"))
            while ({ line = br.readLine(); line }() != null) {
                sb.append(line)
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (br != null) {
                try {
                    br.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }

        return sb.toString()
    }
}
