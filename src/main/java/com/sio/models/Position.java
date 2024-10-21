package com.sio.models;

import java.sql.Timestamp;

public class Position {
    private int id;
    private Target target;
    private Float latitude;
    private Float longitude;
    private Timestamp timestamp;

    public Position(int id, Target target, Float latitude, Float longitude, Timestamp timestamp) {
        this.id = id;
        this.target = target;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public Position(Target target, Float latitude, Float longitude, Timestamp timestamp) {
        this.target = target;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public Position(Float latitude, Float longitude, Timestamp timestamp) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
