package com.example.instatest

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sign_up.setOnClickListener{startActivity(Intent(this,RegisterActivity::class.java))}

        btn_login.setOnClickListener{loginUser()}
    }
    private fun loginUser() {
        val email =  email_login.text.toString()
        val password = password_login.text.toString()

        when{
            TextUtils.isEmpty(email) -> Toast.makeText(this,"Email harus diisi", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(password) -> Toast.makeText(this,"Password harus diisi", Toast.LENGTH_SHORT).show()

            else -> {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("login")
                progressDialog.setMessage("Please Wait...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        progressDialog.dismiss()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        val message =  task.exception.toString()
                        Toast.makeText(this,"Gagal:$message",Toast.LENGTH_SHORT).show()
                        FirebaseAuth.getInstance().signOut()
                        progressDialog.dismiss()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (FirebaseAuth.getInstance().currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

}