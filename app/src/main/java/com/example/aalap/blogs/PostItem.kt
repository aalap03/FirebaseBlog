package com.example.aalap.blogs

import com.google.gson.annotations.SerializedName

data class PostItem(
        val postId: String,
        val userId: String,
        val title: String,
        val desc: String,
        val price: String,
        val country: String,
        val province: String,
        val city: String,
        val email: String,
        val postImageUri: String
)
