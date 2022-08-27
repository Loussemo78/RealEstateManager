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

private var binding : FragmentRealEstateListBinding? = null
private lateinit var realEstateViewModel: RealEstateViewModel
//private var adapter: RecyclerView.Adapter<RealEstateRecyclerViewAdapter.RealEstateViewHolder>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = binding?.root



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        realEstateViewModel = ViewModelProvider(this)[RealEstateViewModel::class.java]

    }

    private fun initRecyclerView(){
        binding?.recyclerView?.layoutManager = LinearLayoutManager(this)
    }

    private fun displayRealEstateList(){
        realEstateViewModel.getAllRealEstates().observe(this, Observer {
            binding?.recyclerView?.adapter = RealEstateRecyclerViewAdapter(it)
        })
    }
}
