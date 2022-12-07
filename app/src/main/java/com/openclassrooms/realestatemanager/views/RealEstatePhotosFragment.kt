package com.openclassrooms.realestatemanager.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.adapter.RealEstatePhotosRecyclerViewAdapter
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstatePhotosListBinding
import com.openclassrooms.realestatemanager.models.RealEstate


class RealEstatePhotosFragment : Fragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var binding : FragmentRealEstatePhotosListBinding
    private lateinit var adapter: RealEstatePhotosRecyclerViewAdapter
    var mRealEstate: RealEstate? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRealEstatePhotosListBinding.inflate(inflater,container,false)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        return binding.root
    }

    override fun onViewCreated(view: View,  savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL, false
        )
        if (requireParentFragment().requireArguments().getSerializable(RealEstateFragment.KEY) != null) {
            mRealEstate = requireParentFragment().requireArguments().getSerializable(
                RealEstateFragment.KEY
            ) as RealEstate?
        } else if (requireParentFragment().requireArguments().getSerializable(
                MapFragment.MAPS_MARKER_CLICK_REAL_ESTATE
            ) != null
        ) {
            mRealEstate = requireParentFragment().requireArguments().getSerializable(
                MapFragment.MAPS_MARKER_CLICK_REAL_ESTATE
            ) as RealEstate?
        }
        adapter = RealEstatePhotosRecyclerViewAdapter() //empty constructor adapter
        recyclerView.adapter = adapter
        adapter.setRealEstatePhotosList(mRealEstate?.listPhotos)
    }

}