package com.example.plantapp.Onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.plantapp.R

class Onboarding1 : AppCompatActivity() {
    private lateinit var btnNextOnboarding1:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding1)
        btnNextOnboarding1=findViewById(R.id.btnNextOnboarding1)
        btnNextOnboarding1.setOnClickListener{
            startActivity(Intent(this,Onboarding2::class.java))
        }
    }
}