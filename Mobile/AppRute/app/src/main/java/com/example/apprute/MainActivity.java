package com.example.apprute;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.apprute.Adapter.HarborAdapter;
import com.example.apprute.Adapter.HomeAdapter;
import com.example.apprute.Adapter.PortRouteAdapter;
import com.example.apprute.model.Harbor;
import com.example.apprute.model.PortRoute;
import com.example.apprute.model.Route;
import com.example.apprute.service.ApiInterface;
import com.example.apprute.service.BaseUrl;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.itemhome);
        apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        getAllPortRoute();
        recyclerView = findViewById(R.id.recycerlview_home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiInterface = BaseUrl.getClient().create(ApiInterface.class);
////        recyclerView = findViewById(R.id.recycerlview);
////        recyclerView.setHasFixedSize(true);
////        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.43.2:8082/api/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

//        apiInterface = retrofit.create(ApiInterface.class);
//        getHarbor();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.rute:
                        startActivity(new Intent(getApplicationContext(), RuteActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.itemhome:
                        return true;
                    case R.id.tour:
                        startActivity(new Intent(getApplicationContext(), InformationTravelActivity.class));
                        return true;
                }
                return false;
            }
        });
    }
//    private void getHarbor(){
//        Call<List<Harbor>> listCall = apiInterface.getHarbor();
//        listCall.enqueue(new Callback<List<Harbor>>() {
//            @Override
//            public void onResponse(Call<List<Harbor>> call, Response<List<Harbor>> response) {
//                System.out.println("Success");
//                if (!response.isSuccessful()){
//                    Toast.makeText(MainActivity.this, response.code(), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                List<Harbor> harborList = response.body();
//                HarborAdapter harborAdapter = new HarborAdapter(MainActivity.this, harborList);
//                recyclerView.setAdapter(harborAdapter);
//            }
//
//            @Override
//            public void onFailure(Call<List<Harbor>> call, Throwable t) {
//                System.out.println("ANING : "+t.getMessage());
//                Toast.makeText(MainActivity.this, t.getMessage() , Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
    private void getAllPortRoute(){
    Call<List<PortRoute>> listCall = apiInterface.getRoute();
    listCall.enqueue(new Callback<List<PortRoute>>() {
        @Override
        public void onResponse(Call<List<PortRoute>> call, Response<List<PortRoute>> response) {
            List<PortRoute> portRoutes = response.body();
            HomeAdapter portRouteAdapter = new HomeAdapter(MainActivity.this,portRoutes);
            recyclerView.setAdapter(portRouteAdapter);
        }

        @Override
        public void onFailure(Call<List<PortRoute>> call, Throwable t) {

        }
    });
}


}