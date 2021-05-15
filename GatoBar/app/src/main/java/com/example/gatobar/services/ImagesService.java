package com.example.gatobar.services;

import com.example.gatobar.models.Images;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ImagesService {

    String API_ROUTE = "/v2/list?imit=100";

    @GET(API_ROUTE)
    Call<List<Images>> getImages();
}
