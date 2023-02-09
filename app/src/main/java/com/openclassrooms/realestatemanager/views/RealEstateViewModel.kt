package com.openclassrooms.realestatemanager.views

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.repository.RealEstateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RealEstateViewModel(application: Application):AndroidViewModel(application) {
     var repository: RealEstateRepository = RealEstateRepository(application.applicationContext)

    private var readAll: LiveData<List<RealEstate>> = repository.getAllRealEstates()



    fun getRealEstate(id: Long): LiveData<RealEstate> {
        return repository.getRealEstate(id)
    }



    fun getAllRealEstates(
        isFiltered: Boolean?,
        minimumPrice: Int?, maximumPrice: Int?,
        minimumSurface: Int?, maximumSurface: Int?, numberOfPhotos: Int?,
        minimumEntryDate: String?,
        minimumSaleDate: String?
    ):LiveData<List<RealEstate>>{

        return if (isFiltered == true){
            repository.getRealEstatesFiltered(minimumPrice,maximumPrice,minimumSurface,maximumSurface,numberOfPhotos ,minimumEntryDate,minimumSaleDate)
        }else{
            readAll
        }

    }

    fun addRealEstate(realEstate: RealEstate){
        viewModelScope.launch (Dispatchers.IO) {
            repository.insertRealEstate(realEstate)
        }
    }

    fun deleteRealEstate(realEstate: RealEstate){
        viewModelScope.launch (Dispatchers.IO) {
            repository.deleteRealEstate(realEstate)
        }
    }

}