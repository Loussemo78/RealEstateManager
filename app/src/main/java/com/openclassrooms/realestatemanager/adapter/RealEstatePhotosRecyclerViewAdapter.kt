package com.openclassrooms.realestatemanager.adapter

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstatePhotosBinding
import com.openclassrooms.realestatemanager.models.RealEstatePhotos


class RealEstatePhotosRecyclerViewAdapter :
    RecyclerView.Adapter<RealEstatePhotosRecyclerViewAdapter.ViewHolder>() {
    private val realEstatePhotosList: MutableList<RealEstatePhotos> = ArrayList()

    //method to update the items of adapter
    fun setRealEstatePhotosList(realEstatePhotosList: List<RealEstatePhotos>?) {
        this.realEstatePhotosList.clear()
        this.realEstatePhotosList.addAll(realEstatePhotosList!!)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentRealEstatePhotosBinding.inflate(
                LayoutInflater
                    .from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (photoUri, photoUrl, description) = realEstatePhotosList[position]
        if (photoUrl != null) {
            holder.fragmentRealEstatePhotosBinding.fragmentRealEstatePhotosImageView?.let {
                Glide.with(it.context)
                    .load(photoUrl)
                    .into(holder.fragmentRealEstatePhotosBinding.fragmentRealEstatePhotosImageView!!)
            }
        }
        if (photoUri != null) {
            val imageUri: Uri? = RealEstatePhotos.stringToUri(photoUri)
            holder.fragmentRealEstatePhotosBinding.fragmentRealEstatePhotosImageView?.setImageURI(
                imageUri
            )
        }
        holder.fragmentRealEstatePhotosBinding.fragmentRealEstatePhotosDescription?.setText(description
        )
    }

    override fun getItemCount(): Int {
        return realEstatePhotosList.size
    }

    inner class ViewHolder(fragmentRealEstatePhotosBinding: FragmentRealEstatePhotosBinding) :
        RecyclerView.ViewHolder(fragmentRealEstatePhotosBinding.root) {
         var fragmentRealEstatePhotosBinding: FragmentRealEstatePhotosBinding

        init {
            this.fragmentRealEstatePhotosBinding = fragmentRealEstatePhotosBinding
        }
    }
}