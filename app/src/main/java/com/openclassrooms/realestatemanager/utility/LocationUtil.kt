package com.openclassrooms.realestatemanager.utility

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.openclassrooms.realestatemanager.models.RealEstate
import java.io.IOException


class LocationUtil {

companion object {
    fun getLocationFromAddress(context: Context, realEstate: RealEstate, strAddress: String) {
        val geocoder = Geocoder(context)
        val address: List<Address>?
        try {
            //Get latLng from tring
            address = geocoder.getFromLocationName(strAddress, 5)
            if (address != null) {

                // Take first possibility from the all possibilities.
                try {
                    val location: Address = address[0]
                    realEstate.latitude = location.latitude
                    realEstate.longitude = location.longitude
                } catch (e: IndexOutOfBoundsException) {
                }
            }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}
}