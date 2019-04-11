package com.neatorobotics.sdk.android.authentication

import android.content.Context
import android.content.SharedPreferences

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

import java.util.Calendar
import java.util.Date

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue

@RunWith(MockitoJUnitRunner::class)
class DefaultAccessTokenDatasourceTest {

    @Mock
    internal var sharedPreferences: SharedPreferences? = null

    @Mock
    internal var editor: SharedPreferences.Editor? = null

    @Mock
    internal var context: Context? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun formatHTTP11DateString() {
        val now = Date()
        val formattedDate = DefaultAccessTokenDatasource.serializeDate(now)
        assertNotNull(formattedDate)
    }

    @Test
    fun parseHTTP11DateString() {
        val inputStr = "2014-01-02T12:00:00Z"
        val parsedDate = DefaultAccessTokenDatasource.deserializeDate(inputStr)
        assertNotNull(parsedDate)
    }

    @Test
    fun parseAndFormatHTTP11DateString() {
        val inputStr = "2014-01-02T12:00:00Z"
        val inputStr2 = "2014-01-02T12:00:01Z"
        val parsedDate = DefaultAccessTokenDatasource.deserializeDate(inputStr)
        assertNotNull(parsedDate)

        val formattedStr = DefaultAccessTokenDatasource.serializeDate(parsedDate)
        assertEquals(inputStr, formattedStr)

        val parsedDate2 = DefaultAccessTokenDatasource.deserializeDate(inputStr2)

        val cal1 = Calendar.getInstance()
        cal1.time = parsedDate

        val cal2 = Calendar.getInstance()
        cal2.time = parsedDate2
        assertTrue(cal1.before(cal2))
    }
}