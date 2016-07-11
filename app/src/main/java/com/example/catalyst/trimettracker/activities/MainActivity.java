package com.example.catalyst.trimettracker.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import com.example.catalyst.trimettracker.TextDrawable;
import com.example.catalyst.trimettracker.events.LocationEvent;
import com.example.catalyst.trimettracker.models.Vehicle;
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

        /*LatLng location = new LatLng(42.6049, -70.6527);

        mMap.addMarker(new MarkerOptions().position(location).title("Hey"));//.icon(BitmapDescriptorFactory.fromResource(R.drawable.vehicle_label)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
 */


        final ApiCaller caller = new ApiCaller();


        timerRunnable = new Runnable() {
            @Override
            public void run() {
                caller.getVehicleLocation("3168");
                timerHandler.postDelayed(this, 10000);
            }
        };
        timerRunnable.run();


    }

    @Subscribe
    public void updateLocation(LocationEvent event) {
        mMap.clear();
        for (Vehicle v : event.getVehicles()) {
            LatLng location = new LatLng(v.getLatitude(), v.getLongitude());

            TextDrawable circle = new TextDrawable(this, v.getRouteNumber());
           // Drawable circle = getResources().getDrawable(R.drawable.vehicle_label);
            Canvas canvas = new Canvas();
            Bitmap bitmap = Bitmap.createBitmap(circle.getIntrinsicWidth(), circle.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            canvas.setBitmap(bitmap);
            circle.setBounds(0, 0, circle.getIntrinsicWidth(), circle.getIntrinsicHeight());

            circle.draw(canvas);
            BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(bitmap);

            /*
            Drawable circle = getResources().getDrawable(R.drawable.circle_shape);
Canvas canvas = new Canvas();
Bitmap bitmap = Bitmap.createBitmap(circle.getIntrinsicWidth(), circle.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
canvas.setBitmap(bitmap);
circle.setBounds(0, 0, circle.getIntrinsicWidth(), circle.getIntrinsicHeight());
circle.draw(canvas);
BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(bitmap);

map.addMarker(new MarkerOptions()
                .position(new LatLng(41.906991, 12.453360))
                .title("My Marker")
                .icon(bd)
);
             */

            mMap.addMarker(new MarkerOptions().position(location).title(v.getRouteNumber()).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

           // mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
           // mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
        }

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
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