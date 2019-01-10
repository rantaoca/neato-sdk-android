package com.neatorobotics.sdk.android.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DateUtilsUnitTest {
    @Test
    public void testGetHTTP11DateStringHeader() throws Exception{
        Calendar cal = Calendar.getInstance();
        cal.set(2016,4,4,14,55,45);//note the month is 0-based
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        String dateHeader = DateUtils.getHTTP11DateStringHeader(cal);
        assertEquals("Wed, 04 May 2016 14:55:45 GMT",dateHeader);
    }
}
