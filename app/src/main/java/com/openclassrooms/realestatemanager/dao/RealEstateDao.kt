package com.openclassrooms.realestatemanager.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.models.RealEstate

@Dao
interface RealEstateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRealEstate(realEstate: RealEstate?): Long


    @Query("SELECT * FROM real_estate_db WHERE id = :realEstateId")
    suspend fun getRealEstate(realEstateId: Long): LiveData<RealEstate?>?

    @Query("SELECT * FROM real_estate_db")
    suspend fun getRealEstates(): LiveData<List<RealEstate?>?>?

    @Update
    suspend fun updateRealEstate(realEstate: RealEstate?): Int

    @Query("DELETE FROM real_estate_db")
    suspend fun deleteAll(): Int

    //Content Provider
    @Query("SELECT * FROM real_estate_db")
    suspend fun getRealEstatesWithCursor(): Cursor?

    //Filter
    @RawQuery(observedEntities = [RealEstate::class])
    suspend fun getRealEstatesFiltered(query: SupportSQLiteQuery?): LiveData<List<RealEstate?>?>?

}