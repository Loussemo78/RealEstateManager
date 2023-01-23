package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.models.RealEstate


class RealEstateContentProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(uri: Uri,  projection: Array<String>?,  selection: String?,
                        selectionArgs: Array<String>?,  sortOrder: String?): Cursor {
        if (context != null) {
            val cursor: Cursor = RealEstateDatabase.getInstance(context!!)?.realEstateDao!!.getRealEstatesWithCursor()
            cursor.setNotificationUri(context!!.contentResolver, uri)
            return cursor
        }
        throw IllegalArgumentException("Failed to query row for uri$uri")
    }

    override fun getType(uri: Uri): String {
        return "vnd.android.cursor.real.estate/" + AUTHORITY + "." + TABLE_NAME
    }


    override fun insert(uri: Uri,  contentValues: ContentValues?):Uri {
            if (context != null) {
                val id: Long = RealEstateDatabase
                        .getInstance(context!!)
                        ?.realEstateDao!!
                        .insertRealEstate(RealEstate.fromContentValues(contentValues!!))

                if (id != 0L) {
                    context!!.contentResolver.notifyChange(uri, null)
                   return ContentUris.withAppendedId(uri, id)
                }
            }
        throw java.lang.IllegalArgumentException("Failed to insert row into$uri")
    }


    override fun delete(uri: Uri,  s: String?,  strings: Array<String>?): Int {
        if (context != null) {
            val count: Int = RealEstateDatabase.getInstance(context!!)?.realEstateDao!!
                    .deleteRealEstateWitID(ContentUris.parseId(uri))
            context!!.contentResolver.notifyChange(uri, null)
            return count
        }
        throw IllegalArgumentException("Failed to delete row into $uri")
    }

    override fun update(uri: Uri,  contentValues: ContentValues?,  s: String?,
                         strings: Array<String>?): Int {
        if (context != null) {
            val count: Int = RealEstateDatabase.getInstance(context!!)?.realEstateDao!!
                    .updateRealEstate( RealEstate.fromContentValues(contentValues!!))
            context!!.contentResolver.notifyChange(uri, null)
            return count
        }
        throw IllegalArgumentException("Failed to update row into $uri")
    }

    companion object {
        // FOR DATA
        const val AUTHORITY = "com.openclassrooms.realestatemanager.provider"
        val TABLE_NAME = RealEstate::class.java.simpleName
        val URI_REAL_ESTATE: Uri = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
    }
}