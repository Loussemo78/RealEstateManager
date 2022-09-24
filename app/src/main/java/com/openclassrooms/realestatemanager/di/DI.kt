package com.openclassrooms.realestatemanager.di

import android.app.Application
import com.openclassrooms.realestatemanager.repository.RealEstateRepository

class DI {

companion object{
    private  var repository : RealEstateRepository ? = null


    fun getRepository(application : Application) : RealEstateRepository {
        if (repository == null)
            repository = RealEstateRepository(application);

        return repository as RealEstateRepository
    }
}
}
