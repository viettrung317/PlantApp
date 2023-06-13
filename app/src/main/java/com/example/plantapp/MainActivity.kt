package com.example.plantapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.intl.Locale
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.plantapp.Onboarding.Onboarding1
import com.example.plantapp.account.LoginActivity
import com.example.plantapp.databinding.ActivityMainBinding
import com.example.plantapp.fragment.AddPlantFragment
import com.example.plantapp.fragment.HomePageFragment
import com.example.plantapp.fragment.ProfileFragment
import com.example.plantapp.model.User
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
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
    private val REQUEST_CODE_GPS_PERMISSION = 100
    private var address:String=""

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
    override fun onResume() {
        super.onResume()

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu quyền truy cập vị trí của người dùng nếu chưa được cấp
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_GPS_PERMISSION)
        } else {
            // Lấy tọa độ hiện tại nếu đã được cấp quyền truy cập vị trí
            getCurrentLocation()
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_GPS_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Nếu người dùng cấp quyền truy cập vị trí, lấy tọa độ hiện tại
                getCurrentLocation()
            } else {
                // Nếu người dùng từ chối cấp quyền truy cập vị trí, hiển thị thông báo hoặc xử lý khác
                Toast.makeText(this, "Không thể lấy tọa độ hiện tại vì quyền truy cập vị trí bị từ chối", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getCurrentLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                address=getAddress(location.latitude,location.longitude)

                // Cập nhật UI của bạn với dữ liệu về vị trí mới nhất
            }
        }
    }

    private fun getAddress(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this, java.util.Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1) as List<Address>

        if (addresses.isNotEmpty()) {
            val address: Address = addresses[0]
            return address.getAddressLine(0) // Trả về địa chỉ đầy đủ
        }
        return "" // Trả về chuỗi rỗng nếu không có kết quả
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
                        R.id.menu_profile -> openProfile(address)
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
    private fun openProfile(string: String) {
        val bundle = Bundle()
        bundle.putString("address",string)
        val profileFragment = ProfileFragment()
        profileFragment.arguments = bundle

        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentlayout, profileFragment)
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
