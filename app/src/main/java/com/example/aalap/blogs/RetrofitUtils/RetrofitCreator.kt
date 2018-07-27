package com.example.aalap.blogs.RetrofitUtils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitCreator {

    fun retrofit(): Retrofit {

        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor({ chain ->
            val request = chain.request()

            val requestHeader = request.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Basic dXNlcjpSSExjODJmVGZSRms=")
                    .build()

            chain.proceed(requestHeader)
        })

        return Retrofit.Builder()
                .baseUrl("http://35.232.82.151//elasticsearch/posts/post/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

    }
}