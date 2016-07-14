package com.example.catalyst.trimettracker.models;

/**
 * Created by dsloane on 7/13/2016.
 */

public class Route {

    private String description;
    private String routeNumber;

    public Route() {}
    public Route(String description, String routeNumber) {
        this.description = description;
        this.routeNumber = routeNumber;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRouteNumber() { return routeNumber; }
    public void setRouteNumber(String routeNumber) { this.routeNumber = routeNumber; }
}
