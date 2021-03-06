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

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject resultSet = response.getJSONObject("resultSet");
                    JSONArray vehicles = resultSet.getJSONArray("vehicle");

                    ArrayList<Vehicle> vehiclesList = new ArrayList<>();

                    for (int i = 0; i < vehicles.length(); i++) {
                        JSONObject vehicle = vehicles.getJSONObject(i);
                        Vehicle v = new Vehicle();

                        int bearing = vehicle.getInt(ApiConstants.TRIMET_VEHICLE_BEARING);

                        if (bearing < 90) {
                            bearing += 270;
                        } else {
                            bearing -= 90;
                        }

                        v.setLatitude((float) vehicle.getDouble(ApiConstants.TRIMET_VEHICLE_LATITUDE));
                        v.setLongitude((float) vehicle.getDouble(ApiConstants.TRIMET_VEHICLE_LONGITUDE));
                        v.setRouteNumber(vehicle.getString(ApiConstants.TRIMET_VEHICLE_ROUTE_NUMBER));
                        v.setBearing(bearing);
                        v.setSignMessageLong(vehicle.getString(ApiConstants.TRIMET_VEHICLE_SIGN_MESSAGE_LONG));

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
                //TODO: should probably have a different callback function in case of error
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // avoid data caching on the device, which can cause 500 errors
        req.setShouldCache(false);
        // add the request to the request queue
        AppController.getInstance().addToRequestQueue(req);
    }

}
