package com.neatorobotics.sdk.android.authentication;

import android.net.Uri;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Marco on 25/03/16.
 */
public class NeatoAuthenticationResponse {

    private static final String TAG = "NeatoAuthResponse";

    private Response type;
    private String token;
    private String error;
    private String errorDescription;

    public enum Response {
        TOKEN, ERROR, INVALID
    }

    /**
     *
     * @param uri
     * @return an authentication request object starting from the response Uri
     */
    public static NeatoAuthenticationResponse fromUri(Uri uri) {
        Log.d(TAG, uri!=null?uri.toString():"");
        return new NeatoAuthenticationResponse(uri);
    }

    private NeatoAuthenticationResponse(Uri uri) {
        parseUriResponse(uri);
    }

    /**
     *
     * @return the response type of the authentication request
     */
    public Response getType() {
        return type;
    }

    /**
     *
     * @return the authentication token if the signin was successful or null otherwise
     */
    public String getToken() {
        return token;
    }

    /**
     *
     * @return the authentication error code if the signin failed
     */
    public String getError() {
        return error;
    }

    /**
     *
     * @return the authentication error description if the signin failed
     */
    public String getErrorDescription() {
        return errorDescription;
    }

    /**
     *
     * @param uri
     * @return set the NeatoAuthenticationResponse instace fields
     */
    protected void parseUriResponse(Uri uri) {
        if(uri == null || uri.toString() == null) {
            type =  Response.INVALID;
            return;
        }
        String rawUriStr = uri.toString();
        if(!rawUriStr.contains("#")) {
            type = Response.INVALID;
            return;
        }
        if(rawUriStr.split("#").length < 2) {
            type = Response.INVALID;
            return;
        }
        String queryStr = rawUriStr.split("#")[1];
        if(queryStr == null) {
            type = Response.INVALID;
            return;
        }
        try {
            Map<String,String> params = splitQuery(queryStr);
            if(params.containsKey("access_token")) {
                type = Response.TOKEN;
                token = params.get("access_token");
            }
            else if(params.containsKey("error")) {
                type = Response.ERROR;
                error = params.get("error");
                if(params.containsKey("error_description")) {
                    errorDescription = params.get("error_description");
                }
            }
            else type = Response.INVALID;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            type = Response.INVALID;
        }
    }

    /**
     *
     * @param query the uri query string eg. "key1=val1&key2=val2"
     * @return a map containing all the value-key pairs
     * @throws UnsupportedEncodingException
     */
    protected Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        if(query != null) {
            try {
                String[] pairs = query.split("&");
                for (String pair : pairs) {
                    int idx = pair.indexOf("=");
                    if (idx != -1) {
                        query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return query_pairs;
    }
}
