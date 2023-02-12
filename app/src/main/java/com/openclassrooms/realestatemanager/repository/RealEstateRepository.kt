package com.openclassrooms.realestatemanager.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.models.RealEstate


class RealEstateRepository( val context: Context) {

    private val dao : RealEstateDao = RealEstateDatabase.getInstance(context)?.realEstateDao!!

     fun insertRealEstate(realEstate: RealEstate) =
            dao.insertRealEstate(realEstate)


     fun deleteRealEstate(realEstate: RealEstate) =
            dao.deleteRealEstate(realEstate)

    fun getAllRealEstates():LiveData<List<RealEstate>> =
            dao.getRealEstates()

    fun getRealEstate(id: Long): LiveData<RealEstate> =
            dao.getRealEstate(id)

    //minimumPrice,maximumPrice,minimumSurface,maximumSurface,firstLocation,numberOfRooms, description ,pointOfInterest.toString(),minimumEntryDate,minimumSaleDate
    fun getRealEstatesFiltered(
        minimumPrice: Int?,
        maximumPrice: Int?, minimumSurface: Int?, maximumSurface: Int?, numberOfRooms: Int?, minimumEntryDate: String?, minimumSaleDate: String?
    ): LiveData<List<RealEstate>>{
       return dao.getRealEstatesFiltered(minimumPrice,maximumPrice, minimumSurface, maximumSurface, numberOfRooms, minimumEntryDate, minimumSaleDate )
    }
}