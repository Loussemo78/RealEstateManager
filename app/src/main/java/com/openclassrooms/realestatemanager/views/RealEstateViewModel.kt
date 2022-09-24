package com.openclassrooms.realestatemanager.views

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.repository.RealEstateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RealEstateViewModel(application: Application):AndroidViewModel(application) {
    private val repository: RealEstateRepository = RealEstateRepository(application.applicationContext)
    private var readAll: LiveData<List<RealEstate>> = repository.getAllRealEstates()



    fun getRealEstate(id: Long): LiveData<RealEstate> {
        return repository.getRealEstate(id)
    }

    fun getAllRealEstates():LiveData<List<RealEstate>>{
       return repository.getAllRealEstates()
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