package com.example.catalyst.trimettracker.events;

/**
 * Created by dsloane on 7/8/2016.
 */
public class LocationEvent {
    private float latitude;
    private float longitude;

    public LocationEvent(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() { return latitude; }
    public float getLongitude() { return longitude; }
}
