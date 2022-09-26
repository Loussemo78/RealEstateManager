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
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.MainActivity
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentMapBinding
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.utility.TAG_DETAILS_FRAGMENT
import com.openclassrooms.realestatemanager.utility.Utils


class MapFragment : Fragment(),OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var binding : FragmentMapBinding
    private lateinit var viewModel: RealEstateViewModel

    private lateinit var map: GoogleMap
    private lateinit var realEstateList: MutableList<RealEstate>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // MAP
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val newYork = LatLng(40.75691, -73.97619)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newYork, 11F))

        viewModel = ViewModelProvider(this)[RealEstateViewModel::class.java]
        viewModel
        realEstateList = mutableListOf<RealEstate>()
        val isFiltered =  arguments?.getBoolean("isFiltered")
        val minimumPrice =   arguments?.getInt("minimumPrice")
        val   maximumPrice = arguments?.getInt("maximumPrice")
        val   minimumSurface = arguments?.getInt("minimumSurface")
        val maximumSurface =  arguments?.getInt("maximumSurface")
        val firstLocation =  arguments?.getString("firstLocation")
        val pointOfInterest =  arguments?.getString("pointOfInterest")
        val numberOfPhotos = arguments?.getInt("numberOfPhotos")
        val minimumEntryDate =  arguments?.getString("minimumEntryDate")
        val minimumSaleDate = arguments?.getString("minimumSaleDate")

        viewModel.getAllRealEstates(isFiltered,minimumPrice,maximumPrice,minimumSurface,maximumSurface,firstLocation.toString(),pointOfInterest?.toInt(),numberOfPhotos.toString(),
            minimumEntryDate,
            minimumSaleDate).observe(viewLifecycleOwner, Observer {
                estates->
            estates.forEach {
                val realEstateId = it.id.toString()
                val latLng = LatLng(it.latitude, it.longitude)
                map.addMarker(MarkerOptions().position(latLng).title(realEstateId))
                realEstateList.add(it)
            }
        })



    }

    override fun onMarkerClick(p0: Marker): Boolean {
        val id = p0.title!!.toLong()
        (activity as MainActivity).setFragment(
                RealEstateDetailFragment.newInstance(id),
                true,
                TAG_DETAILS_FRAGMENT
        )

        return true
    }


    companion object{
        fun newInstance() = MapFragment()
        const val MAPS_MARKER_CLICK_REAL_ESTATE = "MAPS_MARKER_CLICK_REAL_ESTATE"

    }

}