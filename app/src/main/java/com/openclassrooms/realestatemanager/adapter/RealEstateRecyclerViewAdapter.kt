package com.openclassrooms.realestatemanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityAddOrCreateRealEstateBinding
import com.openclassrooms.realestatemanager.databinding.ActivityAddOrCreateRealEstateBinding.inflate
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstateItemsBinding
import com.openclassrooms.realestatemanager.models.RealEstate
import java.util.ArrayList

class RealEstateRecyclerViewAdapter(private val itemsList: List<RealEstate>): RecyclerView.Adapter<RealEstateRecyclerViewAdapter.RealEstateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RealEstateRecyclerViewAdapter.RealEstateViewHolder {
        val binding = FragmentRealEstateItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RealEstateViewHolder(binding,parent.context)
    }

    override fun onBindViewHolder(holder: RealEstateViewHolder, position: Int) {
        holder.bind(itemsList[position])
    }



    override fun getItemCount(): Int = itemsList.size

    inner class RealEstateViewHolder(private val binding: FragmentRealEstateItemsBinding, private val context:Context): RecyclerView.ViewHolder(binding.root){

                  fun bind(realEstate: RealEstate){
                     val url = "https://www.notreloft.com/images/2016/10/loft-Manhattan-New-York-00500-800x533.jpg"
                      val imageView = binding.fragmentRealEstateImageView
                      Glide.with(context)
                              .load(url)
                              .into(imageView)

                      binding.fragmentRealEstateItemType.text = realEstate.type
                      binding.fragmentRealEstateItemPlace.text = realEstate.place
                      binding.fragmentRealEstateItemPrice.text = realEstate.price.toString()

                  }
    }


}