package com.example.aalap.blogs.Utilities

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.aalap.blogs.Models.Hits
import com.example.aalap.blogs.Models.ResponsePostItem
import com.example.aalap.blogs.R

class GridAdapter(val context: Context, private val items:MutableList<ResponsePostItem>) : RecyclerView.Adapter<GridAdapter.GridHolder>() {

    private val requestManager = Glide.with(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridHolder {
        return GridHolder(LayoutInflater.from(context).inflate(R.layout.grid_image_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: GridHolder, position: Int) {
        requestManager
                .applyDefaultRequestOptions(RequestOptions()
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher))
                .load(items[position].postImageUri)
                .into(holder.imageView)
    }

    class GridHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.findViewById<ImageView>(R.id.item_post_image)!!
    }
}