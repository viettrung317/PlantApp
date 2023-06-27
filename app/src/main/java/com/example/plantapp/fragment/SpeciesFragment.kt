package com.example.plantapp.fragment


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantapp.Adapter.SpeciesAdapter
//import com.example.plantapp.Adapter.SpeciesAdapter
import com.example.plantapp.databinding.FragmentSpeciesBinding
import com.example.plantapp.model.Species
import com.example.plantapp.viewModel.ViewModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class SpeciesFragment : Fragment(), SpeciesAdapter.OnItemClickListener {
    private lateinit var viewModel: ViewModel
    private lateinit var binding: FragmentSpeciesBinding
    private val data: FirebaseDatabase = Firebase.database
    private val ref: DatabaseReference =data.getReference("Data")
    private var listSpecies= mutableListOf<Species>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSpeciesBinding.inflate(layoutInflater,container,false)
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getListData()
    }


    private fun setEvents(listSpecies: MutableList<Species>) {
        binding.imgExitSpecies.setOnClickListener{
            if (activity?.supportFragmentManager?.backStackEntryCount!! > 0) {
                activity?.supportFragmentManager?.popBackStack()
            } else {
                // FragmentManager không có Fragment nào trên Backstack, không gọi onBackPressed để tránh đóng Activity.
                // Thực hiện hành động khác nếu cần thiết.
            }
        }
        binding.txtSeachSpecies.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.imageView111.visibility=View.GONE
                var list= mutableListOf<Species>()
                for (species:Species in listSpecies) {
                    if (species.nameSpecies?.toLowerCase(Locale.getDefault())
                            ?.contains(s.toString().toLowerCase(Locale.getDefault())) == true)

                    {
                        list.add(species)
                    }
                }
                val adapter = SpeciesAdapter(list,this@SpeciesFragment)
                binding.rcvSpecies.adapter = adapter

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun getListData() {
        viewModel.getListSpecies().observe(viewLifecycleOwner) { listSpecise1 ->
            listSpecies= listSpecise1 as MutableList<Species>
            val sortedList = listSpecies.sortedBy { it.nameSpecies }
            setData(sortedList as MutableList<Species>)
            setEvents(sortedList)
        }
        viewModel.loadSpecies()
    }
    private fun setData(listSpecies: MutableList<Species>) {
        // Khởi tạo RecyclerView
        binding.rcvSpecies.layoutManager = LinearLayoutManager(requireContext())
        // Khởi tạo adapter và vẽ lên RecyclerView
        val adapter = SpeciesAdapter(listSpecies, this@SpeciesFragment)
        binding.rcvSpecies.adapter = adapter
    }

    override fun onItemClick(position: Int) {
        val sortedList = listSpecies.sortedBy { it.nameSpecies }
        val species = sortedList[position]
        var position:Int=0
        for (species1:Species in listSpecies){
            position++
            if(species1.nameSpecies.equals(species.nameSpecies)){
                break
            }
        }
        val bundle = Bundle()
        bundle.putSerializable("species",species)
        bundle.putInt("positionSpecies",position)
        val listPlantFragment = ListPlantFragment()
        listPlantFragment.arguments = bundle
        val fragmentLayout = requireActivity().findViewById<FrameLayout>(com.example.plantapp.R.id.fragmentlayout)
        fragmentLayout?.let {
            val fragmentManager = requireActivity().supportFragmentManager.beginTransaction()
            fragmentManager.replace(it.id, listPlantFragment)
            fragmentManager.addToBackStack(null)
            fragmentManager.commit()
        }
    }

}
