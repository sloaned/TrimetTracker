package com.example.catalyst.trimettracker.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.catalyst.trimettracker.AppController;
import com.example.catalyst.trimettracker.events.LocationEvent;
import com.example.catalyst.trimettracker.models.Vehicle;
import com.example.catalyst.trimettracker.util.ApiConstants;
import com.example.catalyst.trimettracker.util.NetworkConstants;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by dsloane on 7/8/2016.
 */
public class ApiCaller {

    // used for logging statements
    public static final String TAG = ApiCaller.class.getSimpleName();

    public void getVehicleLocation(String vehicleId) {
        String url = NetworkConstants.TRIMET_VEHICLES_API + "/" + ApiConstants.TRIMET_QUERY_SUFFIX;
        //"ids/" + vehicleId +

        Log.d(TAG, "url = " + url);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "something worked");
                try {
                    JSONObject resultSet = response.getJSONObject("resultSet");
                    JSONArray vehicles = resultSet.getJSONArray("vehicle");

                    ArrayList<Vehicle> vehiclesList = new ArrayList<>();

                    for (int i = 0; i < vehicles.length(); i++) {
                        JSONObject vehicle = vehicles.getJSONObject(i);
                        Vehicle v = new Vehicle();

                        float longitude = (float) vehicle.getDouble("longitude");
                        float latitude = (float) vehicle.getDouble("latitude");
                        String routeNumber = vehicle.getString("routeNumber");
                        v.setLatitude(latitude);
                        v.setLongitude(longitude);
                        v.setRouteNumber(routeNumber);

                        vehiclesList.add(v);
                    }


                    EventBus.getDefault().post(new LocationEvent(vehiclesList));

                } catch (JSONException e) {
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "got a bad error");
                //TODO: should probably have a different callback function in case of error
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        Log.d(TAG, "req = " + req);
        // avoid data caching on the device, which can cause 500 errors
        req.setShouldCache(false);
        // add the request to the request queue
        AppController.getInstance().addToRequestQueue(req);
    }

}
