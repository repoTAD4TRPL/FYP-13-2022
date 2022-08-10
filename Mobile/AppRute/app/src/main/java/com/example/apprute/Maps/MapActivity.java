package com.example.apprute.Maps;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.apprute.Adapter.TourisListAdapterFragment;
import com.example.apprute.R;
import com.example.apprute.model.Harbor;
import com.example.apprute.model.HarborVM;
import com.example.apprute.model.PortRoute;
import com.example.apprute.model.Schedule;
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
import java.time.format.DateTimeFormatter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,TaskLoadedCallback {
    TaskLoadedTimeEstimation  timeEstimationLoaded;
    private GoogleMap mMap;
    private MarkerOptions markerOptions[];
    ApiInterface apiInterface;
    private List<Double> latitude_harbor,longitude_harbor, latlngOri;
    TextView tv_nameportroute,tv_totalduration,tv_totaldistance,tv_estimasi,tv_route1,tv_disdur_route1,tv_routename,tv_disdur_route2,tv_disdur_route3,tv_route2;
    ArrayList<String> duration;
    ArrayList<Integer> Listdistance;
    ArrayList<LatLng> locationArrayList, touristAtrractionLocationList, tourList, foodList, hotelList, souvenirList;
    private Polyline currentPolyline;
    private LatLng ori, dest, ori2, dest2;
    ArrayList<Double> ListDistance;
    private Double distance;
    int port_id, duration_1, duration_2, id_tour;
    double latitude_tour,longitude_tour;
    String time, location, name_tour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        latitude_tour = getIntent().getDoubleExtra("latitude",0);
        longitude_tour = getIntent().getDoubleExtra("longitude",0);
        id_tour = getIntent().getIntExtra("id_tour",0);
        time = getIntent().getStringExtra("time");
        location = getIntent().getStringExtra("location");
        port_id = getIntent().getIntExtra("port_id",0);
        latitude_harbor = new ArrayList();
        longitude_harbor = new ArrayList();
        latlngOri = new ArrayList<>();
        getHarborRoute(port_id);
        getDetailTouristAttraction();
        getTouristAttraction();
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
        System.out.println("tourlist "+tourList.size());
        for(int i = 0;i<tourList.size();i++){
            mMap.addMarker(new MarkerOptions().position(tourList.get(i)).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }
        for (int i = 0; i< foodList.size();i++){
            mMap.addMarker(new MarkerOptions().position(foodList.get(i)).title(title).
                    icon(bitmapDescriptorFromVector(MapActivity.this,R.drawable.ic_baseline_restaurant_24)).title("Makanan"));
        }
        for (int i = 0; i< souvenirList.size();i++){
            mMap.addMarker(new MarkerOptions().position(souvenirList.get(i)).title(title).
                    icon(bitmapDescriptorFromVector(MapActivity.this,R.drawable.ic_baseline_card_giftcard_24)).title("Makanan"));
        }
        for (int i = 0; i< hotelList.size();i++){
            mMap.addMarker(new MarkerOptions().position(hotelList.get(i)).title(title).
                    icon(bitmapDescriptorFromVector(MapActivity.this,R.drawable.ic_baseline_hotel_24)).title("Makanan"));
        }
        for (int i = 0; i < locationArrayList.size(); i++) {
            Geocoder geocoder = new Geocoder(this,Locale.getDefault());
            List<Address> addresses;

            try {
                addresses = geocoder.getFromLocation(locationArrayList.get(i).latitude,locationArrayList.get(i).longitude,1);
                address = addresses.get(0).getAddressLine(0);
                title = address;
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
           mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));

        }
        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(2.6274,98.7922))
                .zoom(10)
                .build();
        distance = SphericalUtil.computeDistanceBetween(ori, dest)  ;
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5000, null);
    }
    @Override
    public void onTaskDone(Object... values) {
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
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

        for (int i = 0; i < Listdistance.size(); i++) {
            if (Listdistance.size() > 1) {
                d1 =Listdistance.get(1)/1000;
                total_d = Listdistance.get(0)+ Listdistance.get(1);
            }
                d2 = Listdistance.get(0)/1000;
        }
        tv_disdur_route1.setText(strHours2 + " Jam " + strmin2 + " Menit"+"                 "+d1+" KM");
        tv_disdur_route3.setText(strHours1 + " Jam " + strmin1 + " Menit"+"                 "+d2+" KM");
        tv_totalduration.setText("Total Duration : "+strHourstot1 + " Jam " + strmintot1 + " Menit");
        tv_totaldistance.setText("Total Distance : "+total_d/1000+" KM");
        getSchedule(port_id,duration_1,duration_2);
    }

    @Override
    public void onDistance(Object... values) {
        String value = values[0].toString();
        Listdistance.add(Integer.parseInt(value));
        for (int i = 0; i < Listdistance.size(); i++) {
            if (Listdistance.size() > 1) {
                int s1 = Listdistance.get(1)/1000;
                int total =Listdistance.get(0) + Listdistance.get(1);
            }
                int s2 = Listdistance.get(1)/1000;
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
        ori = new LatLng(latlngOri.get(0),latlngOri.get(1));
        dest = new LatLng(latitude_harbor.get(0),longitude_harbor.get(0));
        ori2 = new LatLng(latitude_harbor.get(1), longitude_harbor.get(1));
        dest2 = new LatLng(latitude_tour,longitude_tour);
        duration = new ArrayList<>();
        touristAtrractionLocationList = new ArrayList<>();
        locationArrayList = new ArrayList<>();
        tourList = new ArrayList<>();
        foodList = new ArrayList<>();
        hotelList = new ArrayList<>();
        souvenirList = new ArrayList<>();
        Listdistance = new ArrayList<>();
        ListDistance = new ArrayList<>();
        tv_nameportroute = findViewById(R.id.tv_nameportroute);
        tv_totalduration = findViewById(R.id.tv_totalduration);
        tv_totaldistance = findViewById(R.id.tv_totaldistance);
        tv_estimasi = findViewById(R.id.tv_estimasi);
        tv_route1 = findViewById(R.id.tv_route1);
        tv_disdur_route1 = findViewById(R.id.tv_disdur_route1);
        tv_routename = findViewById(R.id.tv_routename);
        tv_disdur_route2 = findViewById(R.id.tv_disdur_route2);
        tv_route2 = findViewById(R.id.tv_route2);
        tv_disdur_route3 = findViewById(R.id.tv_disdur_route3);
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
                    FetchURL fetchURL = new FetchURL(MapActivity.this);
                    fetchURL.execute(url, "driving");
                }
            }
        }
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);


    }

    private void getSchedule(int id, int durationStart, int durationEnd){
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
                    tv_estimasi.setText(FinalTime+ "  +Next Day");
                }else {
                    tv_estimasi.setText(FinalTime);
                }
            }

            @Override
            public void onFailure(Call<List<Schedule>> call, Throwable t) {

            }
        });
    }

    public void getDataRoute(int id_port){
        Call<PortRoute> call = apiInterface.getRoute(id_port);
        Call<List<Harbor>> call2 = apiInterface.getHarborPortRoute(id_port);
        call.enqueue(new Callback<PortRoute>() {
            @Override
            public void onResponse(Call<PortRoute> call, Response<PortRoute> response) {
                PortRoute portRoute = response.body();
                tv_nameportroute.setText(portRoute.getRouteName());
                tv_routename.setText(portRoute.getRouteName());
            }
            @Override
            public void onFailure(Call<PortRoute> call, Throwable t) {

            }
        });
        call2.enqueue(new Callback<List<Harbor>>() {
            @Override
            public void onResponse(Call<List<Harbor>> call, Response<List<Harbor>> response) {
                List<Harbor> harbors = response.body();
                tv_route1.setText(location+" - "+harbors.get(0).getHarborName());
                tv_route2.setText(harbors.get(1).getHarborName() +" - "+name_tour);
            }

            @Override
            public void onFailure(Call<List<Harbor>> call, Throwable t) {

            }
        });
    }
    private void  getDetailTouristAttraction(){
        Call<TouristAttraction> call = apiInterface.getDetailTouristAttraction(id_tour);
        call.enqueue(new Callback<TouristAttraction>() {
            @Override
            public void onResponse(Call<TouristAttraction> call, Response<TouristAttraction> response) {
                System.out.println(response);
                if (!response.isSuccessful()) {
                    return;
                }
                TouristAttraction touristAttraction = response.body();
                name_tour = touristAttraction.getName();
                getDataRoute(port_id);
            }

            @Override
            public void onFailure(Call<TouristAttraction>call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
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
    private List<String> getMarkObjectArroundl(ArrayList<LatLng> markerPoints, String directionMode) {

        List<String> mUrls = new ArrayList<>();
            String str_origin = markerPoints.get(0).latitude + "," + markerPoints.get(0).longitude;
            String str_dest = markerPoints.get(1).latitude + "," + markerPoints.get(1).longitude;
            String mode = "mode="+directionMode;
            String parameters = "origin=" + str_origin + "&destination=" + str_dest + "&" + mode;
            String output = "json";
            String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters+ "&key=" + getString(R.string.google_maps_key);
            System.out.println(str_origin);
            mUrls.add(url);
        return mUrls;
    }
    public void getObjectAround(){

    }
    private void getTouristAttraction(){
        System.out.println("Berhasil kali");
        Call<List<TouristAttraction>> listCall = apiInterface.getTouristAttraction();
        Call<List<TouristAttraction>> foodCall = apiInterface.getFoodDestination();
        Call<List<TouristAttraction>> souvenirCall = apiInterface.getSouvenirDestination();
        Call<List<TouristAttraction>> hotelCall = apiInterface.getHotelDestination();

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
                LatLng destination = dest2;
                System.out.println("destination "+destination);
                for(int i = 0;i<touristAttractionsFood.size();i++){
                    location_dest = new LatLng(touristAttractionsFood.get(i).getLatitude(), touristAttractionsFood.get(i).getLongitude());
                    if (SphericalUtil.computeDistanceBetween(location_dest, destination)<5000){
                        foodList.add(new LatLng(touristAttractionsFood.get(i).getLatitude(), touristAttractionsFood.get(i).getLongitude()));
                    }
                    location_dest = null;
                }
            }

            @Override
            public void onFailure(Call<List<TouristAttraction>> call, Throwable t) {

            }
        });
        souvenirCall.enqueue(new Callback<List<TouristAttraction>>() {
            @Override
            public void onResponse(Call<List<TouristAttraction>> call, Response<List<TouristAttraction>> response) {
                List<TouristAttraction> touristAttractionsFood = response.body();
                LatLng location_dest = null;
                LatLng destination = dest2;
                System.out.println("destination "+dest2);
                for(int i = 0;i<touristAttractionsFood.size();i++){
                    location_dest = new LatLng(touristAttractionsFood.get(i).getLatitude(), touristAttractionsFood.get(i).getLongitude());
                    if (SphericalUtil.computeDistanceBetween(location_dest, dest2)<5000){
                        souvenirList.add(new LatLng(touristAttractionsFood.get(i).getLatitude(), touristAttractionsFood.get(i).getLongitude()));
                    }
                    location_dest = null;
                }
            }

            @Override
            public void onFailure(Call<List<TouristAttraction>> call, Throwable t) {

            }
        });
        hotelCall.enqueue(new Callback<List<TouristAttraction>>() {
            @Override
            public void onResponse(Call<List<TouristAttraction>> call, Response<List<TouristAttraction>> response) {
                List<TouristAttraction> touristAttractionsFood = response.body();
                LatLng location_dest = null;
                LatLng destination = dest2;
                System.out.println("destination "+dest2);
                for(int i = 0;i<touristAttractionsFood.size();i++){
                    location_dest = new LatLng(touristAttractionsFood.get(i).getLatitude(), touristAttractionsFood.get(i).getLongitude());
                    if (SphericalUtil.computeDistanceBetween(location_dest, dest2)<5000){
                        hotelList.add(new LatLng(touristAttractionsFood.get(i).getLatitude(), touristAttractionsFood.get(i).getLongitude()));
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
