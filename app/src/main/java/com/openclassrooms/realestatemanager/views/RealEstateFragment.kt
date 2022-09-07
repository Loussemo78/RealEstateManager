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


class RealEstateFragment: Fragment() {

private lateinit var binding : FragmentRealEstateListBinding
private lateinit var realEstateViewModel: RealEstateViewModel
private lateinit var recyclerView: RecyclerView


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

    }


}
