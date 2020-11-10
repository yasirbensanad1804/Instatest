package com.example.instatest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instatest.R
import com.example.instatest.model.Post

class MyImagesAdapter(private val myContext: Context, private val mPost : List<Post>)
    : RecyclerView.Adapter<MyImagesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyImagesViewHolder {
        val view = LayoutInflater.from(myContext).inflate(R.layout.item_post_layout, parent,false)
        return MyImagesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mPost.size
    }

    override fun onBindViewHolder(holder: MyImagesViewHolder, position: Int) {
        val mPost = mPost[position]

        Glide.with(myContext).load(mPost.getPostImage()).into(holder.postImageGrid)

    }
}

class MyImagesViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
    var postImageGrid : ImageView = itemView.findViewById(R.id.post_image_grid)
}