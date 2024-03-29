package com.openclassrooms.realestatemanager

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.di.DI
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.utility.TAG_REAL_ESTATE_FRAGMENT
import com.openclassrooms.realestatemanager.views.MapFragment
import com.openclassrooms.realestatemanager.views.RealEstateDetailFragment
import com.openclassrooms.realestatemanager.views.RealEstateFragment
import com.openclassrooms.realestatemanager.views.RealEstateViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity()   {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: RealEstateViewModel
    private lateinit var  repository: RealEstateRepository


    private lateinit var realEstateFilteredList: ArrayList<RealEstate>


    companion object{
        private const val ALL_PERMISSIONS = 10
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
  // gestion connexion internet
        checkPermissionsGranted()

        repository =  DI.getRepository(application)
        GlobalScope.launch(Dispatchers.Default) {

            val repository = RealEstateRepository(this@MainActivity)
            val realEstateObject = RealEstate(1,"Type",12 ,"Place",
                    1500,2,"Description"
                    ,"https://www.notreloft.com/images/2016/10/loft-Manhattan-New-York-00500-800x533.jpg",3,"Address","mainPhoto",
                    "points","", null,-73.97619
                    ,40.75691,"video")

            repository.insertRealEstate(realEstateObject)
      }
        initializeBottomNavigationItemView()

        viewModel = ViewModelProvider(this)[RealEstateViewModel::class.java]


        //A - We only add DetailFragment in Tablet mode (If found frame_layout_detail)
        if (findViewById<View>(R.id.frame_layout_detail) != null)
        {
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_fragment_container_view_list,
                    RealEstateFragment()).commit()

            val fragmentDetail = RealEstateDetailFragment()
            val bundle = Bundle()
            bundle.putInt(RealEstateFragment.KEY, 1)
            fragmentDetail.arguments = bundle

           supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout_detail,
                    fragmentDetail)
                .addToBackStack(RealEstateDetailFragment::class.java.simpleName)
                .commit()
        }
        else
        supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_fragment_container_view_list,
                        RealEstateFragment()).commit()



        realEstateFilteredList = ArrayList()

    }




    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                val tabletSize = resources.getBoolean(com.openclassrooms.realestatemanager.R.bool.isTablet)

                val fragmentEdit = AddOrCreateRealEstateFragment()

                if (!tabletSize) {
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.activity_main_fragment_container_view_list,
                                    fragmentEdit)
                            .addToBackStack(AddOrCreateRealEstateFragment::class.java.simpleName)
                            .commit()


                } else {
                    supportFragmentManager
                            .beginTransaction()
                            .replace(com.openclassrooms.realestatemanager.R.id.frame_layout_detail, fragmentEdit)
                            .commit()

                }



            }
            R.id.menu_search -> {

                val tabletSize = resources.getBoolean(com.openclassrooms.realestatemanager.R.bool.isTablet)

                val fragmentSearch = SearchRealEstateFragment()

                if (!tabletSize) {
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.activity_main_fragment_container_view_list,
                                    fragmentSearch)
                            .addToBackStack(SearchRealEstateFragment::class.java.simpleName)
                            .commit()
                } else {
                    supportFragmentManager
                            .beginTransaction()
                            .replace(com.openclassrooms.realestatemanager.R.id.frame_layout_detail, fragmentSearch)
                            .commit()
                }
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun initializeBottomNavigationItemView(){
        binding.bottomNavigationView?.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.page1 -> {
                    val tabletSize = resources.getBoolean(com.openclassrooms.realestatemanager.R.bool.isTablet)

                    val fragmentList = RealEstateFragment()

                    if (!tabletSize) {
                        supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.activity_main_fragment_container_view_list,
                                        fragmentList)
                                .addToBackStack(RealEstateFragment::class.java.simpleName)
                                .commit()


                    } else {
                        startActivity(Intent(this, MainActivity::class.java))
                    }

                    }



                R.id.page2 -> {
                    val tabletSize = resources.getBoolean(com.openclassrooms.realestatemanager.R.bool.isTablet)

                    val fragmentMap = MapFragment()

                    if (!tabletSize) {
                        supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.activity_main_fragment_container_view_list,
                                        fragmentMap)
                                .addToBackStack(MapFragment::class.java.simpleName)
                                .commit()


                    } else {
                        supportFragmentManager
                                .beginTransaction()
                                .replace(com.openclassrooms.realestatemanager.R.id.frame_layout_detail, fragmentMap)
                                .commit()

                    }


                }
            }
            true
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun setFragment(fragment: Fragment, addBackStack: Boolean, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
            when (tag) {
                TAG_REAL_ESTATE_FRAGMENT -> transaction.replace(R.id.activity_main_fragment_container_view_list, fragment, tag)
                else -> transaction.replace(R.id.activity_main_fragment_container_view_list, fragment, tag)
            }

        if (addBackStack) transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun checkPermissionsGranted() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
                    == PackageManager.PERMISSION_GRANTED) && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
        } else {
            getPermissions()
        }
    }

    private fun getPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), ALL_PERMISSIONS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ALL_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] ==
                PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                getPermissions()
            }
        }
    }

}