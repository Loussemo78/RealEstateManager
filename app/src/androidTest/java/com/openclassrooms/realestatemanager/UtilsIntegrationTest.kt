package com.openclassrooms.realestatemanager

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.utility.Utils
import junit.framework.Assert.assertTrue
import org.junit.Test

class UtilsIntegrationTest {

    @Test
    fun assertIsInternetAvailableWorks() {
        // Context of the app under test.
        val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val isInternetAvailable: Boolean = Utils.isInternetAvailableSecond(appContext)
        assertTrue(isInternetAvailable)
    }
}