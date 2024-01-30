package com.example.alphafilms.api

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("Search") val list: List<Movie>?,
    @SerializedName("totalResults") val results: String?,
    @SerializedName("Response") val response: String?
)
