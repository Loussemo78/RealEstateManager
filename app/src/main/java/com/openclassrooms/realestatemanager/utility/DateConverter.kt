package com.openclassrooms.realestatemanager.utility

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return if (date == null) null else date.time
    }

    companion object {

        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    }
}