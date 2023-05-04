package com.example.rxvsflow.data

import retrofit2.http.GET

interface CoroutineServices {

    @GET("available/character?")
    suspend fun getAnimeAvailable(): List<String>
}