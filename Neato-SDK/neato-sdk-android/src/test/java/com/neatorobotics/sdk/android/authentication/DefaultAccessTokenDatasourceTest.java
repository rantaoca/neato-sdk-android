package com.neatorobotics.sdk.android.authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class DefaultAccessTokenDatasourceTest {

    @Mock
    SharedPreferences sharedPreferences;

    @Mock
    SharedPreferences.Editor editor;

    @Mock
    Context context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(sharedPreferences.getString(anyString(), anyString())).thenReturn("value");
        when(sharedPreferences.edit()).thenReturn(editor);
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences);
    }

    @Test
    public void formatHTTP11DateString() throws Exception {
        Date now = new Date();
        String formattedDate = DefaultAccessTokenDatasource.serializeDate(now);
        assertNotNull(formattedDate);
    }

    @Test
    public void parseHTTP11DateString() throws Exception {
        String inputStr = "2014-01-02T12:00:00Z";
        Date parsedDate= DefaultAccessTokenDatasource.deserializeDate(inputStr);
        assertNotNull(parsedDate);
    }

    @Test
    public void parseAndFormatHTTP11DateString() throws Exception {
        String inputStr = "2014-01-02T12:00:00Z";
        String inputStr2 = "2014-01-02T12:00:01Z";
        Date parsedDate= DefaultAccessTokenDatasource.deserializeDate(inputStr);
        assertNotNull(parsedDate);

        String formattedStr = DefaultAccessTokenDatasource.serializeDate(parsedDate);
        assertEquals(inputStr, formattedStr);

        Date parsedDate2= DefaultAccessTokenDatasource.deserializeDate(inputStr2);

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(parsedDate);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(parsedDate2);
        assertTrue(cal1.before(cal2));
    }
}