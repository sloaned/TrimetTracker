package com.example.catalyst.trimettracker.models;

/**
 * Created by Dan on 7/9/2016.
 */
public class Vehicle {

    private Float latitude;
    private Float longitude;
    private String routeNumber;
    private int bearing;
    private String signMessageLong;

    public Float getLatitude() { return latitude; }
    public void setLatitude(Float latitude) { this.latitude = latitude; }
    public Float getLongitude() { return longitude; }
    public void setLongitude(Float longitude) { this.longitude = longitude; }
    public String getRouteNumber() { return routeNumber; }
    public void setRouteNumber(String routeNumber) { this.routeNumber = routeNumber; }
    public int getBearing() { return bearing; }
    public void setBearing(int bearing) { this.bearing = bearing; }
    public String getSignMessageLong() { return signMessageLong; }
    public void setSignMessageLong(String signMessageLong) { this.signMessageLong = signMessageLong; }
}
