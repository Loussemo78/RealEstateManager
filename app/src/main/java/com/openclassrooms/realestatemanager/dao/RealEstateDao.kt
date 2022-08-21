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


    @Query("SELECT * FROM RealEstate WHERE id = :realEstateId")
    fun getRealEstate(realEstateId: Long): LiveData<RealEstate?>?

    @Query("SELECT * FROM RealEstate")
    fun getRealEstates(): LiveData<List<RealEstate?>?>?

    @Update
    fun updateRealEstate(realEstate: RealEstate?): Int

    @Query("DELETE FROM RealEstate")
    fun deleteAll(): Int

    //Content Provider
    @Query("SELECT * FROM RealEstate")
    fun getRealEstatesWithCursor(): Cursor?

    //Filter
    @RawQuery(observedEntities = [RealEstate::class])
    fun getRealEstatesFiltered(query: SupportSQLiteQuery?): LiveData<List<RealEstate?>?>?

}