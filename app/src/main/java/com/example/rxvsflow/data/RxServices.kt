package com.example.rxvsflow.data

import io.reactivex.Observable
import retrofit2.http.GET

interface RxServices {

    @GET("available/character?")
    fun getAnimeAvailable(): Observable<List<String>>

}