package com.openclassrooms.realestatemanager.models

import android.content.ContentValues
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class RealEstate(
        @PrimaryKey val id: Int,
        val type: String,
        val price: Int,
        val surface: Int,
        val numberOfRooms: Int,
        val numberOfBathRooms: Int,
        val numberOfBedRooms: Int,
        val description: String,
        val mainPhotoUrl: String,
        val numberOfPhotos:Int,
        val address:String,
        val mainPhotoString: String,
        val pointsOfInterest: String,
        val latitude:Double,
        val longitude:Double,
         val status:String,
        val entryDate:Date,
        val dateOfSale:Date,
        val realtor:String,
        val agentPhotoUrl:String,
        val video:String
        ){
    fun abstractLayer(values: ContentValues): RealEstate {
        val realEstate = RealEstate(id, type, price, surface, numberOfRooms, numberOfBathRooms, numberOfBedRooms,
                description, mainPhotoUrl, numberOfPhotos, address, mainPhotoString, pointsOfInterest, latitude,
                longitude, status, entryDate, dateOfSale, realtor, agentPhotoUrl, video
        )

        values.put("type", realEstate.type)
        values.put("price", realEstate.price)
        values.put("surface", realEstate.surface)
        values.put("numberOfRooms", realEstate.numberOfRooms)
        values.put("numberOfBathrooms", realEstate.price)
        values.put("numberOfBedrooms", realEstate.price)
        values.put("description", realEstate.price)
        values.put("mainPhotoUrl", realEstate.price)
        values.put("mainPhotoString", realEstate.price)
        values.put("numberOfPhotos", realEstate.price)
        values.put("firstLocation", realEstate.price)
        values.put("latitude", realEstate.price)
        values.put("longitude", realEstate.price)
        values.put("status", realEstate.price)
        values.put("entryDate", realEstate.price)
        values.put("dateOfSale", realEstate.price)
        values.put("agent", realEstate.price)
        values.put("agentPhotoUrl", realEstate.price)
        values.put("videoId", realEstate.price)

        return realEstate
    }
}



