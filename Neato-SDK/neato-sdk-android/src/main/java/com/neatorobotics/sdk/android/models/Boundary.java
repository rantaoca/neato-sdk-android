package com.neatorobotics.sdk.android.models;

import android.graphics.PointF;

import java.io.Serializable;
import java.util.List;

public class Boundary implements Serializable {

    private String id;
    // "polyline" or "polygon"
    private String type = "polyline";
    private String name = "";
    private String color = "#000000";
    private boolean enabled = true;
    private boolean selected = false;
    private boolean valid = true;
    private List<PointF> vertices;
    private PointF relevancy; //interest point inside a zone

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<PointF> getVertices() {
        return vertices;
    }

    public void setVertices(List<PointF> vertices) {
        this.vertices = vertices;
    }

    public PointF getRelevancy() {
        return relevancy;
    }

    public void setRelevancy(PointF relevancy) {
        this.relevancy = relevancy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
