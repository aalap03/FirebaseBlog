package com.example.aalap.blogs.Models

import com.google.gson.annotations.SerializedName

data class Hits(@SerializedName("hits") val post: MutableList<Hit>)
