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
import com.openclassrooms.realestatemanager.MainActivity
import com.openclassrooms.realestatemanager.adapter.RealEstateRecyclerViewAdapter
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstateListBinding
import com.openclassrooms.realestatemanager.models.RealEstate


class RealEstateFragment: Fragment() , RealEstateRecyclerViewAdapter.OnRealEstateClickListener {

    private lateinit var binding: FragmentRealEstateListBinding
    private lateinit var realEstateViewModel: RealEstateViewModel
    private lateinit var recyclerView: RecyclerView




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRealEstateListBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)

        realEstateViewModel = ViewModelProvider(this)[RealEstateViewModel::class.java]

        val isFiltered = arguments?.getBoolean("isFiltered")
        val minimumPrice = arguments?.getInt("minimumPrice")
        val maximumPrice = arguments?.getInt("maximumPrice")
        val minimumSurface = arguments?.getInt("minimumSurface")
        val maximumSurface = arguments?.getInt("maximumSurface")
        val numberOfPhotos = arguments?.getInt("numberOfPhotos")
        val minimumEntryDate = arguments?.getString("minimumEntryDate")
        val minimumSaleDate = arguments?.getString("minimumSaleDate")



        realEstateViewModel.getAllRealEstates(isFiltered, minimumPrice, maximumPrice, minimumSurface, maximumSurface, numberOfPhotos, minimumEntryDate, minimumSaleDate).observe(viewLifecycleOwner, Observer {
            //SELECT * FROM real_estate_db WHERE price >= 1 AND price <= 1 AND surface >= 1 AND surface <= 1  AND entryDate >= 06/10/2022 AND dateOfSale >= 06/10/2022 AND numberOfPhotos >= 1
            recyclerView.adapter = RealEstateRecyclerViewAdapter(activity as MainActivity, it, this)
        })

        return binding.root
    }


    companion object {
        fun newInstance() = RealEstateFragment()
        const val KEY = "RealEstateClicked"
        const val EDIT_REAL_ESTATE = "RealEstateToEdit"
    }

    override fun onRealEstateClicked(realEstate: RealEstate) {

        //...
        val tabletSize = resources.getBoolean(com.openclassrooms.realestatemanager.R.bool.isTablet)

        val fragmentDetail = RealEstateDetailFragment()
        val bundle = Bundle()
        bundle.putInt(RealEstateFragment.KEY, realEstate.id.toInt())
        fragmentDetail.arguments = bundle

        if (!tabletSize) {
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(com.openclassrooms.realestatemanager.R.id.activity_main_fragment_container_view_list, fragmentDetail)
                    ?.addToBackStack(RealEstateDetailFragment::class.java.simpleName)
                    ?.commit()

        } else {
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(com.openclassrooms.realestatemanager.R.id.frame_layout_detail, fragmentDetail)
                    ?.commit()
            }
        }
    }

