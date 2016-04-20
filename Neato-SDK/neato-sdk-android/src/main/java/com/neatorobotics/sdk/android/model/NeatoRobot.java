package com.neatorobotics.sdk.android.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Marco on 11/03/16.
 */
public class NeatoRobot implements Serializable {

    private NeatoRobotState state;
    private String name,serial,secretKey,model,linkedAt;

    public NeatoRobot(){
    }

    public NeatoRobotState getState() {
        return state;
    }

    public void setState(NeatoRobotState state) {
        this.state = state;
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

    public static NeatoRobot createFromJSON(JSONObject json) {
        NeatoRobot robot = new NeatoRobot();
        if(json != null) {
            robot.serial = json.optString("serial");
            robot.name = json.optString("name");
            robot.model = json.optString("model");
            robot.linkedAt = json.optString("linked_at");
            robot.secretKey = json.optString("secret_key");
        }
        return robot;
    }
}
