package com.example.plantapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.plantapp.Onboarding.Onboarding1
import com.example.plantapp.account.LoginActivity
import com.example.plantapp.databinding.ActivityMainBinding
import com.example.plantapp.fragment.HomePageFragment
import com.example.plantapp.fragment.ProfileFragment
import com.example.plantapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding :ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var backPressedTime: Long = 0
    private var mToast: Toast? = null
    private var isFirstRun = true
    private val data: FirebaseDatabase =Firebase.database
    private val ref: DatabaseReference =data.getReference("User")
    private lateinit var user:User;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //lưu dữ liệu kiểm tra xem đây có phải lần đầu chạy app không
        val sharedPrefs = getSharedPreferences("com.example.plantapp", Context.MODE_PRIVATE)
        isFirstRun = sharedPrefs.getBoolean("is_first_run", true)

        if (isFirstRun) {
            startActivity(Intent(this,Onboarding1::class.java))
            finish()
            val editor = sharedPrefs.edit()
            editor.putBoolean("is_first_run", false)
            editor.apply()
        }
        replaceFragment(HomePageFragment())
        auth = Firebase.auth

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_home -> replaceFragment(HomePageFragment())
                R.id.menu_profile -> replaceFragment(ProfileFragment())
                else ->{

                }
            }
            true
        }
    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentlayout,fragment)
        fragmentTransaction.commit()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser == null){
            val intent = Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            mToast?.cancel() //khi thoát ứng dụng sẽ ko hiện thông báo toast
            super.onBackPressed()
            return
        } else {
            mToast = Toast.makeText(this, "Nhấn lần nữa để thoát!", Toast.LENGTH_SHORT)
            mToast?.show()        }
        backPressedTime = System.currentTimeMillis()
    }
}