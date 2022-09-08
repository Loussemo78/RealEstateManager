package com.openclassrooms.realestatemanager.models

import android.content.ContentValues
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Entity (tableName = "real_estate_db")
data class RealEstate(
    @PrimaryKey(autoGenerate = true)
        val id: Long,
    var type: String,
    var price: Int,
    var place: String,
    var surface: Int,
    var numberOfRooms: Int,
    var numberOfBathRooms: Int,
    var numberOfBedRooms: Int,
    var description: String,
    val mainPhotoUrl: String,
    val numberOfPhotos:Int?,
    val address:String,
    var mainPhotoString: String?,
    var pointsOfInterest: String,
    val latitude:Double,
    val longitude:Double,
    var status:String,
    var entryDate:Date,
    var dateOfSale:Date?,
    var agent:String,
    var agentPhotoUrl:String,
    var video:String
        ):Serializable{





    fun abstractLayer(values: ContentValues): RealEstate {
        val realEstate = RealEstate(id, type, price, place,surface, numberOfRooms, numberOfBathRooms, numberOfBedRooms,
                description, mainPhotoUrl, numberOfPhotos, address, mainPhotoString, pointsOfInterest, latitude,
                longitude, status, entryDate, dateOfSale, agent, agentPhotoUrl, video
        )
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        values.put("id",realEstate.id)
        values.put("type", realEstate.type)
        values.put("price", realEstate.price)
        values.put("place", realEstate.place)
        values.put("surface", realEstate.surface)
        values.put("numberOfRooms", realEstate.numberOfRooms)
        values.put("numberOfBathrooms", realEstate.numberOfBathRooms)
        values.put("numberOfBedrooms", realEstate.numberOfBedRooms)
        values.put("description", realEstate.description)
        values.put("mainPhotoUrl", realEstate.mainPhotoUrl)
        values.put("mainPhotoString", realEstate.mainPhotoString)
        values.put("numberOfPhotos", realEstate.numberOfPhotos)
        values.put("latitude", realEstate.latitude)
        values.put("longitude", realEstate.longitude)
        values.put("status", realEstate.status)
        values.put("entryDate", realEstate.entryDate.let { dateFormat.format(it) })
        values.put("dateOfSale", realEstate.dateOfSale?.let { dateFormat.format(it) })
        values.put("agent", realEstate.agent)
        values.put("agentPhotoUrl", realEstate.agentPhotoUrl)
        values.put("videoId", realEstate.video)

        return realEstate
    }

    companion object{
        fun abstractLayer():Int = 1
    }
}



