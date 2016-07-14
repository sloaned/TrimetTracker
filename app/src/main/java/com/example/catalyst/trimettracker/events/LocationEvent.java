package com.example.catalyst.trimettracker.events;

import com.example.catalyst.trimettracker.models.Vehicle;

import java.util.ArrayList;

/**
 * Created by dsloane on 7/8/2016.
 */
public class LocationEvent {
    private ArrayList<Vehicle> vehicles;

    public LocationEvent() {}
    public LocationEvent(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public ArrayList<Vehicle> getVehicles() { return vehicles; }
}
