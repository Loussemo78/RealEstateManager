package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.models.RealEstate
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import java.util.*

@RunWith(AndroidJUnit4::class)
class RealEstateViewModelTest {

    lateinit var dao: RealEstateDao
    lateinit var database: RealEstateDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {

        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                RealEstateDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.realEstateDao
    }

    @Test
    fun insertRealEstate_exceptedSingle(){
        val realEstate = RealEstate(1,"Type",12 ,"Place",
                1500,2,"Description"
                ,"https://www.notreloft.com/images/2016/10/loft-Manhattan-New-York-00500-800x533.jpg",3,"Address","mainPhoto",
                "points","", null,-73.97619
                ,40.75691,"video")
        dao.insertRealEstate(realEstate)
        val result = dao.getRealEstates().getOrAwaitValue()
        Assert.assertEquals(1, result.size)
    }



    @After
    fun tearDown() {
        database.close()
    }



}
