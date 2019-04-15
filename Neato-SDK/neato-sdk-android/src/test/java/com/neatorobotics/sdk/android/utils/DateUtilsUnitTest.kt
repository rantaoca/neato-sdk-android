package com.neatorobotics.sdk.android.utils

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

import java.util.Calendar
import java.util.TimeZone

import org.junit.Assert.*

@RunWith(MockitoJUnitRunner::class)
class DateUtilsUnitTest {

    @Test
    fun `test Get HTTP1 1Date String Header`() {
        val cal = Calendar.getInstance()
        cal.set(2016, 4, 4, 14, 55, 45)//note the month is 0-based
        cal.timeZone = TimeZone.getTimeZone("GMT")
        val dateHeader = DateUtils.getHTTP11DateStringHeader(cal)
        assertEquals("Wed, 04 May 2016 14:55:45 GMT", dateHeader)
    }
}
