package com.example.aalap.blogs.Models

import com.google.gson.annotations.SerializedName

data class Hit(@SerializedName("_source") val postItem: ResponsePostItem)
