package com.openclassrooms.realestatemanager

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.databinding.ActivitySearchRealEstateBinding
import com.openclassrooms.realestatemanager.utility.Utils
import com.openclassrooms.realestatemanager.views.RealEstateViewModel
import java.util.*


class SearchRealEstateActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchRealEstateBinding
    private lateinit var viewModel: RealEstateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchRealEstateBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        viewModel = ViewModelProvider(this)[RealEstateViewModel::class.java]
        initializeSearchItems()
    }

    private fun initializeSearchItems() {
        binding.activitySearchRealEstateButtonSearch.setOnClickListener {

            val critaries = SearchCritaries()
            val minimumPrice = binding.activitySearchRealEstatePriceMinimumEditText.text.toString()
            val maximumPrice = binding.activitySearchRealEstatePriceMaximumEditText.text.toString()
            val minimumSurface =
                binding.activitySearchRealEstateSurfaceMinimumEditText.text.toString()
            val maximumSurface =
                binding.activitySearchRealEstateSurfaceMaximumEditText.text.toString()
            val firstLocation =
                binding.activitySearchRealEstateFirstLocationEditText.text.toString()
            val numberOfPhotos =
                binding.activitySearchRealEstateNumberPhotosEditText.text.toString()
            val pointOfInterest =
                binding.activitySearchRealEstatePointOfInterestEditText.text.toString()
            val entryDate = binding.activitySearchRealEstateEntryDateSinceEditText.text.toString()
            val saleDate = binding.activitySearchRealEstateSaleDateSinceEditText.text.toString()

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
                critaries.numberOfPhotos = numberOfPhotos.toInt()
            } catch (t: Throwable) {
                t.printStackTrace() //if it's not an integer
            }

            try {
                critaries.pointOfInterest = pointOfInterest
            } catch (t: Throwable) {
                t.printStackTrace() //if it's not an integer
            }

            val entryDateInDate: Date = Utils.convertStringToDate(entryDate)
            try {
                critaries.entryDateInDate = entryDateInDate
            } catch (t: Throwable) {
                t.printStackTrace() //if it's not an integer
            }

            val saleDateInDate: Date = Utils.convertStringToDate(saleDate)
            try {
                critaries.saleDateInDate = saleDateInDate
            } catch (t: Throwable) {
                t.printStackTrace() //if it's not an integer
            }

            critaries.pointOfInterest = pointOfInterest
            critaries.entryDateInDate = entryDateInDate
            critaries.saleDateInDate = saleDateInDate


            //repository.setFilter(critaries)

            finish()

        }


    }

    data class SearchCritaries(
        var minimumPrice: Int? = 0,
        var maximumPrice: Int? = 0,
        var minimumSurface: Int? = 0,
         var maximumSurface: Int? = 0,
         var firstLocation: String? = "",
         var numberOfPhotos: Int? = 0,
         var pointOfInterest: String? = "",
         var entryDateInDate: Date? = null,
         var saleDateInDate: Date? = null
    ) {

    }


}