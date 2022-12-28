package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.openclassrooms.realestatemanager.dao.RealEstateDao
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.utility.LiveDataTestUtil
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class SearchRealEstateDaoTest {

    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testGetRealEstatesFiltered() {
        // Créez une instance de votre classe de DAO et utilisez Mockito pour simuler la réponse de la base de données
        val dao = Mockito.mock(RealEstateDao::class.java)
        Mockito.`when`(dao.getRealEstatesFiltered(
                minimumPrice = 100,
                maximumPrice = 200,
                minimumSurface = 50,
                maximumSurface = 100,
                numberOfPhotos = 2,
                minimumEntryDate = "2022-01-01",
                minimumSaleDate = "2022-01-01"
    ))//.thenReturn(listOf(RealEstate()))

        // Appelez la fonction à tester avec les paramètres souhaités
        val result = dao.getRealEstatesFiltered(
                minimumPrice = 100,
                maximumPrice = 200,
                minimumSurface = 50,
                maximumSurface = 100,
                numberOfPhotos = 2,
                minimumEntryDate = "2022-01-01",
                minimumSaleDate = "2022-01-01"
        )

        // Vérifiez que le résultat retourné est ce que vous attendiez
        //assertEquals(listOf(RealEstate(...)), result)
    }
}
