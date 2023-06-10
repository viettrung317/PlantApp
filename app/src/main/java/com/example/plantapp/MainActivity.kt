package com.example.plantapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.plantapp.Onboarding.Onboarding1
import com.example.plantapp.account.LoginActivity
import com.example.plantapp.databinding.ActivityMainBinding
import com.example.plantapp.fragment.AddPlantFragment
import com.example.plantapp.fragment.HomePageFragment
import com.example.plantapp.fragment.ProfileFragment
import com.example.plantapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding :ActivityMainBinding
    private var auth: FirebaseAuth=Firebase.auth
    private var backPressedTime: Long = 0
    private var mToast: Toast? = null
    private var isFirstRun = true
    private val data: FirebaseDatabase =Firebase.database
    private val ref: DatabaseReference =data.getReference("User")
    private val userfb: FirebaseUser =auth.currentUser!!
    private val uid:String=userfb.uid
    private lateinit var user:User;
    private val REQUESTCODE_CAMERA = 999

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
        getUser()
    }

    private fun getUser() {
        ref.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user= snapshot.getValue(User::class.java)!!
                setEvents()
            }

            private fun setEvents() {
                binding.bottomNavigationView.setOnItemSelectedListener {
                    when(it.itemId){
                        R.id.menu_home -> replaceFragment(HomePageFragment())
                        R.id.menu_add -> openCamera()
                        R.id.menu_profile -> replaceFragment(ProfileFragment())
                        else ->{

                        }
                    }
                    true
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUESTCODE_CAMERA && resultCode == Activity.RESULT_OK && data != null) {
            try {
                val bitmap = data.extras?.get("data") as Bitmap
                showAddPlant(user,bitmap)
            } catch (e: Exception) {
                Log.d("Error", e.toString())
            }
        }
    }

    private fun showAddPlant(user: User, bitmap: Bitmap) {
        val bundle = Bundle()
        bundle.putSerializable("user",user)
        bundle.putParcelable("image", bitmap)
        val addPlantFragment = AddPlantFragment()
        addPlantFragment.arguments = bundle

        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentlayout, addPlantFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun openCamera(){
        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(
            camera,
            REQUESTCODE_CAMERA
        )
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
