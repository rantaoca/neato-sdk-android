package com.neatorobotics.sdk.android.utils

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

import java.io.ByteArrayInputStream
import java.nio.charset.Charset

import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals

@RunWith(MockitoJUnitRunner::class)
class StringUtilsUnitTest {

    @Test
    fun testStringToHex() {
        var input = "Caffeine"
        assertTrue(
            StringUtils.toHex(input.toByteArray(charset("UTF-8")))!!.equals(
                "4361666665696e65",
                ignoreCase = true
            )
        )

        input = "email@example.com"
        assertTrue(
            StringUtils.toHex(input.toByteArray(charset("UTF-8")))!!.equals(
                "656d61696c406578616d706c652e636f6d",
                ignoreCase = true
            )
        )

        input = "città"
        assertTrue(StringUtils.toHex(input.toByteArray(charset("UTF-8")))!!.equals("63697474C3A0", ignoreCase = true))

        input = "{\"nome\":\"valore\"}"
        assertTrue(
            StringUtils.toHex(input.toByteArray(charset("UTF-8")))!!.equals(
                "7b226e6f6d65223a2276616c6f7265227d",
                ignoreCase = true
            )
        )

        input = ""
        assertTrue(StringUtils.toHex(input.toByteArray(charset("UTF-8")))!!.equals("", ignoreCase = true))

        input = " "
        assertTrue(StringUtils.toHex(input.toByteArray(charset("UTF-8")))!!.equals("20", ignoreCase = true))
    }

    @Test
    fun testGetStringFromInputStream() {
        val input = "{\"name\":\"marcoà.,.,.,!$%&€\"}"
        val bais = ByteArrayInputStream(input.toByteArray(Charset.forName("UTF-8")))
        val outputString = StringUtils.getStringFromInputStream(bais)
        assertEquals(input, outputString)
    }
}
