package com.example.apprute.Maps;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.example.apprute.R;
import com.example.apprute.model.TouristAttraction;
import com.example.apprute.service.ApiInterface;
import com.example.apprute.service.BaseUrl;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.android.SphericalUtil;

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
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapLandActivity extends AppCompatActivity implements OnMapReadyCallback,TaskLoadedCallback {
    private GoogleMap mMap;
    private MarkerOptions markerOptions[];
    private List<Double> latlngOri;
    ApiInterface apiInterface;
    ArrayList<LatLng> locationArrayList, tourList,foodList;;
    private LatLng ori, dest, land, dest2,ori2;
    ArrayList<String> duration;
    TextView tv_totaldistance_land,tv_estimasi_land,tv_disdur_route1_land,tv_route1_land,tv_totalduration_land,tv_nameportroute_land;
    private Double distance;
    double latitude_dest, longitude_dest, lat_ori, lng_ori;
    String location,time,name_tour;
    ArrayList<Integer> Listdistance;
    ArrayList<Double> ListDistance;
    private Polyline currentPolyline;
    int id_tour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_land);
        location =getIntent().getStringExtra("location");
        apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        time = getIntent().getStringExtra("time");
        latitude_dest = getIntent().getDoubleExtra("latitude",0);
        longitude_dest = getIntent().getDoubleExtra("longitude",0);
        tv_totaldistance_land = findViewById(R.id.tv_totaldistance_land);
        tv_estimasi_land = findViewById(R.id.tv_estimasi_land);
        tv_disdur_route1_land = findViewById(R.id.tv_disdur_route1_land);
        tv_route1_land = findViewById(R.id.tv_route1_land);
        tv_totalduration_land = findViewById(R.id.tv_totalduration_land);
        tv_nameportroute_land = findViewById(R.id.tv_nameportroute_land);
        System.out.println("sewmua "+ location+" "+ time+" "+latitude_dest+" "+ longitude_dest);
        id_tour = getIntent().getIntExtra("id_tour",0);
            System.out.println("id tour "+id_tour);
        tourList = new ArrayList<>();
        foodList = new ArrayList<>();
            lat_ori = getLocationFromAddress().getLatitude();
            lng_ori = getLocationFromAddress().getLongitude();
            ori = new LatLng(lat_ori, lng_ori);
            dest = new LatLng(2.624618, 98.68488599999999);
            ori2 = new LatLng(2.624618, 98.68488599999999);
            dest2 = new LatLng(latitude_dest, longitude_dest);
            locationArrayList = new ArrayList<>();
            duration = new ArrayList<>();
            Listdistance = new ArrayList<>();
            duration = new ArrayList<>();
            ListDistance = new ArrayList<>();
            locationArrayList.add(ori);
            locationArrayList.add(dest);
            locationArrayList.add(ori2);
            locationArrayList.add(dest2);
        getTouristAttraction();
            List<String> urls = getDirectionsUrl(locationArrayList, "driving");
            if (urls.size() > 1) {
                for (int i = 0; i < urls.size(); i++) {
                    if (i == 1) {
                    } else {
                        String url = urls.get(i);
                        System.out.println("Rute " + url);
                        FetchURL fetchURL = new FetchURL(this);
                        fetchURL.execute(url, "driving");
                    }
                }
            }

            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.mapNearByLand);
            mapFragment.getMapAsync(this);
            if(id_tour!=0){
                getDetailTouristAttraction(id_tour);
            }
        }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String address = "",city="",country="",postalCode = "",title="",state="";
        System.out.println("mark "+ tourList.size() + " mark 2" + foodList.size());
        for(int i = 0;i<tourList.size();i++){
            mMap.addMarker(new MarkerOptions().position(tourList.get(i)).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }
        for (int i = 0; i< foodList.size();i++){
            mMap.addMarker(new MarkerOptions().position(foodList.get(i)).title(title).
                    icon(bitmapDescriptorFromVector(MapLandActivity.this,R.drawable.ic_baseline_restaurant_24)).title("Makanan"));
        }
            for (int i = 0; i < locationArrayList.size(); i++) {
                mMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title("Marker"+i));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
            }
            for (int i=0; i<locationArrayList.size();i++){
                if(i%2==0){
                    ListDistance.add(SphericalUtil.computeDistanceBetween(locationArrayList.get(i), locationArrayList.get(i+1)));
                }
            }

        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(2.6274,98.7922))
                .zoom(10)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5000, null);
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
    public Address getLocationFromAddress() {
        Geocoder coder = new Geocoder(this);
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
    public void onTaskDone(Object... values) {
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
            int hours2 = (s2 / 60) / 60;
            strmin2 = (min2 < 10) ? "0" + Integer.toString(min2) : Integer.toString(min2);
            strHours2 = (hours2 < 10) ? "0" + Integer.toString(hours2) : Integer.toString(hours2);
        }
        System.out.println("Total Duration : "+strHourstot1 + " Jam " + strmintot1 + " Menit");
        getTimeEstimation(total/60);
        tv_totalduration_land.setText("Total Duration : "+strHourstot1 + " Jam " + strmintot1 + " Menit");
        tv_disdur_route1_land.setText(""+strHourstot1 + " Jam " + strmintot1 + " Menit");


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getTimeEstimation(int duration_travel){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        Instant now = Instant.now();
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
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
            cal.add(Calendar.MINUTE, duration_travel);
            timeRoute1 = df.format(cal.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("time Route"+ timeRoute1+" "+time+" " + duration_travel);
        tv_estimasi_land.setText(timeRoute1);
    }

    @Override
    public void onDistance(Object... values) {
        String value = values[0].toString();
        Listdistance.add(Integer.parseInt(value));
        int total = 0;
        for (int i = 0; i < Listdistance.size(); i++) {
            if (Listdistance.size() > 1) {
                int s1 = Listdistance.get(1)/1000;
                total =Listdistance.get(0) + Listdistance.get(1);
            }
            int s2 = Listdistance.get(0)/1000;
        }
        tv_totaldistance_land.setText("Total Distance : "+total/1000+" KM");
    }
    private void  getDetailTouristAttraction(int id_tour){

        Call<TouristAttraction> call = apiInterface.getDetailTouristAttraction(id_tour);
        call.enqueue(new Callback<TouristAttraction>() {
            @Override
            public void onResponse(Call<TouristAttraction> call, Response<TouristAttraction> response) {
                System.out.println(response);
                System.out.println(" Ajibata" + location);
                if (!response.isSuccessful()) {
                    return;
                }else{
                    TouristAttraction touristAttraction = response.body();
                    name_tour = touristAttraction.getName();
                    tv_route1_land.setText(location+" - "+name_tour);
                    tv_nameportroute_land.setText(location+" - "+name_tour);
                    System.out.println("Locationnnnn "+ location);

                }

            }

            @Override
            public void onFailure(Call<TouristAttraction>call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private void getTouristAttraction(){
        Call<List<TouristAttraction>> listCall = apiInterface.getTouristAttraction();
        Call<List<TouristAttraction>> foodCall = apiInterface.getFoodDestination();

        listCall.enqueue(new Callback<List<TouristAttraction>>() {
            @Override
            public void onResponse(Call<List<TouristAttraction>> call, Response<List<TouristAttraction>> response) {
                List<TouristAttraction> touristAttractions = response.body();
                SphericalUtil.computeDistanceBetween(dest, dest2);
                LatLng location_dest = null;
                for(int i = 0;i<touristAttractions.size();i++){
                    location_dest = new LatLng(touristAttractions.get(i).getLatitude(), touristAttractions.get(i).getLongitude());
                    if (SphericalUtil.computeDistanceBetween(location_dest, dest2)<5000){
                        tourList.add(new LatLng(touristAttractions.get(i).getLatitude(), touristAttractions.get(i).getLongitude()));
                    }
                    location_dest = null;
                }
            }

            @Override
            public void onFailure(Call<List<TouristAttraction>> call, Throwable t) {

            }
        });
        foodCall.enqueue(new Callback<List<TouristAttraction>>() {
            @Override
            public void onResponse(Call<List<TouristAttraction>> call, Response<List<TouristAttraction>> response) {
                List<TouristAttraction> touristAttractionsFood = response.body();
                LatLng location_dest = null;
                for(int i = 0;i<touristAttractionsFood.size();i++){
                    location_dest = new LatLng(touristAttractionsFood.get(i).getLatitude(), touristAttractionsFood.get(i).getLongitude());
                    if (SphericalUtil.computeDistanceBetween(location_dest, dest2)<5000){
                        foodList.add(new LatLng(touristAttractionsFood.get(i).getLatitude(), touristAttractionsFood.get(i).getLongitude()));
                    }
                    location_dest = null;
                }
            }

            @Override
            public void onFailure(Call<List<TouristAttraction>> call, Throwable t) {

            }
        });
    }
}