package com.example.catalyst.trimettracker.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.catalyst.trimettracker.Arrow;
import com.example.catalyst.trimettracker.TextDrawable;
import com.example.catalyst.trimettracker.events.LocationEvent;
import com.example.catalyst.trimettracker.events.RouteEvent;
import com.example.catalyst.trimettracker.models.Route;
import com.example.catalyst.trimettracker.models.Vehicle;
import com.example.catalyst.trimettracker.network.ApiCaller;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import com.example.catalyst.trimettracker.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsloane on 7/8/2016.
 */
public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private final String TAG = MainActivity.class.getSimpleName();

    private Spinner spinner;

    final Handler timerHandler = new Handler();
    // runnable that will run with the timerHandler
    private Runnable timerRunnable;

    private GoogleMap mMap;
   // private Bitmap map;

    private List<Route> routeList;

    private ApiCaller caller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        spinner = (Spinner) findViewById(R.id.spinner);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.getUiSettings().setRotateGesturesEnabled(false);

        // initialize the map to the Portland area
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.5231, -122.6765), 9.5f));

        // disable zooming out beyond the Portland area
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                float minZoom = 8.0f;

                if (position.zoom < minZoom) {
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(minZoom));
                }
            }
        });


        //LatLngBounds bounds = new LatLngBounds(new LatLng(46.9754, -121), new LatLng(44.6368, -124));



        caller = new ApiCaller();

        caller.getRoutes();

/*
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                caller.getVehicleLocation("3101");
                timerHandler.postDelayed(this, 10000);
            }
        };
        timerRunnable.run();

 */
    }

    @Subscribe
    public void updateRoutes(RouteEvent event) {

        routeList = event.getRoutes();

        List<String> descriptions = new ArrayList<>();

        for (Route route: routeList) {
            descriptions.add(route.getDescription());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, descriptions);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                timerHandler.removeCallbacks(timerRunnable);
                timerRunnable = new Runnable() {
                    @Override
                    public void run() {
                        caller.getVehicleLocations(routeList.get(position).getRouteNumber());
                        timerHandler.postDelayed(this, 10000);
                    }
                };
                timerRunnable.run();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Subscribe
    public void updateLocation(LocationEvent event) {
        mMap.clear();
        for (Vehicle v : event.getVehicles()) {
            LatLng location = new LatLng(v.getLatitude(), v.getLongitude());

            // add the arrow to point the direction the vehicle is traveling to the map
            Arrow arrow = new Arrow(this, v.getRouteNumber(), v.getBearing());
            Bitmap arrowMap = Bitmap.createBitmap(arrow.getIntrinsicWidth(), arrow.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas();
            canvas.setBitmap(arrowMap);
            arrow.setBounds(0, 0, canvas.getWidth(), canvas.getHeight()-64);
            arrow.draw(canvas);
            mMap.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory.fromBitmap(arrowMap)).anchor(0.5f, 0.5f));

            // add the circle containing the vehicle route number to the map
            TextDrawable drawable = new TextDrawable(this, v.getRouteNumber());
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            canvas = new Canvas();
            canvas.setBitmap(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            mMap.addMarker(new MarkerOptions().position(location).title(v.getSignMessageLong()).icon(BitmapDescriptorFactory.fromBitmap(bitmap)).anchor(0.5f, 0.5f));
        }

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