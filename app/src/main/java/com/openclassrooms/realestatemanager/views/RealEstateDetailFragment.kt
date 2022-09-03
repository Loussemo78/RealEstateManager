package com.openclassrooms.realestatemanager.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstateDetailBinding
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.utility.Utils
import java.util.*


class RealEstateDetailFragment:Fragment(),OnMapReadyCallback {

private lateinit var binding:FragmentRealEstateDetailBinding
private lateinit var realEstate:RealEstate
    private lateinit var  map: GoogleMap


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentRealEstateDetailBinding.inflate(inflater,container,false)
        if (arguments?.getSerializable(RealEstateFragment.KEY) != null) {
            realEstate = requireArguments().getSerializable(RealEstateFragment.KEY) as RealEstate
        } else if (arguments?.getSerializable(MapFragment.MAPS_MARKER_CLICK_REAL_ESTATE) != null) {
            realEstate = (arguments?.getSerializable(MapFragment.MAPS_MARKER_CLICK_REAL_ESTATE) as RealEstate)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(binding.fragmentOnClickRealEstateImageView.context)
                .load(realEstate.mainPhotoUrl)
                .into(binding.fragmentOnClickRealEstateImageView)

        binding.fragmentOnClickRealEstateDescription.text = realEstate.description

        Glide.with(binding.fragmentOnClickRealEstateAgentPhoto.context)
                .load(realEstate.mainPhotoUrl)
                .into(binding.fragmentOnClickRealEstateAgentPhoto)

        binding.fragmentOnClickRealEstateAgentName.text = realEstate.agent

        if (realEstate.status == "For sale") {
            binding.fragmentOnClickRealEstateStatus.text = realEstate.status
            binding.fragmentOnClickRealEstateStatus.setTextColor(resources
                    .getColor(R.color.fragment_on_click_real_estate_for_sale_status_color))
        } else {
            binding.fragmentOnClickRealEstateStatus.text = realEstate.status
            binding.fragmentOnClickRealEstateStatus.setTextColor(resources.getColor(
                    R.color.fragment_on_click_real_estate_sold_status_color))
        }


        val dateOfSale: Date? = realEstate.dateOfSale


        val surface: String = realEstate.surface.toString() + " sq" + " m"
        val numberOfRooms = realEstate.numberOfRooms.toString()
        val numberOfBathrooms = realEstate.numberOfBathRooms.toString()
        val numberOfBedrooms = realEstate.numberOfBedRooms.toString()


        binding.fragmentOnClickRealEstateSurfaceValue.text = surface
        binding.fragmentOnClickRealEstateRoomsValue.text = numberOfRooms
        binding.fragmentOnClickRealEstateBathroomsValue.text = numberOfBathrooms
        binding.fragmentOnClickRealEstateBedroomsValue.text = numberOfBedrooms
     //   binding.fragmentOnClickRealEstateLocationValue.setText(mRealEstate.getSecondLocation())
        binding.fragmentOnClickRealEstatePointsOfInterestValue.text = realEstate.pointsOfInterest
        binding.fragmentOnClickRealEstatePriceValue.text = "$" + realEstate.price
        binding.fragmentOnClickRealEstateEntryDateValue.text = Utils.convertDateToString(realEstate.entryDate)
        if (dateOfSale != null) binding.fragmentOnClickRealEstateSaleDateValue.text = Utils.convertDateToString(dateOfSale)


        val supportMapFragment = this.childFragmentManager
                .findFragmentById(R.id.fragment_on_click_real_estate_map_fragment) as SupportMapFragment?
        supportMapFragment!!.getMapAsync(this)

    }


    companion object {
        //fun newInstance() = RealEstateDetailFragment()
        const val KEY_ESTATE_FOR_DETAILS: String = "ID_ESTATE"

        fun newInstance(id: Long): RealEstateDetailFragment {
            val fragment = RealEstateDetailFragment()
            val args = Bundle()
            args.putLong(KEY_ESTATE_FOR_DETAILS, id)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        if (realEstate.latitude != 0.0 && realEstate.longitude != 0.0) {
            val realEstateLatLng = LatLng(realEstate.latitude, realEstate.longitude)
            map.addMarker(MarkerOptions().position(realEstateLatLng).title("Real estate marker"))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(realEstateLatLng, 18f))
        }
    }
}