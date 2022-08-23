package com.openclassrooms.realestatemanager.utility

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.models.RealEstatePhotos
import java.lang.reflect.Type


class RealEstateTypeConverter {

    @TypeConverter
    fun fromValuesToList(value: ArrayList<RealEstatePhotos?>?): String? {
        if (value == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<RealEstatePhotos?>?>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toOptionValuesList(value: String?): ArrayList<RealEstatePhotos?>? {
        if (value == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<RealEstatePhotos?>?>() {}.type
        return gson.fromJson(value, type)
    }
}