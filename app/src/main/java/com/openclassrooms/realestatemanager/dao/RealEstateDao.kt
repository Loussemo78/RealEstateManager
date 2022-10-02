package com.openclassrooms.realestatemanager.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
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
    @RawQuery(observedEntities = [RealEstate::class])
     fun getRealEstatesFiltered(query: SupportSQLiteQuery): LiveData<List<RealEstate>>


}