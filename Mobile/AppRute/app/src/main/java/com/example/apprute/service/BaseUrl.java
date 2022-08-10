package com.example.apprute.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseUrl {

//    Deploy
    public static  final String BASE_URL = "http://martin123412-001-site1.gtempurl.com/api/";
//    hospot pribadi
//    public static final String BASE_URL = "http://192.168.43.2:8084/api/";
//    tendik2021
//    public static final String BASE_URL = "http://172.28.43.192:8084/api/";
//      public static final String BASE_URL = "http://172.27.43.25:8084/api/";

    //    students
//    public static final String BASE_URL = "http://172.31.42.156:8084/api/";
//    public static final String BASE_URL = "http://192.168.53.247:8084/api/";
    public static Retrofit getClient() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        return retrofit;
    }
}
