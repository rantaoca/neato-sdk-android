package com.neatorobotics.sdk.android.authentication;

import android.net.Uri;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NeatoAuthenticationResponseTest {

    @Mock
    Uri uriToken;
    @Mock
    Uri uriError;
    @Mock
    Uri uriInvalid;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(uriToken.toString()).thenReturn("my-app://neato#access_token=123&expiration=1");
        when(uriError.toString()).thenReturn("my-app://neato#error=error_1&error_description=description1");
        when(uriInvalid.toString()).thenReturn("");
    }

    @Test
    public void fromUriToken() throws Exception {
        NeatoAuthenticationResponse response = NeatoAuthenticationResponse.fromUri(uriToken);
        assertNotNull(response);
        assertTrue(response.getType() == NeatoAuthenticationResponse.Response.TOKEN);
        assertEquals("123", response.getToken());
        assertNull(response.getError());
        assertNull(response.getErrorDescription());
    }

    @Test
    public void fromUriError() throws Exception {
        NeatoAuthenticationResponse response = NeatoAuthenticationResponse.fromUri(uriError);
        assertNotNull(response);
        assertTrue(response.getType() == NeatoAuthenticationResponse.Response.ERROR);
        assertEquals("error_1", response.getError());
        assertEquals("description1", response.getErrorDescription());
        assertNull(response.getToken());
    }

    @Test
    public void fromUriCancelled() throws Exception {
        NeatoAuthenticationResponse response = NeatoAuthenticationResponse.fromUri(uriInvalid);
        assertNotNull(response);
        assertTrue(response.getType() == NeatoAuthenticationResponse.Response.INVALID);
        assertNull(response.getError());
        assertNull(response.getErrorDescription());
        assertNull(response.getToken());
    }

    @Test
    public void splitQueryMultipleParams() throws Exception {
        Map<String, String> params = NeatoAuthenticationResponse.fromUri(null).splitQuery("a=vala&b=valb");
        assertNotNull(params);
        assertNotNull(params.get("a"));
        assertNotNull(params.get("b"));
        assertEquals(params.get("a"), "vala");
        assertEquals(params.get("b"), "valb");
    }

    @Test
    public void splitQuerySingleParam() throws Exception {
        Map<String, String> params = NeatoAuthenticationResponse.fromUri(null).splitQuery("a=vala");
        assertNotNull(params);
        assertNotNull(params.get("a"));
        assertNull(params.get("b"));
        assertEquals(params.get("a"), "vala");
    }

    @Test
    public void splitQueryNoParam() throws Exception {
        Map<String, String> params = NeatoAuthenticationResponse.fromUri(null).splitQuery("");
        assertNotNull(params);
        assertNull(params.get("a"));
        assertNull(params.get("b"));
    }

    @Test
    public void splitQueryNull() throws Exception {
        Map<String, String> params = NeatoAuthenticationResponse.fromUri(null).splitQuery(null);
        assertNotNull(params);
        assertNull(params.get("a"));
        assertNull(params.get("b"));
    }

    @Test
    public void getTokenExpirationDateNoChange() throws Exception {
        String inDateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat inFormat = new SimpleDateFormat(inDateFormat, Locale.US);

        Date date1 = inFormat.parse("2014-01-02T12:00:00Z");
        Date expire = NeatoAuthenticationResponse.fromUri(null).getTokenExpirationDate(date1, 0);//0 sec

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(expire);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date1);
        assertFalse(cal1.before(cal2));
        assertFalse(cal2.after(cal1));
    }

    @Test
    public void getTokenExpirationDate1Minute() throws Exception {
        String inDateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat inFormat = new SimpleDateFormat(inDateFormat, Locale.US);

        Date date1 = inFormat.parse("2014-01-02T12:00:00Z");
        Date expire = NeatoAuthenticationResponse.fromUri(null).getTokenExpirationDate(date1, 60);//60 sec
        Date date2 = inFormat.parse("2014-01-02T12:01:00Z");//add exactly 1 minute

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(expire);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        assertFalse(cal1.before(cal2));
        assertFalse(cal2.after(cal1));
    }

    @Test
    public void getTokenExpirationDate1Second() throws Exception {
        String inDateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat inFormat = new SimpleDateFormat(inDateFormat, Locale.US);

        Date date1 = inFormat.parse("2014-01-02T12:00:00Z");
        Date expire = NeatoAuthenticationResponse.fromUri(null).getTokenExpirationDate(date1, 1);//1 sec
        Date date2 = inFormat.parse("2014-01-02T12:00:01Z");//add exactly 1 sec

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(expire);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        assertFalse(cal1.before(cal2));
        assertFalse(cal2.after(cal1));
    }
}