package com.openclassrooms.realestatemanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.databinding.FragmentSearchRealEstateBinding
import com.openclassrooms.realestatemanager.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.views.RealEstateFragment
import com.openclassrooms.realestatemanager.views.RealEstateViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SearchRealEstateFragment : Fragment() {

    private lateinit var binding: FragmentSearchRealEstateBinding
    private lateinit var viewModel: RealEstateViewModel
    private lateinit var repository: RealEstateRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        repository = RealEstateRepository(requireActivity())
        viewModel = ViewModelProvider(this)[RealEstateViewModel::class.java]

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchRealEstateBinding.inflate(layoutInflater)
        val view: View = binding.root
        initializeSearchItems()
        return view
    }

    private fun initializeSearchItems() {
        binding.fragmentSearchRealEstateButtonSearch.setOnClickListener {

            val critaries = SearchCritaries()
            val minimumPrice = binding.fragmentSearchRealEstatePriceMinimumEditText.text.toString()
            val maximumPrice = binding.fragmentSearchRealEstatePriceMaximumEditText.text.toString()
            val minimumSurface =
                    binding.fragmentSearchRealEstateSurfaceMinimumEditText.text.toString()
            val maximumSurface =
                    binding.fragmentSearchRealEstateSurfaceMaximumEditText.text.toString()
            val firstLocation =
                    binding.fragmentSearchRealEstateFirstLocationEditText.text.toString()
            val numberOfRooms =
                    binding.fragmentSearchRealEstateNumberRoomsEditText.text.toString()

            val entryDate = binding.fragmentSearchRealEstateEntryDateSinceEditText.text.toString()
            val saleDate = binding.fragmentSearchRealEstateSaleDateSinceEditText.text.toString()

            try {
                critaries.minimumPrice = minimumPrice.toInt()
            } catch (t: Throwable) {
                t.printStackTrace() //if it's not an integer
            }

            try {
                critaries.maximumPrice = maximumPrice.toInt()
            } catch (t: Throwable) {
                t.printStackTrace() //if it's not an integer
            }

            try {
                critaries.minimumSurface = minimumSurface.toInt()
            } catch (t: Throwable) {
                t.printStackTrace() //if it's not an integer
            }
            try {
                critaries.maximumSurface = maximumSurface.toInt()
            } catch (t: Throwable) {
                t.printStackTrace() //if it's not an integer
            }

            try {
                critaries.firstLocation = firstLocation
            } catch (t: Throwable) {
                t.printStackTrace() //if it's not an integer
            }

            try {
                critaries.numberOfRooms = numberOfRooms.toInt()
            } catch (t: Throwable) {
                t.printStackTrace() //if it's not an integer
            }


            val entryDateInDate: String? = entryDate
            try {
                critaries.entryDateInDate = entryDateInDate
            } catch (t: Throwable) {
                t.printStackTrace() //if it's not an integer
            }

            val saleDateInDate: String? = saleDate
            try {
                critaries.saleDateInDate = saleDateInDate
            } catch (t: Throwable) {
                t.printStackTrace() //if it's not an integer
            }

            critaries.entryDateInDate = entryDateInDate
            critaries.saleDateInDate = saleDateInDate


            GlobalScope.launch(Dispatchers.Main) {

                Toast.makeText(requireActivity(), "filter submit ok", Toast.LENGTH_SHORT).show()
                val bundle = Bundle()
                bundle.putBoolean("isFiltered", true)
                bundle.putInt("minimumPrice", minimumPrice.toInt())
                bundle.putInt("maximumPrice", maximumPrice.toInt())
                bundle.putInt("minimumSurface", minimumSurface.toInt())
                bundle.putInt("maximumSurface", maximumSurface.toInt())
                bundle.putString("firstLocation", firstLocation)
                bundle.putInt("numberOfRooms", numberOfRooms.toInt())
                bundle.putString("minimumEntryDate", entryDateInDate.toString())
                bundle.putString("minimumSaleDate", saleDateInDate.toString())


                val fragment = RealEstateFragment()
                fragment.arguments = bundle

                requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.activity_main_fragment_container_view_list, fragment)
                        .addToBackStack(RealEstateFragment::class.java.simpleName)
                        .commit();


            }

        }


    }

    data class SearchCritaries(
            var minimumPrice: Int? = 0,
            var maximumPrice: Int? = 0,
            var minimumSurface: Int? = 0,
            var maximumSurface: Int? = 0,
            var firstLocation: String? = "",
            var description: String? = "",
            var numberOfRooms: Int? = 0,
            var entryDateInDate: String? = "",
            var saleDateInDate: String? = "",
    ) {

    }
}