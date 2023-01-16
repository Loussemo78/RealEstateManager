package com.openclassrooms.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.dao.RealEstateDao
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.utility.DateConverter
import com.openclassrooms.realestatemanager.utility.RealEstateTypeConverter
import com.openclassrooms.realestatemanager.utility.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [RealEstate::class], version = 6 , exportSchema = false)
@TypeConverters(RealEstateTypeConverter::class, DateConverter::class)

abstract class RealEstateDatabase : RoomDatabase() {

    // --- DAO ---
    abstract val realEstateDao: RealEstateDao

    // --- SINGLETON ---
    companion object {
        @Volatile
        private var INSTANCE: RealEstateDatabase? = null



        // --- INSTANCE --

        fun getInstance(context: Context): RealEstateDatabase? {
            synchronized(this) {
                val scope = CoroutineScope(Dispatchers.IO)
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        RealEstateDatabase::class.java, "real_estate_db"
                    )
                        .addCallback(RealEstateDatabaseCallBack(scope))
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return INSTANCE
        }

        private class RealEstateDatabaseCallBack(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let {
                    scope.launch {
                        INSTANCE?.realEstateDao?.insertRealEstate(
                            RealEstate(
                                0, "Loft", 120000, "Manhattan",
                                8, 4, Utils.description(),
                                "https://www.notreloft.com/images/2016/10/loft-Manhattan-New-York-00500-800x533.jpg",
                                0, "", "", "41 Great Jones Street Penthouse\\n\" +\n" +
                                    "                                        \"Lafayette\\n\" +\n" +
                                    "                                        \"NoHo\\n\" +\n" +
                                    "                                        \"New York", "", null,40.72896 ,-73.99279 ,
                                "23/08/2022", "", ""
                            )
                        )
                    }
                }
            }
        }
    }


}