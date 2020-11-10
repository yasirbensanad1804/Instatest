package com.example.instatest

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        txt_sing.setOnClickListener{startActivity(Intent(
            this,LoginActivity::class.java))}
        backbutton.setOnClickListener{startActivity(Intent(
            this,LoginActivity::class.java))}

        btn_register.setOnClickListener{
            createaccount()
        }
    }
    private fun createaccount(){
        val fullname =  edt_fullnameregist.text.toString()
        val username =  edt_usernameRegist.text.toString()
        val email =  edt_emailregist.text.toString()
        val password =  edt_passregist.text.toString()
        when{
            TextUtils.isEmpty(fullname) -> Toast.makeText(this,"fullname is required", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(username) -> Toast.makeText(this,"username is required", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(email) -> Toast.makeText(this,"email is required", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(password) -> Toast.makeText(this,"password is required", Toast.LENGTH_SHORT).show()

            else -> {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Register")
                progressDialog.setMessage("Tunggu bentaran....")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mAuth : FirebaseAuth = FirebaseAuth.getInstance()

                mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener{task ->
                        if (task.isSuccessful)
                            saveUserInfo(fullname, username, email, progressDialog)
                    }
            }

        }



    }

    private fun saveUserInfo(fullname: String, username: String, email: String, progressDialog: ProgressDialog) {

        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef : DatabaseReference =  FirebaseDatabase.getInstance().reference.child("users")

        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserID
        userMap["fullname"] = fullname
        userMap["username"] = username
        userMap["email"] = email
        userMap["bio"] = "HEY"
        userMap["image"] = "https://firebasestorage.googleapis.com/v0/b/instatest-a8e08.appspot.com/o/1st%20folder%2Fblohsh%20n.png?alt=media&token=2c3dab40-3d00-4b90-affb-ab961e7d3c1a"

        usersRef.child(currentUserID).setValue(userMap)
            .addOnCompleteListener {task ->
                if (task.isSuccessful){
                    progressDialog.dismiss()
                    Toast.makeText(this,"Akun Sudah Dibuat",Toast.LENGTH_SHORT).show()

                    val pergi = Intent(this,MainActivity::class.java)
                    pergi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(pergi)
                    finish()
                }else{
                    val message = task.exception!!.toString()
                    Toast.makeText(this,"ERROR BANG $message",Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                    progressDialog.dismiss()

                }
            }
    }
}