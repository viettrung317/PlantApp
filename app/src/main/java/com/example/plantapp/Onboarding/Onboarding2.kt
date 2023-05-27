package com.example.plantapp.Onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.plantapp.R

class Onboarding2 : AppCompatActivity() {
    private lateinit var btnNextOnboarding2:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding2)
        btnNextOnboarding2=findViewById<Button>(R.id.btnNextOnboarding2)
        btnNextOnboarding2.setOnClickListener{
            startActivity(Intent(this,Onboarding3::class.java))
        }

    }
}