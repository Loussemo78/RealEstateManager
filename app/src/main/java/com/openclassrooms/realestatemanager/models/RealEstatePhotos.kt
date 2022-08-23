package com.openclassrooms.realestatemanager.models

import java.io.Serializable

data class RealEstatePhotos (
        val photoUri : String,
        val photoUrl : String,
        val description : String,
        ):Serializable
