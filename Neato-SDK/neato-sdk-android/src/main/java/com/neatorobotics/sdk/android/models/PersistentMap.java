package com.neatorobotics.sdk.android.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Neato
 * Created by Marco on 08/01/2019.
 * Copyright © 2019 Neato Robotics. All rights reserved.
 */
public class PersistentMap implements Serializable {
    String id, name, url, rawMapUrl;
    Date expireAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRawMapUrl(String setRawMapUrl) {
        this.rawMapUrl = setRawMapUrl;
    }

    public String geRawMapUrl() {
        return rawMapUrl;
    }

    public Date getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }

    public boolean isExpired() {
        //milliseconds
        long difference = expireAt.getTime() - System.currentTimeMillis();
        if( difference < 30*1000 ) return true;//30 secs of margin
        else return false;
    }
}
