package com.openclassrooms.realestatemanager.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.models.RealEstate

@Dao
interface RealEstateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertRealEstate(realEstate: RealEstate?): Long

    //for test
    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insertAll(realEstates: List<RealEstate>)

    @Query("SELECT * FROM real_estate_db WHERE id = :realEstateId")
     fun getRealEstate(realEstateId: Long): LiveData<RealEstate>

    @Query("SELECT * FROM real_estate_db WHERE id = :realEstateId")
    fun getRealEstateFromID(realEstateId: Long): RealEstate

    @Query("SELECT * FROM real_estate_db")
     fun getRealEstates(): LiveData<List<RealEstate>>


    @Update
      fun updateRealEstate(realEstate: RealEstate?): Int

    @Delete
      fun deleteRealEstate(realEstate: RealEstate?): Int


    @Query("DELETE FROM real_estate_db WHERE id = :realEstateId ")
    fun deleteRealEstateWitID(realEstateId: Long): Int

    @Query("DELETE FROM real_estate_db")
      fun deleteAll(): Int

    //Content Provider
    @Query("SELECT * FROM real_estate_db")
    fun getRealEstatesWithCursor(): Cursor


    //Filter
    @Query("SELECT * FROM real_estate_db WHERE price >= :minimumPrice AND price <= :maximumPrice AND surface >= :minimumSurface AND numberOfPhotos >= :numberOfPhotos AND surface <= :maximumSurface  AND entryDate >= :minimumEntryDate AND dateOfSale >= :minimumSaleDate ")
     fun getRealEstatesFiltered(
        minimumPrice: Int?,
        maximumPrice: Int?, minimumSurface: Int?, maximumSurface: Int?, numberOfPhotos: Int?, minimumEntryDate: String?, minimumSaleDate: String?
    ): LiveData<List<RealEstate>>


}