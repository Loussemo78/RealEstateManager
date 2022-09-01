package com.openclassrooms.realestatemanager

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.utility.TAG_REAL_ESTATE_FRAGMENT
import com.openclassrooms.realestatemanager.views.MapFragment
import com.openclassrooms.realestatemanager.views.RealEstateFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        GlobalScope.launch(Dispatchers.Main) {

            val repository = RealEstateRepository(this@MainActivity)
            val realEstateObject = RealEstate(1,"Type",12 ,"Place",
                    1500,2,3,1,"Description"
                    ,"PhotoUrl",3,"Address","mainPhoto",
                    "points",47.244854, 3.346571, "Status", Date(2022,3,12),Date(2022,3,12),"Agent"
                    ,"photoUrl","video")

            repository.insertRealEstate(realEstateObject)
        }
        initializeBottomNavigationItemView()
        supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_fragment_container_view_list,
                        RealEstateFragment()).commit()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }



    private fun initializeBottomNavigationItemView(){
        binding.bottomNavigationView.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.page1 -> supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_fragment_container_view_list,
                                RealEstateFragment()).commit()
                R.id.page2 -> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.activity_main_fragment_container_view_list,
                                    MapFragment()).commit()
                }
            }
            true
        }
    }

    fun setFragment(fragment: Fragment, addBackStack: Boolean, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
            when (tag) {
                TAG_REAL_ESTATE_FRAGMENT -> transaction.replace(R.id.activity_main_fragment_container_view_list, fragment, tag)
                else -> transaction.replace(R.id.activity_main_fragment_container_view_list, fragment, tag)
            }
//         else {
//            transaction.replace(R.id.main_frame, fragment, tag)
//        }
        if (addBackStack) transaction.addToBackStack(null)
        transaction.commit()
    }

}