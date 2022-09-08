package com.openclassrooms.realestatemanager

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.databinding.ActivityAddOrCreateRealEstateBinding
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.views.RealEstateFragment


class AddOrCreateRealEstateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddOrCreateRealEstateBinding
    private lateinit var  newRealEstate: RealEstate
    lateinit var  type: String
    lateinit var status:String
    lateinit var agent:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddOrCreateRealEstateBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        //If intent coming from AddRealEstate
        if (intent.getSerializableExtra(MainActivity.ADD_REAL_ESTATE) != null) {
            newRealEstate = (intent.getSerializableExtra(MainActivity.ADD_REAL_ESTATE) as RealEstate)
        } //Else if intent coming from EditRealEstate
        else if (intent.getSerializableExtra(RealEstateFragment.EDIT_REAL_ESTATE) != null) {
            newRealEstate = intent.getSerializableExtra(RealEstateFragment.EDIT_REAL_ESTATE) as RealEstate
        }
    }


    private fun initializeSpinnerAdapter(resourceArray:Int,spinner: Spinner){
        val adapter = ArrayAdapter.createFromResource(
            this,
            resourceArray, R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        spinner.adapter(adapter)
    }
}