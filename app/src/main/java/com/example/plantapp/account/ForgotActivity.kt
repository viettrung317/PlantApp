package com.example.plantapp.account

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.example.plantapp.R
import com.example.plantapp.databinding.ActivityForgotBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotBinding
    private lateinit var email:String
    private val auth:FirebaseAuth=Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityForgotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSendEmail.setOnClickListener {
            email=binding.txtEmailReset.text.toString()
            if(TextUtils.isEmpty(email)){
                binding.txtEmailReset.error="Email can't empty!"
            }
            else{
                binding.progressBar3.visibility=View.VISIBLE
                auth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val signInMethods = task.result?.signInMethods
                            if (!signInMethods.isNullOrEmpty()) {
                                // Email đã được đăng ký với một phương thức đăng nhập nào đó
                                Firebase.auth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Log.d(TAG, "Email sent.")
                                            startActivity(Intent(this,LoginActivity::class.java))
                                            finish()
                                        }
                                    }
                            } else {
                                // Email chưa được đăng ký với bất kỳ phương thức đăng nhập nào
                                binding.progressBar3.visibility=View.GONE
                                binding.txtEmailReset.error="Email chưa được đăng ký!"
                                binding.txtEmailReset.requestFocus()
                            }
                        } else {
                            // Xảy ra lỗi khi lấy danh sách phương thức đăng nhập cho email
                            Log.d("Error","Xảy ra lỗi khi lấy danh sách phương thức đăng nhập cho email!")
                        }
                    }
            }
        }
    }
}