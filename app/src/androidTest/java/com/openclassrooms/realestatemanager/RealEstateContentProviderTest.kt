package com.openclassrooms.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.provider.RealEstateContentProvider
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test


class RealEstateContentProviderTest {



   // FOR DATA
   private var mContentResolver: ContentResolver? = null

   // DATA SET FOR TEST

   // DATA SET FOR TEST
   private val USER_ID: Long = 1

   @Before
   fun setUp() {
      Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context,
              RealEstateDatabase::class.java)
              .allowMainThreadQueries()
              .build()
      mContentResolver = InstrumentationRegistry.getInstrumentation().context
              .contentResolver
   }

   @Test
   fun getItemsWhenNoItemInserted() {
      val cursor: Cursor? = mContentResolver!!.query(ContentUris.withAppendedId(RealEstateContentProvider.URI_REAL_ESTATE, USER_ID), null, null, null, null)
      assertThat(cursor, notNullValue())
      if (cursor != null) {
         assertThat(cursor.count, `is`(6))
      }
      cursor?.close()
   }

   @Test
   fun insertAndGetItem() {

      // BEFORE : Adding demo item
      val userUri: Uri? = mContentResolver!!.insert(RealEstateContentProvider.URI_REAL_ESTATE, generateItem())

      // TEST
      val cursor: Cursor? = mContentResolver!!.query(ContentUris.withAppendedId(RealEstateContentProvider.URI_REAL_ESTATE, USER_ID), null, null, null, null)
      assertThat(cursor, notNullValue())
      if (cursor != null) {
         assertThat(cursor.count, `is`(6))
      }
      if (cursor != null) {
         assertThat(cursor.moveToLast(), `is`(true))
      }
      if (cursor != null) {
         assertThat(cursor.getString(cursor.getColumnIndexOrThrow("type")), `is`("Loft"))
      }
   }

   // ---

   // ---
   private fun generateItem(): ContentValues? {
      val values = ContentValues()
      values.put("type", "Loft")
      values.put("place", "Queens")
      values.put("price", "50")
      values.put("id", "4")
      return values
   }

}