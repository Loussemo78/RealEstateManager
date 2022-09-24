package com.openclassrooms.realestatemanager.database

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.utility.DateConverter
import java.util.*
import kotlin.collections.ArrayList


class RealEstateQuery {
    companion object {
        fun generateQuery(
            minimumPrice: Int?, maximumPrice: Int?,
            minimumSurface: Int?, maximumSurface: Int?,
            firstLocation: String, numberOfPhotos: Int?,
            pointOfInterest: String, minimumEntryDate: Date?,
            minimumSaleDate: Date?
        ): SupportSQLiteQuery {

            // List of bind parameters
            val args: MutableList<Any> = ArrayList()

            // Beginning of query string
            val selectString = " SELECT * FROM RealEstate"
            var whereString = " WHERE "
            if (minimumPrice != null && minimumPrice >= 0) {
                args.add(minimumPrice)
                whereString += "price >= ? AND "
            }
            if (maximumPrice != null && minimumPrice!! >= 0) {
                whereString += "price <= ? AND "
                args.add(maximumPrice)
            }
            if (minimumSurface != null && minimumPrice!! >= 0) {
                whereString += "surface >= ? AND "
                args.add(minimumSurface)
            }
            if (maximumSurface != null && minimumPrice!! >= 0) {
                whereString += "surface <= ? AND "
                args.add(maximumSurface)
            }
            if (!firstLocation.isEmpty()) {
                whereString += "firstLocation = ? AND "
                args.add(firstLocation)
            }
            if (!pointOfInterest.isEmpty()) {
                whereString += "pointsOfInterest = ? AND "
                args.add(pointOfInterest)
            }
            if (minimumEntryDate != null) {
                whereString += "entryDate >= ? AND "
                DateConverter().fromDate(minimumEntryDate)?.let { args.add(it) }
            }
            if (minimumSaleDate != null) {
                whereString += "dateOfSale >= ? AND "
                DateConverter().fromDate(minimumSaleDate)?.let { args.add(it) }
            }
            if (numberOfPhotos != null) {
                whereString += "numberOfPhotos >= ? AND "
                args.add(numberOfPhotos)
            }

            //We have an AND at the end, we delete it
            val lastIndexAnd = whereString.lastIndexOf(" AND ")
            whereString = whereString.substring(0, lastIndexAnd)
            val queryString = selectString + whereString
            Log.d("SQL", queryString)
            return SimpleSQLiteQuery(queryString, args.toTypedArray())
        }
    }
}