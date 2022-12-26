package com.openclassrooms.realestatemanager

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.utility.LiveDataTestUtil.getValue
import com.openclassrooms.realestatemanager.utility.Utils
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class RealEstateDaoTest {

     lateinit var database: RealEstateDatabase


    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun initDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context,
                RealEstateDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }


    /*private val realEstateTest = RealEstate(1,"Type",12 ,"Place",
            1500,2,3,1,"Description"
            ,"https://www.notreloft.com/images/2016/10/loft-Manhattan-New-York-00500-800x533.jpg",3,"Address","mainPhoto",
            "points","", null,"",-73.97619
            ,40.75691,"video")*/


    private val realEstateTest = RealEstate(
            0, "Loft", 120000, "Manhattan",
            8, 4, 4, 4, Utils.description(),
            "https://www.notreloft.com/images/2016/10/loft-Manhattan-New-York-00500-800x533.jpg",
            0, "", "", "41 Great Jones Street Penthouse\\n\" +\n" +
            "                                        \"Lafayette\\n\" +\n" +
            "                                        \"NoHo\\n\" +\n" +
            "                                        \"New York", "", null,
            "",40.72896 ,-73.99279 ,
            "23/08/2022", "", ""
    )


    //getRealEstate

    @Test
   @Throws(InterruptedException::class)
    fun getRealEstatesWhenNoRealEstateInserted() {
        val realEstates: List<RealEstate> = getValue(database.realEstateDao
                .getRealEstates())
        val realEstate: RealEstate = getValue(database.realEstateDao.getRealEstate(1))
        assertTrue(realEstates.isEmpty())
        assertNull(realEstate)
    }


    //add or create real estate
    @Test
    @Throws(InterruptedException::class)
    suspend fun insertAndGetRealEstate() {
        this.database.realEstateDao.insertRealEstate(realEstateTest)
        val realEstate: RealEstate = getValue(database.realEstateDao
                .getRealEstate(1))
        val realEstates: List<RealEstate> = getValue(database.realEstateDao
                .getRealEstates())
        assertNotNull(realEstate)
        assertEquals(1, realEstate.id)
        assertEquals(1, realEstates.size)
    }

    @After
    @Throws(java.lang.Exception::class)
    fun closeDb() {
        database.close()
    }

}