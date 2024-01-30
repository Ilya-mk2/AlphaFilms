package com.example.alphafilms.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query



interface MovieAPI  {
    @GET("/")
    fun search(
        @Query("apikey") apiKey : String,
        @Query("s") search : String,
        @Query("page") page : Int ) : Single<SearchResponse>

}