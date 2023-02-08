package com.openclassrooms.realestatemanager

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.utility.DummyProperty
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EstateDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var dao: RealEstateDao
    lateinit var database: RealEstateDatabase

    @Before
    fun setUp() {

        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                RealEstateDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.realEstateDao
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun insertEstate() {
        val estate = DummyProperty.getDummyProperty()
        dao.insertRealEstate(estate)
        val result = dao.getRealEstates().getOrAwaitValue()
        assertEquals(1,result.size)
    }

    @Test
    fun updateEstate()  {
        val estate = DummyProperty.getDummyProperty()

        val propertyToUpdate = estate.copy(surface = 1000)

        dao.insertRealEstate(estate)
        dao.updateRealEstate(propertyToUpdate)

        val updatedProperty = dao.getRealEstates().getOrAwaitValue().first()
        val result = dao.getRealEstates().getOrAwaitValue()

        assertEquals(1,result.size)
        assertEquals(updatedProperty.surface,1000)
    }

    @Test
    fun getPropertyResearch()  {

        val allProperties = DummyProperty.samplePropertyList
        dao.insertAll(allProperties)

        val isNearMinPrice = 250
        val isNearMaxPrice = 450
        val isNearMinSurface = 8
        val isNearMaxSurface = 10
        val isNumPhotos = 0
        val isMinDate = "24/08/2022"
        val isMaxDate = "29/11/2022"

        val fetchedProperties = dao.getRealEstatesFiltered(
                isNearMinPrice,
                isNearMaxPrice,
                isNearMinSurface,
                isNearMaxSurface,
                isNumPhotos,
                isMinDate,
                isMaxDate
        ).getOrAwaitValue()

        assertThat(fetchedProperties.size , `is`(greaterThanOrEqualTo(0)))
        assertThat(fetchedProperties[0].surface, `is`(greaterThanOrEqualTo(8)))
        assertThat(fetchedProperties[0].surface, `is`(lessThanOrEqualTo(10)))
        assertThat(fetchedProperties[0].price, `is`(lessThanOrEqualTo(450)))

        Log.d("getPropertyResearch", fetchedProperties.toString())


    }



    @Test
    fun deleteAllProperties()  {

        val property = DummyProperty.getDummyProperty()
        dao.insertRealEstate(property)

        val allProperties = dao.getRealEstates()
        dao.deleteAll()

        assert(allProperties.getOrAwaitValue().isEmpty())
    }

}