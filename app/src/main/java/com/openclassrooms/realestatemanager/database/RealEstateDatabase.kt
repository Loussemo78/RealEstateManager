package com.openclassrooms.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.dao.RealEstateDao
import com.openclassrooms.realestatemanager.models.RealEstate
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities =  [RealEstate::class], version = 1)
abstract class RealEstateDatabase : RoomDatabase() {

    // --- DAO ---
    abstract val realEstateDao: RealEstateDao

    // --- SINGLETON ---
    companion object{
        @Volatile
        private var INSTANCE: RealEstateDatabase? = null

        // Executors
        private val NUMBER_OF_THREAD = 4
        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREAD)

        // --- INSTANCE --

          fun getInstance(context: Context): RealEstateDatabase? {
               synchronized(this){
                   if (INSTANCE == null) {
                       INSTANCE = Room.databaseBuilder(context.applicationContext,
                               RealEstateDatabase::class.java, "RealEstate")
                               //.addCallback()
                               .build()
                   }
               }

            return INSTANCE
        }
    }
}