package com.openclassrooms.realestatemanager.views

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.MainActivity
import com.openclassrooms.realestatemanager.adapter.RealEstateRecyclerViewAdapter
import com.openclassrooms.realestatemanager.database.RealEstateHandlerThread
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstateListBinding
import com.openclassrooms.realestatemanager.models.RealEstate


class RealEstateFragment: Fragment() {

    private lateinit var binding : FragmentRealEstateListBinding
    private lateinit var realEstateViewModel: RealEstateViewModel
    private lateinit var recyclerView: RecyclerView

    private val EDIT_REQUEST_CODE = 25


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        binding = FragmentRealEstateListBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)

        realEstateViewModel = ViewModelProvider(this)[RealEstateViewModel::class.java]

        val isFiltered =  arguments?.getBoolean("isFiltered")
        val minimumPrice =   arguments?.getInt("minimumPrice")
        val maximumPrice = arguments?.getInt("maximumPrice")
        val minimumSurface = arguments?.getInt("minimumSurface")
        val maximumSurface =  arguments?.getInt("maximumSurface")
        val firstLocation =  arguments?.getString("firstLocation")
        val pointOfInterest =  arguments?.getString("pointOfInterest")
        val numberOfPhotos = arguments?.getInt("numberOfPhotos")
        val description = arguments?.getString("description")
        val minimumEntryDate =  arguments?.getString("minimumEntryDate")
        val minimumSaleDate = arguments?.getString("minimumSaleDate")



            realEstateViewModel.getAllRealEstates(isFiltered,minimumPrice,maximumPrice,minimumSurface,maximumSurface,
                    firstLocation.toString(),numberOfPhotos,description,pointOfInterest?.toString(),minimumEntryDate,minimumSaleDate).observe(viewLifecycleOwner, Observer {
            //SELECT * FROM real_estate_db WHERE price >= 1 AND price <= 1 AND surface >= 1 AND surface <= 1  AND entryDate >= 06/10/2022 AND dateOfSale >= 06/10/2022 AND numberOfPhotos >= 1
                recyclerView.adapter = RealEstateRecyclerViewAdapter(activity as MainActivity, it)
            })

        return  binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val realEstate = data?.getSerializableExtra(EDIT_REAL_ESTATE) as RealEstate?
                val realEstateHandlerThread = RealEstateHandlerThread("UpdateRealEstateInDatabase")
                realEstateHandlerThread.startUpdateRealEstateHandler(realEstate, realEstateViewModel)
            }
        }
    }


    companion object {
        fun newInstance() = RealEstateFragment()
        const val KEY = "RealEstateClicked"
        const val EDIT_REAL_ESTATE = "RealEstateToEdit"

    }


}
