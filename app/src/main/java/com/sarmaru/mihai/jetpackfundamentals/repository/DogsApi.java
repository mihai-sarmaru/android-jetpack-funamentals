package com.sarmaru.mihai.jetpackfundamentals.repository;

import com.sarmaru.mihai.jetpackfundamentals.model.DogBreed;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface DogsApi {
    @GET("DevTides/DogsApi/master/dogs.json")
    Single<List<DogBreed>> getDogs();
}
