package com.example.apprute;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.apprute.Adapter.HarborAdapter;
import com.example.apprute.model.Harbor;
import com.example.apprute.service.ApiInterface;
import com.example.apprute.service.BaseUrl;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HarborActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harbor);
        recyclerView = findViewById(R.id.recycerlview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        getHarbor();

    }
    private void getHarbor(){
        Call<List<Harbor>> listCall = apiInterface.getHarbor();
        listCall.enqueue(new Callback<List<Harbor>>() {
            @Override
            public void onResponse(Call<List<Harbor>> call, Response<List<Harbor>> response) {
                System.out.println("Success");
//                if (!response.isSuccessful()){
//                    Toast.makeText(MainActivity.this, response.code(), Toast.LENGTH_SHORT).show();
//                    return;
//                }
                List<Harbor> harborList = response.body();
                HarborAdapter harborAdapter = new HarborAdapter(HarborActivity.this, harborList);
                recyclerView.setAdapter(harborAdapter);
            }

            @Override
            public void onFailure(Call<List<Harbor>> call, Throwable t) {
                Log.e("Error", t.getMessage());
                Toast.makeText(HarborActivity.this, t.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }
}