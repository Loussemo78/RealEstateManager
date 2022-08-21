package com.openclassrooms.realestatemanager

import android.R.menu
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id. -> {
//                val intent = Intent(this, AddOrEditRealEstateActivity::class.java)
//                val realEstate = RealEstate()
//                intent.putExtra(ADD_REAL_ESTATE, realEstate as Serializable)
//                startActivityForResult(intent, ADD_REAL_ESTATE_REQUEST_CODE)
//            }
//            R.id.menu_search -> {
//                if (realEstateFilteredList.size() !== 0) realEstateFilteredList.clear()
//                val intent1 = Intent(this, SearchRealEstateProviderActivity::class.java)
//                startActivity(intent1)
//            }
//            R.id.menu_clear_filter -> repository.resetFilter()
//            else ->
//                return super.onOptionsItemSelected(item)
//        }
//        return true
//    }
}