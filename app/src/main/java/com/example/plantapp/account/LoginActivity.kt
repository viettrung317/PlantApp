package com.example.plantapp.account

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.plantapp.MainActivity
import com.example.plantapp.R
import com.example.plantapp.databinding.ActivityForgotBinding.inflate
import com.example.plantapp.databinding.ActivityLoginBinding
import com.example.plantapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private var mAuth: FirebaseAuth = Firebase.auth
    var temp:Int=0
    private lateinit var sharedPreferences: SharedPreferences // chuyển sharedPreferences thành biến toàn cục
    override fun onStart() {
        super.onStart()
        val emaillogin = sharedPreferences.getString("email", "")
        val password = sharedPreferences.getString("password", "")
        // Kiểm tra giá trị:
        if (emaillogin!!.isNotEmpty() && password!!.isNotEmpty()) {
            binding.txtEmailLogin.setText(emaillogin)
            binding.txtPassLogin.setText(password)
            binding.checkBoxRemember.isChecked=true
        }
    }

    private fun removeAcc() {
        val editor = sharedPreferences.edit()
        editor.remove("email")
        editor.remove("password")
        editor.apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.txtEmailLogin.addTextChangedListener{
            binding.txtPassLogin.setText("")
            binding.txtEmailLogin.requestFocus()
            binding.checkBoxRemember.isChecked=false
        }
        binding.txtforgotPass.setOnClickListener{
            startActivity(Intent(this,ForgotActivity::class.java))
        }
        binding.viewHaveAcc.setOnClickListener{
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        // khởi tạo sharedPrefences trong hàm onCreate()
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        binding.btnLogin.setOnClickListener {
            login()
        }
    }
    // xử lý đăng nhập
    private fun login() {
        temp++
        if(temp==1){
            var email: String? = null
            var pass: String? = null
            email = binding.txtEmailLogin.text.toString()
            pass = binding.txtPassLogin.text.toString()


            if (TextUtils.isEmpty(email)){
                binding.txtEmailLogin.error = "Email can't empty"
                binding.txtEmailLogin.requestFocus()
            } else if (TextUtils.isEmpty(pass)) {
                binding.txtPassLogin.error = "Password can't empty"
                binding.txtPassLogin.requestFocus()
            } else {
                binding.progressBar1.visibility = View.VISIBLE
                val emaillogin = sharedPreferences.getString("email", "")
                val password = sharedPreferences.getString("password", "")
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        if (binding.checkBoxRemember.isChecked){
                            saveAcc(email,pass)
                        }else{
                            if(emaillogin!!.isNotEmpty() && password!!.isNotEmpty()){
                                removeAcc()
                            }
                        }
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        binding.progressBar1.visibility = View.GONE
                        // Khi đăng nhập thất bại
                        try {
                            throw task.exception!!
                        } catch (e: FirebaseAuthInvalidUserException) {
                            // Người dùng không tồn tại hoặc đã xóa tài khoản
                            Toast.makeText(this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show()
                            binding.txtPassLogin.setText("")
                            binding.txtEmailLogin.setText("")
                            temp=0
                        } catch (e: FirebaseAuthInvalidCredentialsException) {
                            // Sai tên đăng nhập hoặc mật khẩu
                            Toast.makeText(this, "Sai mật khẩu!", Toast.LENGTH_SHORT).show()
                            binding.txtPassLogin.setText("")
                            temp=0
                        } catch (e: Exception) {
                            // Lỗi khác
                        }
                    }
                }
            }
        }
    }

    private fun saveAcc(email: String, pass: String) {
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", pass)
        editor.apply()
    }
}
