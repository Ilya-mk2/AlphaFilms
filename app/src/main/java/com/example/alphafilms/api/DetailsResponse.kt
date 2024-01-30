package com.example.alphafilms.api

import com.google.gson.annotations.SerializedName

data class DetailsResponse (
    @SerializedName("Title") val title : String,
    @SerializedName("Year") val year : String,
    @SerializedName("Genre") val  genre : String,
    @SerializedName("Awards") val awards : String,
    @SerializedName("Poster") val poster : String
)