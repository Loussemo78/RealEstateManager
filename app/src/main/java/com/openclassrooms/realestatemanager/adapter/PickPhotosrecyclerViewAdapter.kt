package com.openclassrooms.realestatemanager.adapter


import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.databinding.FragmentPickPhotosBinding
import com.openclassrooms.realestatemanager.models.RealEstatePhotos


class PickPhotosRecyclerViewAdapter(): RecyclerView.Adapter<PickPhotosRecyclerViewAdapter.PickPhotosViewHolder>(){


    private val realEstatePhotosList: ArrayList<RealEstatePhotos> = ArrayList()
    companion object{
        val map: HashMap<Int, String> = HashMap()
    }



    fun setRealEstatePhotosList(realEstatePhotosList: ArrayList<RealEstatePhotos>) {
        this.realEstatePhotosList.clear()
        this.realEstatePhotosList.addAll(realEstatePhotosList)
        notifyDataSetChanged()
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickPhotosViewHolder {
        return  PickPhotosViewHolder(FragmentPickPhotosBinding.inflate(
                LayoutInflater
                        .from(parent.context), parent, false));
    }

    override fun onBindViewHolder(holder: PickPhotosViewHolder, @SuppressLint("RecyclerView") position:Int) {
        val (photoUri, photoUrl, description) = realEstatePhotosList[position]

        if (photoUri != null) {
            holder.fragmentPickPhotosBinding!!.fragmentPickPhotosImageView.setImageURI(
                    RealEstatePhotos.stringToUri(photoUri)
            )
        } else if (photoUrl != null) {
            Glide.with(holder.fragmentPickPhotosBinding!!.fragmentPickPhotosImageView.context)
                    .load(photoUrl)
                    .into(holder.fragmentPickPhotosBinding!!.fragmentPickPhotosImageView)
            holder.fragmentPickPhotosBinding!!.fragmentRealEstatePhotosDescription.setText(
                    description
            )
        }

        holder.fragmentPickPhotosBinding!!.fragmentRealEstatePhotosDescription.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(
                            charSequence: CharSequence,
                            i: Int,
                            i1: Int,
                            i2: Int
                    ) {
                    }

                    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun afterTextChanged(editable: Editable) {
                        val value = map[position]
                        if (value == null) {
                            map[position] =
                                    holder.fragmentPickPhotosBinding!!.fragmentRealEstatePhotosDescription.text.toString()
                        }
                    }
                }
        )
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }



    inner class PickPhotosViewHolder(private val binding: FragmentPickPhotosBinding) : RecyclerView.ViewHolder(binding.root) {
        var fragmentPickPhotosBinding: FragmentPickPhotosBinding = binding

    }


}


