package com.openclassrooms.realestatemanager

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.databinding.ActivitySearchRealEstateBinding
import com.openclassrooms.realestatemanager.views.RealEstateViewModel


class SearchRealEstateActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchRealEstateBinding
    private lateinit var viewModel: RealEstateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchRealEstateBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        viewModel = ViewModelProvider(this)[RealEstateViewModel::class.java]

    }

    private fun initializeSearchItems(){

    }
}