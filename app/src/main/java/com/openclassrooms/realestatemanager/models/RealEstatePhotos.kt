package com.openclassrooms.realestatemanager.models

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import java.io.Serializable


data class RealEstatePhotos (
    var photoUri : String = "",
    val photoUrl : String = "",
    var description :String = "",
        ):Serializable{
                companion object{
                        fun uriToString(uri: Uri): String {
                                return uri.toString()
                        }

                        fun stringToUri(stringUri: String?): Uri? {
                                return Uri.parse(stringUri)
                        }

                        fun bitmapToImageUri(inContext: Context, inImage: Bitmap): Uri {
                                val bytes = ByteArrayOutputStream()
                                inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                                val path = MediaStore.Images.Media.insertImage(
                                        inContext.getContentResolver(), inImage,
                                        "Title", null
                                )
                                return Uri.parse(path)
                        }
                }
        }

