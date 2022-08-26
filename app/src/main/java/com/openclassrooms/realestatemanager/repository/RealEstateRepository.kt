package com.openclassrooms.realestatemanager.repository

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.dao.RealEstateDao
import com.openclassrooms.realestatemanager.models.RealEstate

class RealEstateRepository(private val dao: RealEstateDao) {

    suspend fun insertRealEstate(realEstate: RealEstate) =
            dao.insertRealEstate(realEstate)

    suspend fun updateRealEstate(realEstate: RealEstate) =
            dao.updateRealEstate(realEstate)

    suspend fun deleteRealEstate(realEstate: RealEstate) =
            dao.updateRealEstate(realEstate)


    fun getAllRealEstates():LiveData<List<RealEstate>> =
            dao.getRealEstates()
}