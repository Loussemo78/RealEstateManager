package com.openclassrooms.realestatemanager.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.MainActivity
import com.openclassrooms.realestatemanager.adapter.PickPhotosRecyclerViewAdapter
import com.openclassrooms.realestatemanager.databinding.FragmentPickPhotosListBinding
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstatePhotos


class PickPhotosFragment : Fragment() {

    private lateinit var recyclerview: RecyclerView
    private lateinit var binding : FragmentPickPhotosListBinding
    private lateinit var adapter: PickPhotosRecyclerViewAdapter
    private lateinit var newRealEstate: RealEstate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPickPhotosListBinding.inflate(inflater,container,false)
        recyclerview.layoutManager = LinearLayoutManager(activity)
        return binding.root
    }

    override fun onViewCreated(view: View,  savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerview = view as RecyclerView
        recyclerview.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL, false
        )

        //If realEstate come from MainActivity to add a real estate
        if (requireActivity().intent.getSerializableExtra(MainActivity.ADD_REAL_ESTATE) != null) {
            newRealEstate = (requireActivity().intent
                .getSerializableExtra(MainActivity.ADD_REAL_ESTATE) as RealEstate?)!!
        } //Else if realEstate come from an existing real estate to edit
        else if (requireActivity().intent.getSerializableExtra(
                RealEstateFragment.EDIT_REAL_ESTATE
            ) != null
        ) {
            newRealEstate = requireActivity().intent.getSerializableExtra(
                RealEstateFragment.EDIT_REAL_ESTATE
            ) as RealEstate
        }
        adapter = PickPhotosRecyclerViewAdapter()
        recyclerview.adapter = adapter
        if (newRealEstate.listPhotos != null) adapter.setRealEstatePhotosList(newRealEstate.listPhotos as ArrayList<RealEstatePhotos>)
    }


}