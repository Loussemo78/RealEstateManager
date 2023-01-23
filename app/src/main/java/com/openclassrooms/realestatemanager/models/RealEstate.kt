package com.openclassrooms.realestatemanager.models

import android.content.ClipData.Item
import android.content.ContentValues
import androidx.room.*
import java.io.Serializable


@Entity(tableName = "real_estate_db")
data class RealEstate(

        @PrimaryKey(autoGenerate = true )
        var id: Long = 0,
        var type: String = "",
        var price: Int = 0,
        var place: String = "",
        var surface: Int = 0,
        var numberOfRooms: Int = 0,
        var description: String = "",
        var mainPhotoUrl: String = "",
        var numberOfPhotos: Int? = 0,
        var firstLocation:String = "",
        var secondLocation:String = "",
        val address: String = "",
        var mainPhotoString: String? = "",
        @TypeConverters
        var listPhotos: ArrayList<RealEstatePhotos>? = ArrayList(),
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var status: String = "",
        var entryDate: String = "",
        var dateOfSale: String = "",
        var agent: String = "",
        var agentPhotoUrl: String = "",
        var video: String = ""
) : Serializable {


    fun abstractLayer(values: ContentValues): RealEstate {
        val realEstate = RealEstate(
            id,
            type,
            price,
            place,
            surface,
            numberOfRooms,
            description,
            mainPhotoUrl,
            numberOfPhotos,
            address,
            firstLocation,
            secondLocation,
            mainPhotoString,
            listPhotos,
            latitude,
            longitude,
            status,
            entryDate,
            dateOfSale,
            agent,
            agentPhotoUrl,
            video
        )

        values.put("id", realEstate.id)
        values.put("type", realEstate.type)
        values.put("price", realEstate.price)
        values.put("place", realEstate.place)
        values.put("surface", realEstate.surface)
        values.put("numberOfRooms", realEstate.numberOfRooms)
        values.put("description", realEstate.description)
        values.put("mainPhotoUrl", realEstate.mainPhotoUrl)
        values.put("mainPhotoString", realEstate.mainPhotoString)
        values.put("numberOfPhotos", realEstate.numberOfPhotos)
        values.put("firstLocation", realEstate.firstLocation)
        values.put("secondLocation", realEstate.secondLocation)
        values.put("latitude", realEstate.latitude)
        values.put("longitude", realEstate.longitude)
        values.put("status", realEstate.status)
        values.put("entryDate", realEstate.entryDate)
        values.put("dateOfSale", realEstate.dateOfSale)
        values.put("agent", realEstate.agent)
        values.put("agentPhotoUrl", realEstate.agentPhotoUrl)
        values.put("videoId", realEstate.video)

        return realEstate
    }

    companion object {
        fun abstractLayer(): Int = 1

        fun fromContentValues(values: ContentValues): RealEstate {
            val item: RealEstate = RealEstate()
            if (values.containsKey("id")) item.id = (values.getAsInteger("id").toLong())
            if (values.containsKey("price")) item.price = values.getAsInteger("price")
            if (values.containsKey("type")) item.type = values.getAsString("type")
            if (values.containsKey("place")) item.place = values.getAsString("place")
            if (values.containsKey("surface")) item.surface = values.getAsInteger("surface")
            if (values.containsKey("numberOfRooms")) item.numberOfRooms = values.getAsInteger("numberOfRooms")
            if (values.containsKey("description")) item.description = values.getAsString("description")
            if (values.containsKey("mainPhotoUrl")) item.mainPhotoUrl = values.getAsString("numberOfRooms")
            if (values.containsKey("mainPhotoString")) item.mainPhotoString = values.getAsString("mainPhotoString")
            if (values.containsKey("numberOfPhotos")) item.numberOfPhotos = values.getAsInteger("numberOfPhotos")
            if (values.containsKey("firstLocation")) item.firstLocation = values.getAsString("firstLocation")
            if (values.containsKey("secondLocation")) item.secondLocation = values.getAsString("secondLocation")
            if (values.containsKey("latitude")) item.latitude = values.getAsDouble("latitude")
            if (values.containsKey("longitude")) item.longitude = values.getAsDouble("longitude")
            if (values.containsKey("status")) item.status = values.getAsString("status")
            if (values.containsKey("entryDate")) item.entryDate = values.getAsString("entryDate")
            if (values.containsKey("dateOfSale")) item.dateOfSale = values.getAsString("dateOfSale")
            if (values.containsKey("agent")) item.agent = values.getAsString("agent")
            if (values.containsKey("agentPhotoUrl")) item.agentPhotoUrl = values.getAsString("agentPhotoUrl")
            if (values.containsKey("videoId")) item.video = values.getAsString("videoId")

            // continuer les attributs de la calsse RealEstate ici

            return item
        }
    }



    // --- UTILS ---

}



