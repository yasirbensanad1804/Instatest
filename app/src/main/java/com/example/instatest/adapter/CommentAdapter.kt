package com.example.instatest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.instatest.R
import com.example.instatest.model.Comment
import com.example.instatest.model.UserSearch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class CommentAdapter(private val context: Context, private val mComment: MutableList<Comment>): RecyclerView.Adapter<CommentViewHolder>() {
    private var firebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.list_layout_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mComment.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        val comment = mComment[position]

        holder.comment.text = comment.getComment()
        getUserInfo(holder.imageProfileComment, holder.usernameComment, comment.getPublisher())
    }

    private fun getUserInfo(imageProfileComment: CircleImageView, usernameComment: TextView, publisher: String) {
        val usersRef = FirebaseDatabase.getInstance().reference
            .child("users").child(publisher)

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user = snapshot.getValue(UserSearch::class.java)

                    Picasso.get()
                        .load(user!!.getImage())
                        .into(imageProfileComment)
                    usernameComment.text = user!!.getUsername()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}

class CommentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var imageProfileComment: CircleImageView = itemView.findViewById(R.id.profile_image_comment)
    var usernameComment: TextView = itemView.findViewById(R.id.username_comment)
    var comment: TextView = itemView.findViewById(R.id.textViewComment)
}