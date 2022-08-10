package com.example.apprute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.apprute.Adapter.TouristAttractionsAdapter;
import com.example.apprute.model.TouristAttraction;
import com.example.apprute.service.ApiInterface;
import com.example.apprute.service.BaseUrl;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTouristAttraction extends AppCompatActivity {
    ApiInterface apiInterface;
    TextView name, description, title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tourist_attraction);

        apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        name = findViewById(R.id.detail_name);
        description = findViewById(R.id.detail_description);
        title = findViewById(R.id.detail_title);
    getDetailTouristAttraction();
    }
    private void getDetailTouristAttraction(){
        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);
        Call<TouristAttraction> call = apiInterface.getDetailTouristAttraction(id);

        call.enqueue(new Callback<TouristAttraction>() {


            @Override
            public void onResponse(Call<TouristAttraction> call, Response<TouristAttraction> response) {
                System.out.println(response);
                if (!response.isSuccessful()){
                    name.setText("code" + response.code());
                    return;

                }
                TouristAttraction touristAttraction = response.body();
                name.setText(touristAttraction.getName());
                description.setText(touristAttraction.getDescription());
                title.setText(touristAttraction.getName());
            }

            @Override
            public void onFailure(Call<TouristAttraction>call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}