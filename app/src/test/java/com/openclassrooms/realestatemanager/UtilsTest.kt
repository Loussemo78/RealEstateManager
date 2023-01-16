package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.utility.Utils
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class UtilsTest {

    @Before
    fun setUp() {
    }

    //method that Euros to Dollar"
    @Test
    fun convertEuroToDollar() {
        val value: Int = Utils.convertEuroToDollar(500)
        val expectedValue = 560
        assertEquals(expectedValue, value)
    }

    @Test
    fun convertDollarToEuro() {
        val value: Int = Utils.convertDollarToEuro(500)
        val expectedValue = 406
        assertEquals(expectedValue, value)
    }

   //method that  returns a value in the format "dd/MM/yyyy"
    @Test
    fun assertGetTodayDateWorks() {
        val today = Date()
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        assertEquals(dateFormat.format(today), Utils.getTodayDateRefactor())
    }

}