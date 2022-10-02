package com.openclassrooms.realestatemanager.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.SearchRealEstateFragment
import com.openclassrooms.realestatemanager.SearchRealEstateFragment.SearchCritaries
import com.openclassrooms.realestatemanager.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.database.RealEstateQuery
import com.openclassrooms.realestatemanager.models.RealEstate


class RealEstateRepository( val context: Context) {

    private val dao : RealEstateDao = RealEstateDatabase.getInstance(context)?.realEstateDao!!
    private var realEstates: LiveData<List<RealEstate>>
    private val filter = MutableLiveData<SearchRealEstateFragment.SearchCritaries?>(null)

     init {
        realEstates = Transformations.switchMap(filter
        ) { filter: SearchCritaries? ->
            if (filter == null) {
                return@switchMap dao.getRealEstates()
            } else {
                return@switchMap filter.firstLocation?.let {
                    filter.pointOfInterest?.let { it1 ->
                        filter.description?.let { it2 ->
                            RealEstateQuery.generateQuery(
                                filter.minimumPrice,
                                filter.maximumPrice,
                                filter.minimumSurface,
                                filter.maximumSurface,
                                it,
                                filter.numberOfPhotos,
                                it2,

                                it1,
                                filter.entryDateInDate,
                                filter.saleDateInDate
                            )
                        }
                    }
                }?.let {
                    dao.getRealEstatesFiltered(
                        it
                    )
                }
            }
        }
    }

    fun resetFilter() {
        filter.value = null
    }

    fun setFilter(filter: SearchCritaries) {
        this.filter.value = filter
    }


    suspend fun insertRealEstate(realEstate: RealEstate) =
            dao.insertRealEstate(realEstate)

    suspend fun updateRealEstate(realEstate: RealEstate) =
            dao.updateRealEstate(realEstate)

    suspend fun deleteRealEstate(realEstate: RealEstate) =
            dao.deleteRealEstate(realEstate)

    suspend fun deleteAll() =
            dao.deleteAll()


    fun getAllRealEstates():LiveData<List<RealEstate>> =
            dao.getRealEstates()

    fun getRealEstate(id: Long): LiveData<RealEstate> =
            dao.getRealEstate(id)

    fun getRealEstatesFiltered(query: SupportSQLiteQuery): LiveData<List<RealEstate>>{
       return dao.getRealEstatesFiltered(query)
    }

//    fun gesEstatesBySearch(query: SimpleSQLiteQuery) : LiveData<List<RealEstate>>{
//        return dao.getRealEstatesFiltered(query)
//    }



}