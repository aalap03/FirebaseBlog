package com.example.aalap.blogs.RetrofitUtils

import com.example.aalap.blogs.Models.MainHitsObject
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitElasticSearch {

    @GET("_search?q=*")
    fun getMainHits():Observable<Response<MainHitsObject>>
}