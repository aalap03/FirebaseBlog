package com.example.aalap.blogs.Models

import com.google.gson.annotations.SerializedName

data class ResponsePostItem(@SerializedName("city") val city: String,
                            @SerializedName("country") val country: String,
                            @SerializedName("desc") val desc: String,
                            @SerializedName("email") val email: String,
                            @SerializedName("postId") val postId: String,
                            @SerializedName("postImageUri") val postImageUri: String,
                            @SerializedName("price") val price: String,
                            @SerializedName("province") val province: String,
                            @SerializedName("title") val title: String,
                            @SerializedName("userId") val userId: String)