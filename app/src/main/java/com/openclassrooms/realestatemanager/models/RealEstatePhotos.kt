package com.openclassrooms.realestatemanager.models

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64.DEFAULT
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.Serializable
import java.util.*


data class RealEstatePhotos (
    var photoUri : String = "",
    var photoUrl : String = "",
    var description :String = "",
        ):Serializable{
                companion object{
                        fun uriToString(uri: Uri): String {
                                return uri.toString()
                        }
                        fun listUriToString(uris: ArrayList<Uri>): ArrayList<String> {
                                val uriStrings = ArrayList<String>()
                                for (uri in uris) {
                                        uriStrings.add(uri.toString())
                                }
                                return uriStrings
                        }


                        fun stringToUri(stringUri: String?): Uri? {
                                return Uri.parse(stringUri)
                        }

                        fun bitmapToImageUri(inContext: Context, inImage: Bitmap): Uri {
                                val bytes = ByteArrayOutputStream()
                                inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                                val path = MediaStore.Images.Media.insertImage(
                                        inContext.contentResolver, inImage,
                                        "Title", null
                                )
                                return Uri.parse(path)
                        }

                        fun bitmapToListUri(inContext: Context, inImages: ArrayList<Bitmap>): ArrayList<Uri> {
                                val uris = ArrayList<Uri>()
                                for (inImage in inImages) {
                                        val bytes = ByteArrayOutputStream()
                                        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                                        val path = MediaStore.Images.Media.insertImage(
                                                inContext.contentResolver, inImage,
                                                "Title", null
                                        )
                                        uris.add(Uri.parse(path))
                                }
                                return uris
                        }
                }
        }

