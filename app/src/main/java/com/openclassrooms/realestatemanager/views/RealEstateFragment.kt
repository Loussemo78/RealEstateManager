package com.openclassrooms.realestatemanager.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.adapter.RealEstateRecyclerViewAdapter
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstateListBinding

class RealEstateFragment: Fragment() {

private lateinit var binding : FragmentRealEstateListBinding
private lateinit var realEstateViewModel: RealEstateViewModel
private var layoutManager: RecyclerView.LayoutManager? = null
private lateinit var adapter: RecyclerView.Adapter<RealEstateRecyclerViewAdapter.RealEstateViewHolder>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        realEstateViewModel = ViewModelProvider(this)[RealEstateViewModel::class.java]
        realEstateViewModel.getAllRealEstates().observe(viewLifecycleOwner, Observer {
            adapter = RealEstateRecyclerViewAdapter(it)
            binding.recyclerView.adapter
            adapter.
            //adapter?.notifyDataSetChanged()
        })
        //initRecyclerView()
        //displayRealEstateList()
    }

//    private fun initRecyclerView(){
//        binding.recyclerView.layoutManager = LinearLayoutManager(context)
//        adapter = RealEstateRecyclerViewAdapter(it)
//        binding.recyclerView.adapter
//    }

//    private fun displayRealEstateList(){
////        realEstateViewModel.getAllRealEstates().observe(viewLifecycleOwner, Observer {
////
////        })
//    }
}
