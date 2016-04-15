package com.neatorobotics.sdk.android.authentication;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Marco on 29/03/16.
 */
public class DefaultAccessTokenDatasource implements AccessTokenDatasource {

    private static final String TAG = "DefaultAccessTokenDatasource";
    private final String TOKEN_KEY = "TOKEN_KEY";
    private final String DATE_EXPIRES_TOKEN_KEY = "DATE_EXPIRES_TOKEN_KEY";

    private Context context;

    public DefaultAccessTokenDatasource(Context context) {
        this.context = context;
    }

    @Override
    public void storeToken(String token, Date expires) {
        SharedPreferences sharedPref = context.getSharedPreferences(TAG,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TOKEN_KEY, token);
        editor.putString(DATE_EXPIRES_TOKEN_KEY, serializeDate(expires));
        editor.commit();
    }

    @Override
    public String loadToken() {
        SharedPreferences settings = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return settings.getString(TOKEN_KEY, null);
    }

    @Override
    public void clearToken() {
        SharedPreferences sharedPref = context.getSharedPreferences(TAG,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }

    @Override
    public boolean isTokenValid() {
        if(loadToken() == null) return false;
        SharedPreferences settings = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        String expirationDateStr = settings.getString(DATE_EXPIRES_TOKEN_KEY, null);
        if(expirationDateStr!=null) {
            try {
                Date date = deserializeDate(expirationDateStr);
                Calendar expire = Calendar.getInstance();
                expire.setTime(date);

                Calendar cal = Calendar.getInstance();
                if(cal.after(expire)) return false;
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }else return false;
        return true;
    }

    protected static String serializeDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        return dateFormat.format(date);
    }

    protected static Date deserializeDate(String dateStr) throws ParseException {
        String inDateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat inFormat = new SimpleDateFormat(inDateFormat, Locale.US);
        Date inDate = inFormat.parse(dateStr);
        return inDate;
    }
}
