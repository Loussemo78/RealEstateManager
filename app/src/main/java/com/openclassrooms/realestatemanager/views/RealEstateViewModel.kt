package com.openclassrooms.realestatemanager.views

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.database.RealEstateQuery
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.repository.RealEstateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RealEstateViewModel(application: Application):AndroidViewModel(application) {
    private val repository: RealEstateRepository = RealEstateRepository(application.applicationContext)
    private var readAll: LiveData<List<RealEstate>> = repository.getAllRealEstates()



    fun getRealEstate(id: Long): LiveData<RealEstate> {
        return repository.getRealEstate(id)
    }

    fun getSearchRealEstate(queryToConvert:String, args:ArrayList<Any>):LiveData<List<RealEstate>>{
        val query = SimpleSQLiteQuery(queryToConvert,args.toArray())
        args.forEach {
            if (it is Long) Log.e("GET_ESTATES_BY_SEARCH", "Args : ${SimpleDateFormat("dd/MM/yyyy").format(Date(it))}")
            else Log.e("GET_ESTATES_BY_SEARCH", "Args : $it")
        }
        return repository.getRealEstatesFiltered(query)
    }

    fun getAllRealEstates(
        isFiltered: Boolean?,
        minimumPrice: Int?, maximumPrice: Int?,
        minimumSurface: Int?, maximumSurface: Int?,
        firstLocation: String, numberOfPhotos: Int?,
        description: String?,
        pointOfInterest: String?, minimumEntryDate: String?,
        minimumSaleDate: String?
    ):LiveData<List<RealEstate>>{

        if (isFiltered == true){
            repository.getRealEstatesFiltered(RealEstateQuery.generateQuery(minimumPrice,maximumPrice,minimumSurface,maximumSurface,firstLocation,numberOfPhotos, description ,pointOfInterest.toString(),minimumEntryDate,minimumSaleDate))
        }else{
            return readAll
        }
        return readAll
    }

    fun addRealEstate(realEstate: RealEstate){
        viewModelScope.launch (Dispatchers.IO) {
            repository.insertRealEstate(realEstate)
        }
    }

    fun updateRealEstate(realEstate: RealEstate){
        viewModelScope.launch (Dispatchers.IO) {
            repository.updateRealEstate(realEstate)
        }
    }


    fun deleteRealEstate(realEstate: RealEstate){
        viewModelScope.launch (Dispatchers.IO) {
            repository.deleteRealEstate(realEstate)
        }
    }
}