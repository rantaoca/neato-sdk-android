package com.neatorobotics.sdk.android.nucleo;

import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by Marco on 04/04/16.
 */
public class NucleoResponse {

    private int httpResultCode;//http response code
    private JSONObject resultJson;//raw JSON response

    public NucleoResponse(int _httpResultCode, JSONObject _json){
        this.httpResultCode = _httpResultCode;
        this.resultJson = _json;
    }

    //true if the http request return OK 200 code
    public boolean isHttpOK(){
        return getStatusCode() == HttpURLConnection.HTTP_OK;
    }

    public int getStatusCode() {
        return httpResultCode;
    }

    public JSONObject getJSON() {
        return resultJson;
    }

    public boolean isStateResponse() {
        //TODO
        return true;
    }
}
