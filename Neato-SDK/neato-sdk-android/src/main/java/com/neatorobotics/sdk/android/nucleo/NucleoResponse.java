package com.neatorobotics.sdk.android.nucleo;

import com.neatorobotics.sdk.android.NeatoError;

import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
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

    //true if the data response property is "ok"
    public boolean isResponseOK() {
        if(isHttpOK()) {
            if(getJSON() != null && getJSON().has("result") && getJSON().optString("result") != null
                    && getJSON().optString("result").equalsIgnoreCase("ok")) {
                return true;
            }
        }
        return false;
    }

    //true if http and robot response are both OK
    public boolean isOK() {
        return isHttpOK() && isResponseOK();
    }

    public int getStatusCode() {
        return httpResultCode;
    }

    public JSONObject getJSON() {
        return resultJson;
    }

    public boolean isStateResponse() {
        if(resultJson != null && resultJson.has("state")) return true;
        return false;
    }

    public NeatoError getError() {
        if(getJSON() != null && getJSON().has("result") && getJSON().optString("result") != null) {
            NeatoError error;
            switch (getJSON().optString("result")) {
                case "ko": error = NeatoError.KO;
                    break;
                case "not_found": error = NeatoError.NOT_FOUND;
                    break;
                case "command_rejected": error = NeatoError.COMMAND_REJECTED;
                    break;
                case "invalid_entry": error = NeatoError.INVALID_ENTRY;
                    break;
                case "max_boundaries_exceeded": error = NeatoError.MAX_BOUNDARIES_EXCEEDED;
                    break;
                case "not_on_charge_base": error = NeatoError.NOT_ON_CHARGE_BASE;
                    break;
                case "not_idle": error = NeatoError.NOT_IDLE;
                    break;
                case "command_not_found": error = NeatoError.COMMAND_NOT_FOUND;
                    break;
                case "bad_request": error = NeatoError.BAD_REQUEST;
                    break;
                case "invalid_json": error = NeatoError.INVALID_JSON;
                    break;
                default: error = NeatoError.GENERIC_ERROR;
                    break;
            }
            return error;
        }else return NeatoError.GENERIC_ERROR;
    }
}
