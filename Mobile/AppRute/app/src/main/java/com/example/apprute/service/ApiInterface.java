package com.example.apprute.service;

import com.example.apprute.model.Harbor;
import com.example.apprute.model.HarborVM;
import com.example.apprute.model.PortRoute;
import com.example.apprute.model.Route;
import com.example.apprute.model.Schedule;
import com.example.apprute.model.TouristAttraction;
import com.google.android.material.internal.ToolbarUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    //HARBORS
    @GET("harbors")
    Call<List<Harbor>> getHarbor();

    @GET("touristattractions/GetTouristAttractions/{id}")
    Call<TouristAttraction> getDetailTouristAttraction(@Path("id") int id);

    @GET("TouristAttractions/GetTouristAttraction")
    Call<List<TouristAttraction>> getTouristAttraction();

    @GET("TouristAttractions/GetFoodDestination")
    Call<List<TouristAttraction>> getFoodDestination();

    @GET("TouristAttractions/GetSouvenirDestination")
    Call<List<TouristAttraction>> getSouvenirDestination();

    @GET("TouristAttractions/GetHotelDestination")
    Call<List<TouristAttraction>> getHotelDestination();

    @GET("touristattractions/GetCategoryTourism/{id}")
    Call<List<TouristAttraction>> getTouristAttractionCategory(@Path("id") int id);

    //PORT ROUTES
    @GET("PortRoutes/")
    Call<List<PortRoute>> getRoute();

    @GET("PortRoutes/{id}")
    Call<PortRoute> getRoute(@Path("id") int id);

    @GET("PortRoutes/GetHarborPortRoute/{id}")
    Call<List<Harbor>> getHarborPortRoute(@Path("id") int id);


    @GET("PortRoutes/GetHarborRoute/{id}")
    Call<List<HarborVM>> getHarborRoute(@Path("id") int id);

    //  SCHEDULE
    @GET("Schedules/GetSchedule/{id}")
    Call<List<Schedule>> getSchedule(@Path("id") int id);

}
