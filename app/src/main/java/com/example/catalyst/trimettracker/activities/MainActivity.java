package com.example.catalyst.trimettracker.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.catalyst.trimettracker.Arrow;
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

    private final String TAG = MainActivity.class.getSimpleName();

    final Handler timerHandler = new Handler();
    // runnable that will run with the timerHandler
    private Runnable timerRunnable;

    private GoogleMap mMap;
    private TextView tv;
    private Bitmap map;

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

        LinearLayout layout = (LinearLayout) ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.circle_text, null);


        tv = (TextView) layout.findViewById(R.id.circle_text);

        /*LatLng location = new LatLng(42.6049, -70.6527);

        mMap.addMarker(new MarkerOptions().position(location).title("Hey"));//.icon(BitmapDescriptorFactory.fromResource(R.drawable.vehicle_label)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
 */


        final ApiCaller caller = new ApiCaller();


        timerRunnable = new Runnable() {
            @Override
            public void run() {
                caller.getVehicleLocation("3101");
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

            Arrow arrow = new Arrow(this, v.getRouteNumber(), v.getBearing());
            Bitmap arrowMap = Bitmap.createBitmap(arrow.getIntrinsicWidth(), arrow.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas();
            canvas.setBitmap(arrowMap);
            arrow.setBounds(0, 0, canvas.getWidth(), canvas.getHeight()-64); // -64
            arrow.draw(canvas);
            mMap.addMarker(new MarkerOptions().position(location).title(v.getRouteNumber()).icon(BitmapDescriptorFactory.fromBitmap(arrowMap)));//.icon(BitmapDescriptorFactory.fromBitmap(bitmap)));


            TextDrawable drawable = new TextDrawable(this, v.getRouteNumber());
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            canvas = new Canvas();
            canvas.setBitmap(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            mMap.addMarker(new MarkerOptions().position(location).title(v.getRouteNumber()).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));//.icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
           // mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            //mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
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