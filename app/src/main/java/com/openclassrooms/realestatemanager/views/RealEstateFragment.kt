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
        realEstateViewModel.getAllRealEstates().observe(viewLifecycleOwner, Observer {

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
//    fun handleRealEstate(realEstate: RealEstate) {
//        val RealEstateDetailFragment = RealEstateDetailFragment()
//        val args = Bundle()
//        args.putSerializable(KEY, realEstate)
//        RealEstateDetailFragment.arguments = args
//        val fragmentContainerViewDetail = parentFragmentManager.findFragmentById(
//                R.id.activity_main_fragment_container_view_detail)
//
//
//        if (fragmentContainerViewDetail == null) {
//            parentFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.activity_main_fragment_container_view_list,
//                            RealEstateDetailFragment)
//                    .addToBackStack(RealEstateDetailFragment::class.java.simpleName)
//                    .commit()
//        } else if (fragmentContainerViewDetail.isVisible) { //on tablet
//            parentFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.activity_main_fragment_container_view_detail,
//                            RealEstateDetailFragment)
//                    .commit()
//        }
//    }


    companion object {
        fun newInstance() = RealEstateFragment()
        const val KEY = "RealEstateClicked"
        const val EDIT_REAL_ESTATE = "RealEstateToEdit"

    }


}
