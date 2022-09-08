package com.openclassrooms.realestatemanager.models

import android.net.Uri
import java.io.Serializable


data class RealEstatePhotos (
        val photoUri : String,
        val photoUrl : String,
        val description : String,
        ):Serializable{
                companion object{
                        fun uriToString(uri: Uri): String? {
                                return uri.toString()
                        }

                        fun stringToUri(stringUri: String?): Uri? {
                                return Uri.parse(stringUri)
                        }
                }
        }

