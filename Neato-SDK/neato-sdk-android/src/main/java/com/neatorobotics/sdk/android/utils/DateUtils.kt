/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtils {

    private val EPOCH_TIME = DateUtils.getDate("yyyy-MM-dd'T'HH:mm:ss'Z'", "2013-01-06T00:00:00Z")

    fun getHTTP11DateStringHeader(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US
        )
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
        return dateFormat.format(calendar.time)
    }

    fun getDate(inDateFormat: String, dateStr: String): Date? {
        val inFormat = SimpleDateFormat(inDateFormat)
        inFormat.timeZone = TimeZone.getTimeZone("UTC")
        try {
            val inDate = inFormat.parse(dateStr)
            return inDate
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    fun getDiffInSeconds(d1: Date?, d2: Date?): Int {
        if (d1 == null || d2 == null) return 0
        val diff = d2.time - d1.time//as given
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
        return seconds.toInt()
    }

    fun isAfterEpochTime(date: Date): Boolean {
        return date.after(EPOCH_TIME)
    }
}
