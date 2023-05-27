package com.example.plantapp.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.example.plantapp.R
import com.example.plantapp.databinding.ActivitySignUpBinding
import com.example.plantapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database

import com.google.firebase.ktx.Firebase


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignUpBinding
    private val valid=CheckInput()
    private var user=User()
    private var email:String=""
    private var pass:String=""
    private var username:String=""
    private var mAuth:FirebaseAuth= Firebase.auth
    private val database: DatabaseReference = Firebase.database.reference
    private lateinit var userfb:FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.txtEmailSignUp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                email= binding.txtEmailSignUp.text.toString()
                if(!valid.validateEmail(email)){
                    binding.txtEmailSignUp.error = "Invalid email!"
                }
                else{
                    binding.txtPassSignUp.setText("")
                    binding.txtEmailSignUp.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }

        })
        binding.txtPassSignUp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                pass= binding.txtPassSignUp.text.toString()
                if(!valid.validatePass(pass)){
                    binding.txtPassSignUp.error = "Invalid password!"
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.txtFullNameSignUp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                username= binding.txtFullNameSignUp.text.toString()
                if(username.length >15){
                    binding.txtFullNameSignUp.error = "Invalid password!"
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.btnSignUp.setOnClickListener {
            email= binding.txtEmailSignUp.text.toString()
            pass= binding.txtPassSignUp.text.toString()
            username= binding.txtFullNameSignUp.text.toString()
            if(TextUtils.isEmpty(email)){
                binding.txtEmailSignUp.error="Email can't empty"
                binding.txtEmailSignUp.requestFocus()
            }else if(TextUtils.isEmpty(pass)){
                binding.txtPassSignUp.error="Password can't empty"
                binding.txtPassSignUp.requestFocus()
            }
            else if(TextUtils.isEmpty(username)){
                binding.txtFullNameSignUp.error="UserName can't empty"
                binding.txtFullNameSignUp.requestFocus()
            }
            else{
                binding.progressBar2.visibility=View.VISIBLE
                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this){
                    if(it.isSuccessful){
                        userfb= mAuth.currentUser!!
                        val userId:String=userfb.uid
                        user.userName=username
                        user.email=email
                        user.imagesource=""
                        database.child("User").child(userId).setValue(user).addOnCompleteListener(this){
                            if(it.isSuccessful){
                                startActivity(Intent(this@SignUpActivity,LoginActivity::class.java))
                                finish()
                            }else{
                                Toast.makeText(this@SignUpActivity, "error!!!" , Toast.LENGTH_SHORT).show()
                            }
                        }

                    }else{
                        Toast.makeText(this@SignUpActivity, "Đăng ký không thành công!" , Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}