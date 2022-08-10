package com.example.apprute;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RuteActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    Button BtnsearchLocation, Btn_getlocation, Btn_gettime;
    LocationManager locationManager;
    TextView textView;
    EditText editText_location, editText_time;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    FusedLocationProviderClient providerClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rute);
        editText_time = findViewById(R.id.edittext_time);
        editText_location = findViewById(R.id.edittext_location);
        BtnsearchLocation = findViewById(R.id.btnPilihWisata);
        Btn_gettime = findViewById(R.id.btn_gettime);
        Btn_getlocation = findViewById(R.id.btn_getlocation);
        Btn_getlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    editText_location.setText(getAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Kami berjanji "+getLatLng().getLongitude());
            }
        });
        BtnsearchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editText_location.getText().toString().length()==0){
                    editText_location.setError("lokasi tidak bisa kosong");

                }
                else if( editText_time.getText().toString().length()==0){
                    editText_time.setError("waktu tidak bisa kosong");
                }
                else if (getLocationFromAddress()==false){
                    editText_location.setError("Lokasi tidak ditemukan, coba lagi !!!");
                }
                else {
                    String time =  editText_time.getText().toString();
                    String location = editText_location.getText().toString();
                    System.out.println("time : "+time);
                    System.out.println("Location "+location);
                    Intent intent = new Intent(RuteActivity.this, TouristAttractionChoice.class);
                    intent.putExtra("time", time);
                    intent.putExtra("location", location);
                    startActivity(intent);
                }
            }
        });
        Btn_gettime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR);
                minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(RuteActivity.this, RuteActivity.this, hour, minute, DateFormat.is24HourFormat(RuteActivity.this));
                timePickerDialog.show();
            }
        });

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.rute);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.rute:
                        return true;
                    case R.id.itemhome:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.tour:
                        startActivity(new Intent(getApplicationContext(), InformationTravelActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
        myYear = year;
        myday = day;
        myMonth = month;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(RuteActivity.this, RuteActivity.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        myHour = hourOfDay;
        hourOfDay = minute;
        Time tme = new Time(myHour,hourOfDay,0);
        Format formatter;
        formatter = new SimpleDateFormat("HH:mm");
        editText_time.setText(formatter.format(tme));
    }

    @SuppressLint("MissingPermission")
    public Location getLatLng(){

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocationGPS != null) {
                return lastKnownLocationGPS;
            } else {
                Location loc =  locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                return loc;
            }
        } else {
            return null;
        }
    }

    public String getAddress() throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(getLatLng().getLatitude(),getLatLng().getLongitude(), 1);

        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        String result = city+","+state;
        return result;
    }
    public boolean getLocationFromAddress()  {
        Geocoder coder = new Geocoder(this);
        List<Address> address = null;
        try {
            address = coder.getFromLocationName(editText_location.getText().toString(), 20);

        } catch (IOException e) {
            e.printStackTrace();

        }
        if (address.size()==0) {
            return false;}
        else{
            return true;
        }


    }

}