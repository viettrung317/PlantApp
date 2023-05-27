package com.example.plantapp.Onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.plantapp.R
import com.example.plantapp.account.LoginActivity

class Onboarding3 : AppCompatActivity() {
    private lateinit var btnNextOnboarding3:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding3)
        btnNextOnboarding3=findViewById<Button>(R.id.btnNextOnboarding3)
        btnNextOnboarding3.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}