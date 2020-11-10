package com.example.instatest

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.instatest.model.UserSearch
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var firebaseUser: FirebaseUser
    private var checkInfoProfile = ""
    private var myUrl = ""
    private var imageUri: Uri? = null
    private var storageProfilePicture: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storageProfilePicture = FirebaseStorage.getInstance().reference.child("Profile Pictures")

        buttonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this@EditProfileActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        txt_gantiimage.setOnClickListener {
            checkInfoProfile = "Clicked"

            CropImage.activity()
                .setAspectRatio(1,1)
                .start(this@EditProfileActivity)
        }

        save_user_info.setOnClickListener {
            if (checkInfoProfile == "Clicked"){
                uploadImageProfileAndUpdateInfoProfile()
            }else{
                updateUserInfoOnly()
            }
        }
        userInfo()
    }

    private fun uploadImageProfileAndUpdateInfoProfile() {
        when{
            imageUri == null -> Toast.makeText(this, "Select Image....", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(etnama.text.toString()) ->{
                Toast.makeText(this, "Jangan Kosong", Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(etusername_ep.text.toString()) ->{
                Toast.makeText(this, "Jangan Kosong", Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(etbio.text.toString()) ->{
                Toast.makeText(this, "Jangan Kosong", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val progressDialog = ProgressDialog(this@EditProfileActivity)
                progressDialog.setTitle("Update Profile")
                progressDialog.setMessage("Tunngu bentaran, Lagi Update dulu")
                progressDialog.show()

                val fileRef = storageProfilePicture!!.child(firebaseUser.uid + ".jpg")

                val uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri!!)
                uploadTask.continueWithTask(Continuation  <UploadTask.TaskSnapshot, Task<Uri>> {task ->
                    if (!task.isSuccessful){
                        task.exception.let {
                            throw it!!

                        }
                    }
                    return@Continuation fileRef.downloadUrl
                }).addOnCompleteListener (OnCompleteListener<Uri> { task ->
                    if (task.isSuccessful){
                        val downloadUrl = task.result
                        myUrl = downloadUrl.toString()

                        val userRef = FirebaseDatabase.getInstance().reference.child("users")
                        val userMap = HashMap<String, Any>()

                        userMap["fullname"] = etnama.text.toString()
                        userMap["username"] = etusername_ep.text.toString()
                        userMap["bio"]      = etbio.text.toString()
                        userMap["image"]    = myUrl

                        userRef.child(firebaseUser.uid).updateChildren(userMap)
                        Toast.makeText(this, "infonya udah di update tuh ngab", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        progressDialog.dismiss()
                    }else{
                        progressDialog.dismiss()
                    }
                })

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null){
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            img_profile_ep.setImageURI(imageUri)
        }
    }


    private fun updateUserInfoOnly() {
        when{
            TextUtils.isEmpty(etnama.text.toString()) ->{
                Toast.makeText(this, "Jangan Kosong", Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(etusername_ep.text.toString()) ->{
                Toast.makeText(this, "Jangan Kosong", Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(etbio.text.toString()) ->{
                Toast.makeText(this, "Jangan Kosong", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val usersRef = FirebaseDatabase.getInstance().reference
                    .child("users")

                val userMap = HashMap<String, Any>()
                userMap["fullname"] = etnama.text.toString()
                userMap["username"] = etusername_ep.text.toString()
                userMap["bio"]      = etbio.text.toString()

                usersRef.child(firebaseUser.uid).updateChildren(userMap)

                Toast.makeText(this, "Infonya udah di update tuh ngab", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            }

        }
    }



    private fun userInfo() {
        val usersRef = FirebaseDatabase.getInstance().reference
            .child("users").child(firebaseUser.uid)

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user = snapshot.getValue<UserSearch>(UserSearch::class.java)

                    etnama.setText(user?.getFullname())
                    etusername_ep.setText(user?.getUsername())
                    etbio.setText(user?.getBio())
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}