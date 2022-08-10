package com.example.apprute;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.apprute.Adapter.PortRouteAdapter;
import com.example.apprute.Maps.FetchURL;
import com.example.apprute.Maps.MapLandActivity;
import com.example.apprute.Maps.TaskLoadedCallback;
import com.example.apprute.Maps.TaskLoadedTimeEstimation;
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
import com.google.android.gms.maps.model.Polyline;
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

public class OptionRouteActivity extends AppCompatActivity implements OnMapReadyCallback,TaskLoadedCallback,TaskLoadedTimeEstimation{
    private RecyclerView recyclerView;
    TaskLoadedTimeEstimation taskLoadedTimeEstimation;
    ApiInterface apiInterface;
    LinearLayout linearLayout;
    int id;
    double lat,lng,latitude, longitude;;
    String time,location;
    ArrayList<String> timeEstimation;
    private GoogleMap mMap;
    private MarkerOptions markerOptions[];
    private List<Double> latitude_harbor,longitude_harbor, latlngOri;
    ArrayList<String> duration;
    ArrayList<Integer> Listdistance;
    ArrayList<LatLng> locationArrayList, touristAtrractionLocationList, tourList;
    private LatLng ori, dest, ori2, dest2;
    ArrayList<Double> ListDistance;
    int duration_1, duration_2 = 0;
    double latitude_tour,longitude_tour;
    String timeRoute1 = "";
    String timeRoute2 = "";
    String FinalTime  = "";

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_route);
        recyclerView = findViewById(R.id.recyclerview_tourist_attraction_choice_fragment);
        linearLayout = findViewById(R.id.land_route);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        latitude_harbor = new ArrayList();
        longitude_harbor = new ArrayList();
        timeEstimation = new ArrayList<>();
        latlngOri = new ArrayList<>();
        Intent intent = getIntent();
        if (intent!=null){
            id = intent.getIntExtra("id_tourist_attraction",0);
            lat = intent.getDoubleExtra("lat_input",0);
            lng = intent.getDoubleExtra("lng_input",0);
            time = intent.getStringExtra("time");
            location = intent.getStringExtra("location");


        }
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id>0){
                    Intent intent = new Intent(OptionRouteActivity.this, MapLandActivity.class);
                    intent.putExtra("time", time);
                    intent.putExtra("location", location);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("id_tour",id);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(OptionRouteActivity.this, MapLandActivity.class);
                    intent.putExtra("time", time);
                    intent.putExtra("location", location);
                    intent.putExtra("latitude", lat);
                    intent.putExtra("longitude", lng);
                    startActivity(intent);
                }
            }
        });
        getDetailTouristAttraction(id);
//       getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

    }
    @Override
    public void onTaskDone(Object... values) {
        System.out.println("Berhasil kali : ");
    }
    @Override
    public void onDistanceDuration(Object... values) {
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

    }

    @Override
    public void onDistance(Object... values) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("XXX");
        mMap = googleMap;
        for (int i = 0; i < locationArrayList.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
    }
    private void getDetailTouristAttraction(int id){
        Call<TouristAttraction> call = apiInterface.getDetailTouristAttraction(id);
        call.enqueue(new Callback<TouristAttraction>() {
            @Override
            public void onResponse(Call<TouristAttraction> call, Response<TouristAttraction> response) {
                System.out.println(response);
                ProgressDialog progressDialog = new ProgressDialog(OptionRouteActivity.this);
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
        System.out.println("size time "+ duration_2);
        Call<List<PortRoute>> listCall = apiInterface.getRoute();
        final int[] i = {0};
        listCall.enqueue(new Callback<List<PortRoute>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<PortRoute>> call, Response<List<PortRoute>> response) {
                List<PortRoute> portRoutes = response.body();

                for (i[0] =0; i[0] <portRoutes.size(); i[0]++){
                    getSchedule(portRoutes.get(i[0]).getId(), duration_1, duration_2, new TimeCallback() {
                        @Override
                        public void timeEstimationCallback(String string) {
                            timeEstimation.add(string);
                            if (timeEstimation.size() == 6){
                                System.out.println("berhasil  3");
                                PortRouteAdapter portRouteAdapter = new PortRouteAdapter(OptionRouteActivity.this,portRoutes,lat,lng,time,location,id, timeEstimation);
                                recyclerView.setAdapter(portRouteAdapter);
                            }
                        }

                    });
                }

            }
            @Override
            public void onFailure(Call<List<PortRoute>> call, Throwable t) {

            }
        });
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
    private void getLatLngFromAndroid(){
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
                    FetchURL fetchURL = new FetchURL(OptionRouteActivity.this);
                    fetchURL.execute(url, "driving");
                }
            }
        }
    }
    public void getHarborRoute(int id){
        Call<List<HarborVM>> call = apiInterface.getHarborRoute(id);
        call.enqueue(new Callback<List<HarborVM>>() {
            @Override
            public void onResponse(Call<List<HarborVM>> call, Response<List<HarborVM>> response) {
                List<HarborVM> harborVM = response.body();
                latitude_harbor.add(harborVM.get(0).getLatitude());
                latitude_harbor.add(harborVM.get(1).getLatitude());
                longitude_harbor.add(harborVM.get(0).getLongitude());
                longitude_harbor.add(harborVM.get(1).getLongitude());
                latlngOri.add(  getLocationFromAddress().getLatitude());
                latlngOri.add(getLocationFromAddress().getLongitude());
                getLatLngFromAndroid();
            }
            @Override
            public void onFailure(Call<List<HarborVM>> call, Throwable t) {
            }
        });
    }
    public interface TimeCallback{
        void timeEstimationCallback(String string);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getSchedule(int id, int durationStart, int durationEnd,TimeCallback timeCallback){
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
                LocalTime time1 = LocalTime.parse(time);
                LocalTime time2 = LocalTime.parse(time);
                String timeStart = "";
                String timeEnd = "";
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
                time1 = LocalTime.parse(timeRoute1);
                int value = 0;
                time2 = LocalTime.parse(schedule.get(0).getTime().substring(0,5));
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
                timeStart = time1.toString();
                timeEnd = time2.toString();
                System.out.println("Final time0 : "+time1+" : "+ time2 );
                System.out.println("DUration End : "+ durationEnd);
                if (time1.compareTo(time2)>0){
                    System.out.println("Final time1 : "+FinalTime);
                    timeCallback.timeEstimationCallback(FinalTime+ " Next Day");
                }else {
                    System.out.println("Final time2 : "+FinalTime);
                    timeCallback.timeEstimationCallback(FinalTime);
                }


            }

            @Override
            public void onFailure(Call<List<Schedule>> call, Throwable t) {

            }
        });
    }


    public Address getLocationFromAddress() {
        Geocoder coder = new Geocoder(OptionRouteActivity.this);
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


    @Override
    public ArrayList<String> onTaskDone(ArrayList<String> arrayList) {
        System.out.println("Berhaisill");
        return arrayList;

    }

    @Override
    public void Test() {
        System.out.println("Berhasil kali ");
    }
}