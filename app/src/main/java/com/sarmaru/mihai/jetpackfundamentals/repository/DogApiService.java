package com.sarmaru.mihai.jetpackfundamentals.repository;

import com.sarmaru.mihai.jetpackfundamentals.model.DogBreed;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DogApiService {
    public static final String BASE_URL = "https://raw.githubusercontent.com";

    private DogsApi dogsApi;

    public DogApiService() {
        dogsApi = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(DogsApi.class);
    }

    public Single<List<DogBreed>> getDogsList() {
        return dogsApi.getDogs();
    }
}
