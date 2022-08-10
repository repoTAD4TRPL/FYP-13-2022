package com.example.apprute.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.apprute.Adapter.PortRouteAdapter;
import com.example.apprute.Maps.FetchURL;
import com.example.apprute.Maps.MapLandActivity;
import com.example.apprute.Maps.TaskLoadedCallback;
import com.example.apprute.R;
import com.example.apprute.model.HarborVM;
import com.example.apprute.model.PortRoute;
import com.example.apprute.model.Schedule;
import com.example.apprute.model.TouristAttraction;
import com.example.apprute.service.ApiInterface;
import com.example.apprute.service.BaseUrl;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OptionFragment extends Fragment implements OnMapReadyCallback,TaskLoadedCallback {
    private RecyclerView recyclerView;
    ApiInterface apiInterface;
    private MarkerOptions markerOptions[];
    ArrayList<Double> latlng;
    double latitude, longitude;
    int port_id,id = 0, duration_1,duration_2;
    String time="", location="";
    double lat=0,lng=0;
    LinearLayout linearLayout;
    ArrayList<Integer> Listdistance;
    ArrayList<String> duration;
    ArrayList<LatLng> locationArrayList, touristAtrractionLocationList, tourList;
    ArrayList<Double> ListDistance;
    double latitude_tour,longitude_tour;
    ArrayList<Integer> idRouteList;
    private LatLng ori, dest, ori2, dest2;
    private List<Double> latitude_harbor,longitude_harbor, latlngOri;
    GoogleMap mMap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        latlng = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_option, container, false);
        recyclerView = view.findViewById(R.id.recyclerview_tourist_attraction_choice_fragment);
        linearLayout = view.findViewById(R.id.land_route);
        duration = new ArrayList<>();
        ListDistance = new ArrayList<>();
        latitude_harbor = new ArrayList<>();
        longitude_harbor = new ArrayList<>();
        locationArrayList = new ArrayList<>();
        idRouteList = new ArrayList<>();
        latlngOri = new ArrayList<>();
        duration = new ArrayList<>();
        Listdistance = new ArrayList<>();
        locationArrayList = new ArrayList<>();
        touristAtrractionLocationList = new ArrayList<>();
        tourList = new ArrayList<>();
        Listdistance = new ArrayList<>();
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        Bundle bundle = this.getArguments();
        if (bundle!=null){
            id = bundle.getInt("id_tourist_attraction");
            lat = bundle.getDouble("lat_input");
            lng = bundle.getDouble("lng_input");
            time = bundle.getString("time");
            location = bundle.getString("location");
        }
        if (id==0){
            getAllPortRoute(lat,lng);
        }else{
            getDetailTouristAttraction(id);
        }
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id>0){
                    Intent intent = new Intent(getContext(), MapLandActivity.class);
                    intent.putExtra("time", time);
                    intent.putExtra("location", location);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("id_tour",id);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getContext(), MapLandActivity.class);
                    intent.putExtra("time", time);
                    intent.putExtra("location", location);
                    intent.putExtra("latitude", lat);
                    intent.putExtra("longitude", lng);
                    startActivity(intent);
                }
            }
        });
        getHarborRoute(18);
        return view;
    }

    private void getDetailTouristAttraction(int id){
        Call<TouristAttraction> call = apiInterface.getDetailTouristAttraction(id);
        call.enqueue(new Callback<TouristAttraction>() {
            @Override
            public void onResponse(Call<TouristAttraction> call, Response<TouristAttraction> response) {
                System.out.println(response);
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// There are 3 styles, You'll figure it out :)
                progressDialog.setCancelable(false);
                progressDialog.setIcon(R.drawable.ic_baseline_access_time_24);
                if (!response.isSuccessful()){
                    progressDialog.show();
                }
                progressDialog.cancel();
                TouristAttraction touristAttraction = response.body();
                latitude = touristAttraction.getLatitude();
                longitude = touristAttraction.getLongitude();
                getAllPortRoute(latitude, longitude);
            }

            @Override
            public void onFailure(Call<TouristAttraction>call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private void getAllPortRoute(double lat, double lng){
        Call<List<PortRoute>> listCall = apiInterface.getRoute();
        listCall.enqueue(new Callback<List<PortRoute>>() {
            @Override
            public void onResponse(Call<List<PortRoute>> call, Response<List<PortRoute>> response) {
                List<PortRoute> portRoutes = response.body();
//                mapMark.Main(portRoutes.get(1).getId());
                PortRouteAdapter portRouteAdapter = new PortRouteAdapter(getView().getContext(),portRoutes,lat,lng,time,location,id, new ArrayList<>());
                recyclerView.setAdapter(portRouteAdapter);
            }
            @Override
            public void onFailure(Call<List<PortRoute>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onTaskDone(Object... values) {
        System.out.println("amin");
    }

    @Override
    public void onDistanceDuration(Object... values) {
        System.out.println("ondistanceDuration");
        String output = values[0].toString();
        duration.add(output);
        Listdistance.add(Integer.parseInt(output));
        int total_d = 0, d1 = 0, d2 = 0;
        int min1 = 0, s1=0,s2 = 0, hours1=0, total=0;
        String strHours1="",strHourstot1="",strmin1="",strmintot1="";
        String strHours2="",strmin2="";
        for (int i = 0; i < duration.size(); i++) {
            if (duration.size() > 1) {
                s1 = Integer.parseInt(duration.get(1));
                min1 = (s1 / 60) % 60;
                duration_1 = s1/60;
                hours1 = (s1 / 60) / 60;
                System.out.println("Hours" + hours1);
                strmin1 = (min1 < 10) ? "0" + Integer.toString(min1) : Integer.toString(min1);
                strHours1 = (hours1 < 10) ? "0" + Integer.toString(hours1) : Integer.toString(hours1);
                total = Integer.parseInt(duration.get(0)) + Integer.parseInt(duration.get(1));
                int mintot = (total / 60) % 60;
                int hourstot = (total / 60) / 60;
                strmintot1= (mintot < 10) ? "0" + Integer.toString(mintot) : Integer.toString(mintot);
                strHourstot1 = (hourstot < 10) ? "0" + Integer.toString(hourstot) : Integer.toString(hourstot);
            }
            s2 = Integer.parseInt(duration.get(0));
            int min2 = (s2 / 60) % 60;
            duration_2 = s2/60;
            int hours2 = (s2 / 60) / 60;
            strmin2 = (min2 < 10) ? "0" + Integer.toString(min2) : Integer.toString(min2);
            strHours2 = (hours2 < 10) ? "0" + Integer.toString(hours2) : Integer.toString(hours2);
        }
        getSchedule(port_id,duration_1,duration_2);
    }

    @Override
    public void onDistance(Object... values) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (int i = 0; i < locationArrayList.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
    }
    private List<String> getDirectionsUrl(ArrayList<LatLng> markerPoints, String directionMode) {

        List<String> mUrls = new ArrayList<>();
        if (markerPoints.size() > 1) {
            String str_origin = markerPoints.get(0).latitude + "," + markerPoints.get(0).longitude;
            String str_dest = markerPoints.get(1).latitude + "," + markerPoints.get(1).longitude;

            String mode = "mode="+directionMode;
            String parameters = "origin=" + str_origin + "&destination=" + str_dest + "&" + mode;
            String output = "json";
            String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters+ "&key=" + getString(R.string.google_maps_key);
            System.out.println(str_origin);
            mUrls.add(url);
            for (int i = 2; i < markerPoints.size(); i++)//loop starts from 2 because 0 and 1 are already printed
            {
                str_origin = str_dest;
                str_dest = markerPoints.get(i).latitude + "," + markerPoints.get(i).longitude;
                parameters = "origin=" + str_origin + "&destination=" + str_dest + "&" + mode;
                url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters+ "&key=" + getString(R.string.google_maps_key);
                mUrls.add(url);
            }
        }

        return mUrls;
    }
    private void getHarborRoute(int id){
        Call<List<HarborVM>> call = apiInterface.getHarborRoute(id);
        call.enqueue(new Callback<List<HarborVM>>() {
            @Override
            public void onResponse(Call<List<HarborVM>> call, Response<List<HarborVM>> response) {
                List<HarborVM> harborVM = response.body();
                latitude_harbor.add(harborVM.get(0).getLatitude());
                latitude_harbor.add(harborVM.get(1).getLatitude());
                longitude_harbor.add(harborVM.get(0).getLongitude());
                longitude_harbor.add(harborVM.get(1).getLongitude());
                latlngOri.add(getLocationFromAddress().getLatitude());
                latlngOri.add(getLocationFromAddress().getLongitude());
                getLatLngFromAndroid();
            }
            @Override
            public void onFailure(Call<List<HarborVM>> call, Throwable t) {
            }
        });
    }
    private void getLatLngFromAndroid(){
        System.out.println("latitude_size"+latitude_harbor.size());

        for (int i=0;i<latitude_harbor.size();i++){
            System.out.println("Latitude"+latitude_harbor.get(i));
        }
        ori = new LatLng(latlngOri.get(0),latlngOri.get(1));
        dest = new LatLng(latitude_harbor.get(0),longitude_harbor.get(0));
        ori2 = new LatLng(latitude_harbor.get(1), longitude_harbor.get(1));
        dest2 = new LatLng(latitude_tour,longitude_tour);
        duration = new ArrayList<>();
        touristAtrractionLocationList = new ArrayList<>();
        locationArrayList = new ArrayList<>();
        tourList = new ArrayList<>();
        Listdistance = new ArrayList<>();
        ListDistance = new ArrayList<>();
        locationArrayList.add(ori);
        locationArrayList.add(dest);
        locationArrayList.add(ori2);
        locationArrayList.add(dest2);
        List<String> urls = getDirectionsUrl(locationArrayList,"driving");
        if (urls.size() > 1) {
            for (int i = 0; i < urls.size(); i++) {
                if(i==1){
                }else {
                    String url = urls.get(i);
                    FetchURL fetchURL = new FetchURL(getContext());
                    fetchURL.execute(url, "driving");
                }
            }
        }
}

    private void getSchedule(int id, int durationStart, int durationEnd){
        System.out.println("Berhasil kakkakakak");
        Call<List<Schedule>> call = apiInterface.getSchedule(id);
        call.enqueue(new Callback<List<Schedule>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<Schedule>> call, Response<List<Schedule>> response) {
                List<Schedule> schedule = response.body();
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
                Instant now = Instant.now();
                ZoneId zoneId = ZoneId.of("Asia/Jakarta");
                String TimeSchedule = schedule.get(0).getTime().substring(0,5);
                ZonedDateTime dateCurrent = ZonedDateTime.ofInstant(now, zoneId);
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                String timeRoute1 = "";
                String timeRoute2 = "";
                String FinalTime  = "";
                Calendar cal = Calendar.getInstance();
                Date d = null;
                try {
                    d = df.parse(time);
                    cal.setTime(d);
                    cal.add(Calendar.MINUTE, durationEnd);
                    timeRoute1 = df.format(cal.getTime());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                LocalTime time1= LocalTime.parse(timeRoute1);
                int value = 0;
                LocalTime time2 = LocalTime.parse(schedule.get(0).getTime().substring(0,5));
                value = time1.compareTo(time2);
                for (int i=1;i<schedule.size();i++){
                    value = time1.compareTo(time2);
                    if (value < 0){
                        if (i>1){
                            time2 = LocalTime.parse(schedule.get(i-1).getTime().substring(0,5));
                            break;
                        }
                    }
                    time2 = LocalTime.parse(schedule.get(i).getTime().substring(0,5));
                }
                if (time1.compareTo(time2)>0){
                    time2 = LocalTime.parse(schedule.get(0).getTime().substring(0,5));
                }
                Date d2 = null;
                try {
                    d2 = df.parse(fmt.format(time2));
                    cal.setTime(d2);
                    cal.add(Calendar.MINUTE, 60);
                    timeRoute2 = df.format(cal.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date d3 = null;
                try {
                    d3 = df.parse(timeRoute2);
                    cal.setTime(d3);
                    cal.add(Calendar.MINUTE, durationStart);
                    FinalTime = df.format(cal.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (time1.compareTo(time2)>0){
                    System.out.println("time "+ FinalTime);
                }else {
                    System.out.println("time 2 "+FinalTime );
                }
            }

            @Override
            public void onFailure(Call<List<Schedule>> call, Throwable t) {

            }
        });
    }
    public Address getLocationFromAddress() {
        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(location, 20);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            return location;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}