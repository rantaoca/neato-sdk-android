package com.neatorobotics.sdk.android.beehive;

import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */
public class BeehiveResponse {

    private int httpResultCode;//http response code
    private JSONObject resultJson;//raw JSON response

    public BeehiveResponse(int _httpResultCode, JSONObject _json){
        this.httpResultCode = _httpResultCode;
        this.resultJson = _json;
    }

    //true if the http request return OK 200 code
    public boolean isHttpOK(){
        return getStatusCode() == HttpURLConnection.HTTP_OK;
    }

    //true if the http request return OK 401 code
    public boolean isUnauthorized() {
        return getStatusCode() == HttpURLConnection.HTTP_UNAUTHORIZED;
    }

    public int getStatusCode() {
        return httpResultCode;
    }

    public JSONObject getJSON() {
        return resultJson;
    }
}
