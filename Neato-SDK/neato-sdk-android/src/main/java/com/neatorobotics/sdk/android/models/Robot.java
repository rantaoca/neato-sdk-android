package com.neatorobotics.sdk.android.models;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Marco on 03/05/16.
 */
public class Robot implements Serializable{
    public String name,serial,secretKey,model,linkedAt;

    public Robot(JSONObject json){
        if(json != null) {
            this.serial    = json.optString("serial");
            this.name      = json.optString("name");
            this.model     = json.optString("model");
            this.linkedAt  = json.optString("linked_at");
            this.secretKey = json.optString("secret_key");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLinkedAt() {
        return linkedAt;
    }

    public void setLinkedAt(String linkedAt) {
        this.linkedAt = linkedAt;
    }
}
