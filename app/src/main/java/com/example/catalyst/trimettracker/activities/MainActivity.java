package com.example.catalyst.trimettracker.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import com.example.catalyst.trimettracker.events.LocationEvent;
import com.example.catalyst.trimettracker.network.ApiCaller;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import com.example.catalyst.trimettracker.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by dsloane on 7/8/2016.
 */
public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    final Handler timerHandler = new Handler();
    // runnable that will run with the timerHandler
    private Runnable timerRunnable;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final ApiCaller caller = new ApiCaller();


        timerRunnable = new Runnable() {
            @Override
            public void run() {
                caller.getVehicleLocation("3101");
                timerHandler.postDelayed(this, 10000);
            }
        };
        timerRunnable.run();

        caller.getVehicleLocation("3101");

    }

    @Subscribe
    public void updateLocation(LocationEvent event) {
        LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(location).title("17 Bus"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    @Override
    public void onResume() {
        // restart the countdown timer
        timerHandler.postDelayed(timerRunnable, 10000);
        super.onResume();
        // register EventBus
        EventBus.getDefault().register(this);

    }

    @Override
    public void onPause() {
        // unregister EventBus
        EventBus.getDefault().unregister(this);

        // stop the countdown timer when we leave the fragment
        timerHandler.removeCallbacks(timerRunnable);
        super.onPause();
    }
}