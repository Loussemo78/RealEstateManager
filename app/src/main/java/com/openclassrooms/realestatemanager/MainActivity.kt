package com.openclassrooms.realestatemanager

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.views.RealEstateFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

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
//                    val fragmentContainerViewDetail: Fragment? = supportFragmentManager
//                            .findFragmentById(R.id.activity_main_fragment_container_view_detail)
//                    if (fragmentContainerViewDetail == null) {
//                        supportFragmentManager.beginTransaction().replace(
//                                R.id.activity_main_fragment_container_view_list, MapsFragment())
//                                .commit()
//                    } else if (fragmentContainerViewDetail.isVisible()) {
//                        supportFragmentManager.beginTransaction().replace(
//                                R.id.activity_main_fragment_container_view_detail, MapsFragment()
//                        ).commit()
//                    }
                }
            }
            true
        }
    }
}