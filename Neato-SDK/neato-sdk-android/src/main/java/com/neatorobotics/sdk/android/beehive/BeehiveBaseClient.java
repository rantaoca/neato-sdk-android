package com.neatorobotics.sdk.android.beehive;

import android.util.Log;

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

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;

/**
 * Created by Marco on 03/05/16.
 */
public class BeehiveBaseClient {
    private static final String TAG = "BeehiveBaseClient";

    public static BeehiveResponse executeCall(String accessToken, String verb,String url, JSONObject input) {

        Log.d(TAG, "### JSON input " + input);

        HttpsURLConnection urlConnection = null;
        OutputStream os = null;
        BufferedWriter writer = null;
        InputStream is = null;
        try {
            URL url1 = new URL(url);
            urlConnection = (HttpsURLConnection) url1.openConnection();
            urlConnection.setConnectTimeout(60000);
            urlConnection.setReadTimeout(60000);
            if(verb.equals("GET") || verb.equals("DELETE")) {
                urlConnection.setDoOutput(false);
            }else {
                urlConnection.setDoOutput(true);
            }
            urlConnection.setRequestMethod(verb);

            if(accessToken != null) {
                urlConnection.setRequestProperty("Authorization", "Bearer "+accessToken);
            }
            urlConnection.setRequestProperty("Accept", "application/vnd.neato.beehive.v1+json");
            urlConnection.setRequestProperty("Content-type", "application/json");
            urlConnection.setRequestProperty("X-Agent", DeviceUtils.getXAgentString());

            if(input != null) {
                os = urlConnection.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(input.toString());
                writer.flush();
            }

            try//temp fix for 401 and WWW-AUTH header bug
            {
                is = new BufferedInputStream(urlConnection.getInputStream());
            } catch (IOException e) {
                is = new BufferedInputStream(urlConnection.getErrorStream());
            }
            String outputJson = StringUtils.getStringFromInputStream(is);
            Object json = new JSONTokener(outputJson).nextValue();
            if (json instanceof JSONArray) {
                outputJson = "{\"value\":"+outputJson+"}";
            }
            Log.d("JSON output from " + verb + " " + url, outputJson);

            int responsecode = urlConnection.getResponseCode();

            BeehiveResponse rerviceResult = new BeehiveResponse( responsecode, new JSONObject(outputJson));
            return rerviceResult;
        }
        catch (SSLHandshakeException e) {
            Log.e(TAG, "Exception", e);
        }
        catch(Exception e) {
            Log.e(TAG, "Exception", e);
        }
        finally {
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
