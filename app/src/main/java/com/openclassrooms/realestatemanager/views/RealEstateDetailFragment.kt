package com.openclassrooms.realestatemanager.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstateDetailBinding
import com.openclassrooms.realestatemanager.models.RealEstate

class RealEstateDetailFragment:Fragment() {

private lateinit var binding:FragmentRealEstateDetailBinding
private lateinit var realEstate:RealEstate


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

}