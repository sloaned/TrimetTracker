package com.example.catalyst.trimettracker.events;

import com.example.catalyst.trimettracker.models.Route;

import java.util.List;

/**
 * Created by dsloane on 7/13/2016.
 */

public class RouteEvent {

    private List<Route> routes;

    public RouteEvent(List<Route> routes) { this.routes = routes; }

    public List<Route> getRoutes() { return routes; }
    public void setRoutes(List<Route> routes) { this.routes = routes; }
}
