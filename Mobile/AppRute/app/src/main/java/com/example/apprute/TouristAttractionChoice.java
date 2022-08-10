package com.example.apprute;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.apprute.Adapter.TouristAttractionAdapterFragment;
import com.example.apprute.Fragment.OptionFragment;
import com.example.apprute.Fragment.TouristFragment;
import com.example.apprute.Maps.FetchURL;
import com.example.apprute.Maps.TaskLoadedCallback;
import com.example.apprute.model.HarborVM;
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

import io.grpc.internal.JsonUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TouristAttractionChoice extends AppCompatActivity implements TaskLoadedCallback, OnMapReadyCallback {
    private RecyclerView recyclerView;
    ApiInterface apiInterface;
    Button btn_getLocation;
    TextView tv_latlng;
    EditText et_location;
    String location;
    ArrayList<Double> latlng;
    String time="",getlocation="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_attraction_choice);
        recyclerView = findViewById(R.id.recyclerview_tourist_attraction_choice);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        et_location = findViewById(R.id.edittext_time);
        btn_getLocation = findViewById(R.id.btn_inputlocation);
        latlng = new ArrayList<>();
        Intent intent = getIntent();
        time = intent.getStringExtra("time");
        location = intent.getStringExtra("location");
        getTouristAttraction();
        btn_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationFromAddress();
                Intent intent = getIntent();
                intent.putExtra("lat_input",latlng.get(0));
                intent.putExtra("lng_input",latlng.get(1));
                intent.putExtra("time",time);
                intent.putExtra("location",getlocation);
                startActivity(intent);
            }
        });
    }
    private void getTouristAttraction(){
        Call<List<TouristAttraction>> listCall = apiInterface.getTouristAttraction();
        listCall.enqueue(new Callback<List<TouristAttraction>>() {
            @Override
            public void onResponse(Call<List<TouristAttraction>> call, Response<List<TouristAttraction>> response) {
                List<TouristAttraction> touristAttractions = response.body();
                TouristAttractionAdapterFragment touristAttractionAdapterFragment = new TouristAttractionAdapterFragment(TouristAttractionChoice.this,time,location,touristAttractions);
                recyclerView.setAdapter(touristAttractionAdapterFragment);
            }
            @Override
            public void onFailure(Call<List<TouristAttraction>> call, Throwable t) {
            }
        });
    }
    public GeoPoint getLocationFromAddress() {
        location =et_location.getText().toString();
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(location, 20);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            latlng.add(location.getLatitude());
            latlng.add(location.getLongitude());
            return p1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onTaskDone(Object... values) {
        System.out.println("TAI");
    }

    @Override
    public void onDistanceDuration(Object... values) {

    }

    @Override
    public void onDistance(Object... values) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}