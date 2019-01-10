package com.neatorobotics.sdk.android.models;

import java.io.Serializable;
import java.util.Date;

public class CleaningMap implements Serializable {

    private String url, id, startAt, endAt, error;
    private boolean docked, delocalized;
    private double area, chargingTimeSeconds, timeInError, timeInPause;
    private Date urlExpiresAt;

    public boolean isExpired() {
        //milliseconds
        long difference = urlExpiresAt.getTime() - System.currentTimeMillis();
        if( difference < 30*1000 ) return true; // 30 secs of margin
        else return false;
    }

    public boolean isDelocalized() {
        return delocalized;
    }

    public void setDelocalized(boolean delocalized) {
        this.delocalized = delocalized;
    }

    public Date getUrlExpiresAt() {
        return urlExpiresAt;
    }

    public void setUrlExpiresAt(Date urlExpiresAt) {
        this.urlExpiresAt = urlExpiresAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isDocked() {
        return docked;
    }

    public void setDocked(boolean docked) {
        this.docked = docked;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getChargingTimeSeconds() {
        return chargingTimeSeconds;
    }

    public void setChargingTimeSeconds(double chargingTimeSeconds) {
        this.chargingTimeSeconds = chargingTimeSeconds;
    }

    public double getTimeInError() {
        return timeInError;
    }

    public void setTimeInError(double timeInError) {
        this.timeInError = timeInError;
    }

    public double getTimeInPause() {
        return timeInPause;
    }

    public void setTimeInPause(double timeInPause) {
        this.timeInPause = timeInPause;
    }
}
