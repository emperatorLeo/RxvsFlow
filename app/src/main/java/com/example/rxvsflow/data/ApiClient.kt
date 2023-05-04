package com.example.rxvsflow.data

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    private fun provideRetrofitClient(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl("https://animechan.vercel.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
    }

    fun provideRxClient(): RxServices {
        return provideRetrofitClient()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(RxServices::class.java)
    }

    fun provideFlowClient(): CoroutineServices{
        return provideRetrofitClient()
            .build()
            .create(CoroutineServices::class.java)
    }

}





