package com.openclassrooms.realestatemanager.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.models.RealEstate

@Dao
interface RealEstateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRealEstate(realEstate: RealEstate?): Long


    @Query("SELECT * FROM real_estate_db WHERE id = :realEstateId")
     fun getRealEstate(realEstateId: Long): LiveData<RealEstate>

    @Query("SELECT * FROM real_estate_db")
     fun getRealEstates(): LiveData<List<RealEstate>>

    @Update
    suspend fun updateRealEstate(realEstate: RealEstate?): Int

    @Delete
    suspend fun deleteRealEstate(realEstate: RealEstate?): Int

    @Query("DELETE FROM real_estate_db")
    suspend fun deleteAll(): Int

    //Content Provider
//    @Query("SELECT * FROM real_estate_db")
//    suspend fun getRealEstatesWithCursor(): Cursor?

    //Filter
    @Query("SELECT * FROM real_estate_db WHERE price >= :minimumPrice AND price <= :maximumPrice AND surface >= :minimumSurface AND surface <= :maximumSurface  AND entryDate >= :minimumEntryDate AND dateOfSale >= :minimumSaleDate AND numberOfPhotos >= :numberOfPhotos")
     fun getRealEstatesFiltered(
        minimumPrice: Int?,
        maximumPrice: Int?, minimumSurface: Int?, maximumSurface: Int?, numberOfPhotos: Int?, minimumEntryDate: String?, minimumSaleDate: String?
    ): LiveData<List<RealEstate>>


}