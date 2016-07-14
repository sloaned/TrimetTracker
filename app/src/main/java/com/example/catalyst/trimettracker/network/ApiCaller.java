package com.example.catalyst.trimettracker.network;

import android.util.Log;
import android.util.Xml;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.catalyst.trimettracker.AppController;
import com.example.catalyst.trimettracker.events.LocationEvent;
import com.example.catalyst.trimettracker.events.RouteEvent;
import com.example.catalyst.trimettracker.models.Route;
import com.example.catalyst.trimettracker.models.Vehicle;
import com.example.catalyst.trimettracker.util.ApiConstants;
import com.example.catalyst.trimettracker.util.NetworkConstants;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dsloane on 7/8/2016.
 */
public class ApiCaller {

    // used for logging statements
    public static final String TAG = ApiCaller.class.getSimpleName();

    public void getRoutes() {
        String url = NetworkConstants.TRIMET_ROUTES_API + ApiConstants.TRIMET_QUERY_SUFFIX;

        Log.d(TAG, "about to get routes");

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               Log.d(TAG, response.toString());
               InputStream stream = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));

               List<Route> routeList = new ArrayList<>();

               try {
                   XmlPullParser parser = Xml.newPullParser();

                   parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

                   parser.setInput(stream, null);
                   parser.nextTag();

                   parser.require(XmlPullParser.START_TAG, null, "resultSet");
                   while (parser.next() != XmlPullParser.END_TAG) {
                       if (parser.getEventType() != XmlPullParser.START_TAG) {
                           continue;
                       }
                       String name = parser.getName();
                       Log.d(TAG, "name = " + name);

                       if (name.equals("route")) {
                           parser.require(XmlPullParser.START_TAG, null, "route");

                           Route route = new Route();
                           String name2 = parser.getName();
                           Log.d(TAG, "name2 = " + name2);
                           for (int i = 0; i < parser.getAttributeCount(); i++) {
                               Log.d(TAG, "attribute = " + parser.getAttributeName(i));
                               if (parser.getAttributeName(i).equals("desc")) {
                                   String desc = parser.getAttributeValue(i);
                                   route.setDescription(desc);
                               }
                               if (parser.getAttributeName(i).equals("route")) {
                                   String routeNumber = parser.getAttributeValue(i);
                                   route.setRouteNumber(routeNumber);
                               }
                           }

                           routeList.add(route);

                           while(parser.next() != XmlPullParser.END_TAG) {
                               if (parser.getEventType() != XmlPullParser.START_TAG) {
                                   Log.d(TAG, "continuing");
                                   continue;
                               }
                               name2 = parser.getName();
                               Log.d(TAG, "name2 = " + name2);
                               if (name.equals("desc")) {
                                   Log.d(TAG, "name2 = desc");
                                   String desc = "";
                                   if (parser.next() == XmlPullParser.TEXT) {
                                       desc = parser.getText();
                                       parser.nextTag();

                                   }
                               } else {
                                   Log.d(TAG, "reached the inner else");
                                   if (parser.getEventType() != XmlPullParser.START_TAG) {
                                       throw new IllegalStateException();
                                   }
                                   int depth = 1;
                                   while (depth != 0) {
                                       switch (parser.next()) {
                                           case XmlPullParser.END_TAG:
                                               depth--;
                                               break;
                                           case XmlPullParser.START_TAG:
                                               depth++;
                                               break;
                                       }
                                   }
                               }
                           }
                       } else {
                           Log.d(TAG, "reached the outer else");
                           if (parser.getEventType() != XmlPullParser.START_TAG) {
                               throw new IllegalStateException();
                           }
                           int depth = 1;
                           while (depth != 0) {
                               switch (parser.next()) {
                                   case XmlPullParser.END_TAG:
                                       depth--;
                                       break;
                                   case XmlPullParser.START_TAG:
                                       depth++;
                                       break;
                               }
                           }
                       }
                   }

                   EventBus.getDefault().post(new RouteEvent(routeList));
               } catch(XmlPullParserException e) {
                   e.printStackTrace();
               } catch(IOException e) {
                   e.printStackTrace();
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




    public void getVehicleLocations(String routeNumber) {
        String url = NetworkConstants.TRIMET_VEHICLES_API + "routes/" + routeNumber + "/" + ApiConstants.TRIMET_QUERY_SUFFIX;
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
                        // correct for Trimet's wonky notion of a circle
                        bearing = 360-bearing;

                        if (bearing >= 270) {
                            bearing -= 270;
                        } else {
                            bearing += 90;
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
