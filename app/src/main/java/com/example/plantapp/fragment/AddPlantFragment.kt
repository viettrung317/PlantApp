package com.example.plantapp.fragment

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.plantapp.R
import com.example.plantapp.databinding.FragmentAddPlantBinding
import com.example.plantapp.model.Plant
import com.example.plantapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.*

class AddPlantFragment : Fragment() {
    private lateinit var binding: FragmentAddPlantBinding
    private val mAuth: FirebaseAuth = Firebase.auth
    private val data: FirebaseDatabase = Firebase.database
    private val ref: DatabaseReference =data.getReference()
    private val userfb: FirebaseUser =mAuth.currentUser!!
    private val uid:String=userfb.uid
    private var user: User?= User()
    private var plant:Plant?= Plant()
    private var listPlant:MutableList<Plant> = mutableListOf()
    private var bitmapImages: Bitmap? = null
    private var storage: FirebaseStorage = FirebaseStorage.getInstance()
    private var type= mutableListOf<String>();

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPlantBinding.inflate(layoutInflater, container, false)
        val storageRef = storage.getReferenceFromUrl("gs://plantapp-b3345.appspot.com")
        getImages()
        binding.imagebackAdd.setOnClickListener(){
            if (activity?.supportFragmentManager?.backStackEntryCount!! > 0) {
                activity?.supportFragmentManager?.popBackStack()
            } else {
                // FragmentManager không có Fragment nào trên Backstack, không gọi onBackPressed để tránh đóng Activity.
                // Thực hiện hành động khác nếu cần thiết.
            }
        }
        binding.rdoSelecttype.setOnCheckedChangeListener{ group, checkedId ->
            when(checkedId){
                R.id.radioButton_cute ->{ type.add("#Cute") }
                R.id.radioButton_mini ->{type.add("#Mini")}
                R.id.radioButton_vegetables ->{type.add("#Vegetables")}
                R.id.radioButton_danger ->{type.add("#Danger")}
            }
        }
        binding.btnAddPlant.setOnClickListener(){
            val name:String=binding.txtplantName.text.toString()
            val des:String=binding.txtDescription.text.toString()
            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(des) || binding.rdoSelecttype.checkedRadioButtonId == -1){
                binding.txtErrorAdd.setText("Error : Vui lòng điền đủ thông tin !")
                return@setOnClickListener
            }
            binding.progressBar.visibility=View.VISIBLE

            val calendar = Calendar.getInstance()
            val mountainsRef = storageRef.child("imagePlant" + calendar.timeInMillis + ".png")
            // Get the data from an ImageView as bytes
            // Get the data from an ImageView as bytes
            val baos = ByteArrayOutputStream()
            bitmapImages?.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val data = baos.toByteArray()
            val uploadTask = mountainsRef.putBytes(data)
            uploadTask.addOnFailureListener { exception ->
                // Upload failed
                Log.e("AAA","Upload Failed: $exception")
                Toast.makeText(activity, "Upload Failed: $exception", Toast.LENGTH_LONG).show()
            }
                .addOnSuccessListener { taskSnapshot ->
                    // Upload successful
                    mountainsRef.downloadUrl.addOnSuccessListener { uri ->
                        val name: String = binding.txtplantName.text.toString()
                        val des: String = binding.txtDescription.text.toString()
                        listPlant= user?.listPhotoGraphy!!
                        plant?.plantName=name
                        plant?.imagePlant=uri.toString()
                        plant?.description=des;
                        plant?.type=type
                        listPlant.add(plant!!)
                        user?.listPhotoGraphy=listPlant
                        ref.child("User").child(uid).setValue(user).addOnCompleteListener(){
                            if(it.isSuccessful){
                                if (activity?.supportFragmentManager?.backStackEntryCount!! > 0) {
                                    activity?.supportFragmentManager?.popBackStack()
                                } else {
                                    // FragmentManager không có Fragment nào trên Backstack, không gọi onBackPressed để tránh đóng Activity.
                                    // Thực hiện hành động khác nếu cần thiết.
                                }
                            }
                        }
                    }
                }
        }
        return binding.root
    }


    private fun getImages() {
        val bundle=arguments
        bitmapImages=bundle?.getParcelable<Bitmap>("image")
        user= bundle?.getSerializable("user") as User?
        binding.imageaddplant.setImageBitmap(bitmapImages)
    }
}
