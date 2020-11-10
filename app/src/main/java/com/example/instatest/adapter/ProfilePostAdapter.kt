package com.example.instatest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.instatest.R
import com.example.instatest.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso

class ProfilePostAdapter (private val mContext : Context, private val ImageProfil : List<Post>) : RecyclerView.Adapter<ProfPostViewHolder>() {
    private var firebaseUser : FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfPostViewHolder {
        val ImageFeed = LayoutInflater.from(mContext).inflate(R.layout.item_post_layout, parent, false)
        return ProfPostViewHolder(ImageFeed)
    }

    override fun getItemCount(): Int {
        return ImageProfil.size
    }

    override fun onBindViewHolder(holder: ProfPostViewHolder, position: Int) {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        val ViewHoldPost = ImageProfil[position]

        Picasso.get().load(ViewHoldPost.getPostImage()).into(holder.imageProfPost)
    }
}


class ProfPostViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview) {
    var imageProfPost : ImageView = itemview.findViewById(R.id.post_image_grid)

}