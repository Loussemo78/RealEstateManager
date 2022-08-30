package com.openclassrooms.realestatemanager.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.openclassrooms.realestatemanager.databinding.FragmentMapBinding
import com.openclassrooms.realestatemanager.models.RealEstate


class MapFragment : Fragment(),OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var binding : FragmentMapBinding
    private lateinit var viewModel: RealEstateViewModel

    private lateinit var map: GoogleMap
    private lateinit var realEstateList: MutableList<RealEstate>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)

        return binding.root
    }



    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val newYork = LatLng(40.75691, -73.97619)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newYork, 11F))

        viewModel = ViewModelProvider(this)[RealEstateViewModel::class.java]
        realEstateList = mutableListOf<RealEstate>()

        viewModel.getAllRealEstates().observe(viewLifecycleOwner , Observer {
            it.forEach {
                realEstateList.add(it)
            }
        })



    }

    override fun onMarkerClick(p0: Marker): Boolean {
        TODO("Not yet implemented")
    }


    companion object{
        fun newInstance() = MapFragment()
    }

}