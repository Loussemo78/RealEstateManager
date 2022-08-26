package com.openclassrooms.realestatemanager.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.models.RealEstate
import java.util.ArrayList

class RealEstateRecyclerViewAdapter(val itemsList: ArrayList<RealEstate>): RecyclerView.Adapter<RealEstateRecyclerViewAdapter.RealEstateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RealEstateRecyclerViewAdapter.RealEstateViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RealEstateViewHolder, position: Int) {
        TODO("Not yet implemented")
    }



    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class RealEstateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }


}