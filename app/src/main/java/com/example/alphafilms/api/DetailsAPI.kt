package com.example.alphafilms.api

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DetailsAPI {
    @GET("/")
    fun search(
        @Query("apikey") apiKey : String,
        @Query("i") id : String
    ) : Single<DetailsResponse>

}