package com.neatorobotics.sdk.android.nucleo;

import android.util.Log;

import com.neatorobotics.sdk.android.utils.DateUtils;
import com.neatorobotics.sdk.android.utils.DeviceUtils;
import com.neatorobotics.sdk.android.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Calendar;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Marco on 03/05/16.
 */
public class NucleoBaseClient {

    private static final String TAG = "NucleoBaseClient";

    public static NucleoResponse executeNucleoCall(String url, String robot_serial, JSONObject command, String robotSecretKey) {
        HttpsURLConnection urlConnection = null;
        OutputStream os = null;
        BufferedWriter writer = null;
        InputStream is = null;
        try {

            Log.d(TAG,"### JSON "+command.toString());

            String full_url = "/vendors/neato/robots/"+robot_serial+"/messages";

            URL url1 = new URL(url+full_url);
            urlConnection = (HttpsURLConnection) url1.openConnection();

            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestMethod("POST");

            //compute authorization header
            String date_header = DateUtils.getHTTP11DateStringHeader(Calendar.getInstance());
            String string_to_sign = robot_serial.toLowerCase() + "\n" + date_header + "\n" + command;
            Mac mac = Mac.getInstance("HmacSha256");
            SecretKeySpec secret = new SecretKeySpec(robotSecretKey.getBytes(), "HmacSha256");
            mac.init(secret);
            String signature =  StringUtils.toHex(mac.doFinal(string_to_sign.getBytes("UTF-8"))).toLowerCase();

            urlConnection.setRequestProperty("Accept", "application/vnd.neato.nucleo.v1");
            urlConnection.setRequestProperty("Content-type", "application/json");
            urlConnection.setRequestProperty("Date", date_header);
            urlConnection.setRequestProperty("Authorization", "NEATOAPP "+signature);
            urlConnection.setRequestProperty("X-Agent", DeviceUtils.getXAgentString());

            if(command != null) {
                os = urlConnection.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(command.toString());
                writer.flush();
            }

            int responsecode = urlConnection.getResponseCode();

            try//temp fix for 401 and WWW-AUTH header bug
            {
                is = new BufferedInputStream(urlConnection.getInputStream());
            }
            catch(IOException e)
            {
                is = new BufferedInputStream(urlConnection.getErrorStream());
            }

            String outputJson = StringUtils.getStringFromInputStream(is);
            Object json = new JSONTokener(outputJson).nextValue();
            if (json instanceof JSONArray) {
                outputJson = "{\"value\":"+outputJson+"}";
            }

            Log.d(TAG, "DATE HEADER : "+date_header);
            Log.d(TAG, "AGENT HEADER : "+DeviceUtils.getXAgentString());
            Log.d(TAG, "SIGNATURE HEADER : "+"NEATOAPP "+signature);
            Log.d("JSON output from POST " + url, outputJson);

            Log.d(TAG, "RESPONSE HTTP CODE : "+responsecode);

            NucleoResponse serviceResult = new NucleoResponse( responsecode, new JSONObject(outputJson));
            return serviceResult;
        }
        catch(Exception e) {
            Log.e(TAG, "Exception", e);
        }finally {
            try {
                if (writer != null) writer.close();
            }catch(IOException e){Log.e(TAG, "Exception", e);}
            try {
                if (is != null) is.close();
            }catch(IOException e){Log.e(TAG, "Exception", e);}
            try {
                if (os != null) os.close();
            }catch(IOException e){Log.e(TAG, "Exception", e);}
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}
