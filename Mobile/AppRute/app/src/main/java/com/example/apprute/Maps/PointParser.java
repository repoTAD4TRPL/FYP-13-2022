package com.example.apprute.Maps;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class PointsParser extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
   TaskLoadedCallback taskCallback;
    String directionMode = "driving";
    String distance;
    String duration;
    int d1,d2;
    int total_distance, total_duration;

    public PointsParser(Context mContext, String directionMode) {
        this.taskCallback = (TaskLoadedCallback) mContext;
        this.directionMode = directionMode;
    }

    // Parsing the data in non-ui thread
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
            Log.d("mylog", jsonData[0].toString());
            DataParser parser = new DataParser();
            Log.d("mylog", parser.toString());

            // Starts parsing data
            routes = parser.parse(jObject);
            Log.d("mylog", "Executing routes");
            Log.d("mylog", routes.toString());

        } catch (Exception e) {
            Log.d("mylog", e.toString());
            e.printStackTrace();
        }
        return routes;
    }

    // Executes in UI thread, after the parsing process
    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points;
        PolylineOptions lineOptions = null;
        List<Integer> Ldistance = new ArrayList<>();
        List<Integer> Lduration = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();
            List<HashMap<String, String>> path = result.get(i);
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);
                if(j==0){    // Get distance from the list
                    distance = (String)point.get("distance");
                    Ldistance.add(Integer.parseInt(distance));
                    continue;
                }else if(j==1){
                    duration = (String)point.get("duration");
                    Lduration.add(Integer.parseInt(duration));
                    continue;
                }
                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);
                points.add(position);
            }
            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            if (directionMode.equalsIgnoreCase("driving")) {
                lineOptions.width(10);
                lineOptions.color(Color.MAGENTA);
            } else {
                lineOptions.width(20);
                lineOptions.color(Color.RED);
            }
            Log.d("mylog", "onPostExecute lineoptions decoded");
        }
        // Drawing polyline in the Google Map for the i-th route
        if (lineOptions != null) {
//            mMap.addPolyline(lineOptions);
            taskCallback.onTaskDone(lineOptions);
            taskCallback.onDistanceDuration(Lduration.get(0));
            taskCallback.onDistance(Ldistance.get(0));
        } else {
            Log.d("mylog", "without Polylines drawn");
        }
        total_duration = d2;
        System.out.println("Total Duration : " + Ldistance);
    }
}
