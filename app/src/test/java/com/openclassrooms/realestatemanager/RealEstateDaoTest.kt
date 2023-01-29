package com.openclassrooms.realestatemanager


import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.utility.Utils
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class RealEstateDaoTest {

    @Mock
    private lateinit var realEstateDao: RealEstateDao




    private val testRealEstate = RealEstate(
            0, "Loft", 120000, "Manhattan",
            8, 4, Utils.description(),
            "https://www.notreloft.com/images/2016/10/loft-Manhattan-New-York-00500-800x533.jpg",
            0, "", "", "41 Great Jones Street Penthouse\\n\" +\n" +
            "                                        \"Lafayette\\n\" +\n" +
            "                                        \"NoHo\\n\" +\n" +
            "                                        \"New York", "", null, 40.72896, -73.99279,
            "23/08/2022", "", ""
    )

    @Before
    fun setUp() {

    }

    // Insert OK
    @Test
    fun insertRealEstate_success() {
        `when`(realEstateDao.insertRealEstate(testRealEstate)).thenReturn(1)
        val result = realEstateDao.insertRealEstate(testRealEstate)
        assertEquals(1, result)
    }



    //Update OK
    @Test
    fun getRealEstateFromID_success() {
        `when`(realEstateDao.getRealEstateFromID(0)).thenReturn(testRealEstate)
        val result = realEstateDao.getRealEstateFromID(0)
        assertEquals(testRealEstate, result)
    }



}
