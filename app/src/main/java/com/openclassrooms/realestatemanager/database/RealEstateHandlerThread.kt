package com.openclassrooms.realestatemanager.database

import android.os.Handler
import android.os.HandlerThread
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.views.RealEstateViewModel


class RealEstateHandlerThread(name: String?) : HandlerThread(name) {

    fun startCreateRealEstateHandler(
        realEstate: RealEstate?,
        realEstateViewModel: RealEstateViewModel
    ) {
        if (!this.isAlive) start()
        val handler = Handler(this.looper)
        handler.post(Runnable { //Long work
            if (realEstate != null) {
                realEstateViewModel.addRealEstate(realEstate)
            }
        })
    }

    fun startUpdateRealEstateHandler(
        realEstate: RealEstate?,
        realEstateViewModel: RealEstateViewModel
    ) {
        if (!this.isAlive) start()
        val handler = Handler(this.looper)
        handler.post(Runnable { realEstateViewModel.updateRealEstate(realEstate!!) })
    }
}