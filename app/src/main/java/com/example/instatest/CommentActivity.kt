package com.example.instatest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instatest.adapter.CommentAdapter
import com.example.instatest.model.Comment
import com.example.instatest.model.UserSearch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_comment.*

class CommentActivity : AppCompatActivity() {

    private var postId = ""
    private var publisherId = ""
    private var firebaseUser : FirebaseUser? = null
    private var commentAdapter: CommentAdapter? = null
    private var commentListData: MutableList<Comment>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        val intent = intent
        postId = intent.getStringExtra("postId")!!
        publisherId = intent.getStringExtra("publisherId")!!

        firebaseUser = FirebaseAuth.getInstance().currentUser

        var recyclerView: RecyclerView? = null
        recyclerView =  findViewById(R.id.recycler_comment)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        recyclerView.layoutManager = linearLayoutManager

        commentListData = ArrayList()
        commentAdapter = CommentAdapter(this, commentListData as ArrayList<Comment>)
        recyclerView.adapter = commentAdapter

        userInfo()

        textViewPostComment.setOnClickListener {
            if (editTextComment.text.toString() == ""){
                Toast.makeText(this, "Tolong diisi", Toast.LENGTH_SHORT).show()
            }else{
                addComment()
            }
        }

        readComment()
        getPostImageComment()
    }

    private fun getPostImageComment() {
        val postCommentRef = FirebaseDatabase.getInstance().reference
            .child("Posts").child(postId).child("postimage")

        postCommentRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val images = snapshot.value.toString()

                    Picasso.get().load(images).into(image_post_comment)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun readComment() {
        val commentsRef = FirebaseDatabase.getInstance().reference
            .child("Comments").child(postId)

        commentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    commentListData!!.clear()
                    for (s in snapshot.children){
                        val comment = snapshot.getValue(Comment::class.java)
                        commentListData!!.add(comment!!)
                    }
                    commentAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addComment() {
        val commentsRef = FirebaseDatabase.getInstance().reference
            .child("Comments").child(postId)
        val commentMap = HashMap<String, Any>()

        commentMap["comments"] = editTextComment!!.text.toString()
        commentMap["publisher"] = firebaseUser!!.uid

        commentsRef.push().setValue(commentMap)
        editTextComment!!.text.clear()
    }

    private fun userInfo() {
        val usersRef = FirebaseDatabase.getInstance().reference
            .child("users").child(firebaseUser!!.uid)

        usersRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user = snapshot.getValue<UserSearch>(UserSearch::class.java)

                    Picasso.get().load(user!!.getImage()).into(circleImageView)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}