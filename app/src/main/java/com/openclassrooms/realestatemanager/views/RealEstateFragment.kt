package com.openclassrooms.realestatemanager.views

import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothClass.Device
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.openclassrooms.realestatemanager.MainActivity
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.RealEstateRecyclerViewAdapter
import com.openclassrooms.realestatemanager.database.RealEstateHandlerThread
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstateListBinding
import com.openclassrooms.realestatemanager.models.RealEstate


class RealEstateFragment: Fragment() , RealEstateRecyclerViewAdapter.OnRealEstateClickListener {

    private lateinit var binding: FragmentRealEstateListBinding
    private lateinit var realEstateViewModel: RealEstateViewModel
    private lateinit var recyclerView: RecyclerView


    private val EDIT_REQUEST_CODE = 25


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
            recyclerView.adapter = RealEstateRecyclerViewAdapter(activity as MainActivity, it, this).also {

            }

        })

        return binding.root
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
    // Declare our interface that will be implemented by any container activity


    companion object {
        fun newInstance() = RealEstateFragment()
        const val KEY = "RealEstateClicked"
        const val EDIT_REAL_ESTATE = "RealEstateToEdit"

        fun isTablet(context: Context): Boolean {

            val xlarge = context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_XLARGE
            val large = context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_LARGE
            return xlarge || large
        }

        fun isTablet1(context: Context): Boolean {
            val xlarge = context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == 4
            val large = context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_LARGE
            return xlarge || large
        }


    }

    override fun onRealEstateClicked(realEstate: RealEstate) {

        //...
       val tabletSize = resources.getBoolean(com.openclassrooms.realestatemanager.R.bool.isTablet)

        val fragmentDetail = RealEstateDetailFragment()
        val bundle = Bundle()
        bundle.putInt(RealEstateFragment.KEY, realEstate.id.toInt())
        fragmentDetail.arguments = bundle

        val fragmentContainerViewDetail = activity?.supportFragmentManager?.findFragmentById(
                com.openclassrooms.realestatemanager.R.id.activity_main_fragment_container_view_detail)



        /*if (context?.let { isTablet(it) } == false) {
            if (fragmentContainerViewDetail == null) {
                activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.activity_main_fragment_container_view_list, fragmentDetail)
                        ?.addToBackStack(RealEstateDetailFragment::class.java.simpleName)
                        ?.commit()
            } else if (!fragmentContainerViewDetail.isVisible) {
                activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.add(R.id.activity_main_fragment_container_view_detail, fragmentDetail)
                        ?.commit()
            }
        } else {*/
        if (!tabletSize) {

            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(com.openclassrooms.realestatemanager.R.id.activity_main_fragment_container_view_list, fragmentDetail)
                    ?.addToBackStack(RealEstateDetailFragment::class.java.simpleName)
                    ?.commit()
            // do something

        } else {
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.add(com.openclassrooms.realestatemanager.R.id.activity_main_fragment_container_view_detail, fragmentDetail)
                    ?.commit()

            // do something else
        }

            // }


            /* val fragmentDetail = RealEstateDetailFragment()
         val bundle = Bundle()
         bundle.putInt(RealEstateFragment.KEY, realEstate.id.toInt())
         fragmentDetail.arguments = bundle

         val fragmentContainerViewDetail = activity?.supportFragmentManager?.findFragmentById(
                 R.id.activity_main_fragment_container_view_detail)


         if (fragmentContainerViewDetail == null || !fragmentContainerViewDetail.isVisible) {
             activity?.supportFragmentManager
                     ?.beginTransaction()
                     ?.replace(R.id.activity_main_fragment_container_view_list,
                             fragmentDetail)
                     ?.addToBackStack(RealEstateDetailFragment::class.java.simpleName)
                     ?.commit()
         } else { //on tablet
             activity?.supportFragmentManager
                     ?.beginTransaction()
                     ?.replace(R.id.activity_main_fragment_container_view_detail,
                             fragmentDetail)
                     ?.commit()
         }*/


        }


}

