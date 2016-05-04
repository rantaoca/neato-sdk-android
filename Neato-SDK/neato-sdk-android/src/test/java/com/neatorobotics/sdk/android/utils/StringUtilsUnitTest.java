package com.neatorobotics.sdk.android.utils;

import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class StringUtilsUnitTest {
    @Test
    public void testStringToHex() throws Exception{
        String input = "Caffeine";
        assertTrue(StringUtils.toHex(input.getBytes("UTF-8")).equalsIgnoreCase("4361666665696e65"));

        input = "email@example.com";
        assertTrue(StringUtils.toHex(input.getBytes("UTF-8")).equalsIgnoreCase("656d61696c406578616d706c652e636f6d"));

        input = "città";
        assertTrue(StringUtils.toHex(input.getBytes("UTF-8")).equalsIgnoreCase("63697474C3A0"));

        input = "{\"nome\":\"valore\"}";
        assertTrue(StringUtils.toHex(input.getBytes("UTF-8")).equalsIgnoreCase("7b226e6f6d65223a2276616c6f7265227d"));

        input = "";
        assertTrue(StringUtils.toHex(input.getBytes("UTF-8")).equalsIgnoreCase(""));

        input = " ";
        assertTrue(StringUtils.toHex(input.getBytes("UTF-8")).equalsIgnoreCase("20"));
    }
}
