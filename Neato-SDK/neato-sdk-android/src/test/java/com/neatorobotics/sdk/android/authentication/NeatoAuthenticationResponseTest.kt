package com.neatorobotics.sdk.android.authentication

import android.net.Uri
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.mockito.Mockito.*

@RunWith(MockitoJUnitRunner::class)
class NeatoAuthenticationResponseTest {

    @Mock
    internal var uriToken: Uri? = null
    @Mock
    internal var uriError: Uri? = null
    @Mock
    internal var uriInvalid: Uri? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        `when`(uriToken!!.toString()).thenReturn("my-app://neato#access_token=123&expiration=1")
        `when`(uriError!!.toString()).thenReturn("my-app://neato#error=error_1&error_description=description1")
        `when`(uriInvalid!!.toString()).thenReturn("")
    }

    @Test
    fun fromUriToken() {
        val response = NeatoAuthenticationResponse.fromUri(uriToken!!)
        assertNotNull(response)
        assertTrue(response.type === NeatoAuthenticationResponse.Response.TOKEN)
        assertEquals("123", response.token)
        assertNull(response.error)
        assertNull(response.errorDescription)
    }

    @Test
    fun fromUriError() {
        val response = NeatoAuthenticationResponse.fromUri(uriError!!)
        assertNotNull(response)
        assertTrue(response.type === NeatoAuthenticationResponse.Response.ERROR)
        assertEquals("error_1", response.error)
        assertEquals("description1", response.errorDescription)
        assertNull(response.token)
    }

    @Test
    fun fromUriCancelled() {
        val response = NeatoAuthenticationResponse.fromUri(uriInvalid!!)
        assertNotNull(response)
        assertTrue(response.type === NeatoAuthenticationResponse.Response.INVALID)
        assertNull(response.error)
        assertNull(response.errorDescription)
        assertNull(response.token)
    }

    @Test
    fun splitQueryMultipleParams() {
        val params = NeatoAuthenticationResponse.fromUri(null).splitQuery("a=vala&b=valb")
        assertNotNull(params)
        assertNotNull(params["a"])
        assertNotNull(params["b"])
        assertEquals(params["a"], "vala")
        assertEquals(params["b"], "valb")
    }

    @Test
    fun splitQuerySingleParam() {
        val params = NeatoAuthenticationResponse.fromUri(null).splitQuery("a=vala")
        assertNotNull(params)
        assertNotNull(params["a"])
        assertNull(params["b"])
        assertEquals(params["a"], "vala")
    }

    @Test
    fun splitQueryNoParam() {
        val params = NeatoAuthenticationResponse.fromUri(null).splitQuery("")
        assertNotNull(params)
        assertNull(params["a"])
        assertNull(params["b"])
    }

    @Test
    fun splitQueryNull() {
        val params = NeatoAuthenticationResponse.fromUri(null).splitQuery(null)
        assertNotNull(params)
        assertNull(params["a"])
        assertNull(params["b"])
    }

    @Test
    fun getTokenExpirationDateNoChange() {
        val inDateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        val inFormat = SimpleDateFormat(inDateFormat, Locale.US)

        val date1 = inFormat.parse("2014-01-02T12:00:00Z")
        val expire = NeatoAuthenticationResponse.fromUri(null).getTokenExpirationDate(date1, 0)//0 sec

        val cal1 = Calendar.getInstance()
        cal1.time = expire

        val cal2 = Calendar.getInstance()
        cal2.time = date1
        assertFalse(cal1.before(cal2))
        assertFalse(cal2.after(cal1))
    }

    @Test
    fun getTokenExpirationDate1Minute() {
        val inDateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        val inFormat = SimpleDateFormat(inDateFormat, Locale.US)

        val date1 = inFormat.parse("2014-01-02T12:00:00Z")
        val expire = NeatoAuthenticationResponse.fromUri(null).getTokenExpirationDate(date1, 60)//60 sec
        val date2 = inFormat.parse("2014-01-02T12:01:00Z")//add exactly 1 minute

        val cal1 = Calendar.getInstance()
        cal1.time = expire

        val cal2 = Calendar.getInstance()
        cal2.time = date2
        assertFalse(cal1.before(cal2))
        assertFalse(cal2.after(cal1))
    }

    @Test
    fun getTokenExpirationDate1Second() {
        val inDateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        val inFormat = SimpleDateFormat(inDateFormat, Locale.US)

        val date1 = inFormat.parse("2014-01-02T12:00:00Z")
        val expire = NeatoAuthenticationResponse.fromUri(null).getTokenExpirationDate(date1, 1)//1 sec
        val date2 = inFormat.parse("2014-01-02T12:00:01Z")//add exactly 1 sec

        val cal1 = Calendar.getInstance()
        cal1.time = expire

        val cal2 = Calendar.getInstance()
        cal2.time = date2
        assertFalse(cal1.before(cal2))
        assertFalse(cal2.after(cal1))
    }
}