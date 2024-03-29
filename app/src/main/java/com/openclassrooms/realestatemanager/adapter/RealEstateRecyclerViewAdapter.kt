package com.openclassrooms.realestatemanager.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.AddOrCreateRealEstateFragment
import com.openclassrooms.realestatemanager.MainActivity
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstateItemsBinding
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.views.RealEstateDetailFragment
import com.openclassrooms.realestatemanager.views.RealEstateFragment
import com.openclassrooms.realestatemanager.views.RealEstateFragment.Companion.EDIT_REAL_ESTATE


class RealEstateRecyclerViewAdapter(private val context: Context ,private val itemsList: List<RealEstate>,private val onRealEstateClickListener: OnRealEstateClickListener?) : RecyclerView.Adapter<RealEstateRecyclerViewAdapter.RealEstateViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RealEstateRecyclerViewAdapter.RealEstateViewHolder {
        val binding = FragmentRealEstateItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RealEstateViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: RealEstateViewHolder, position: Int) {
        holder.bind(itemsList[position])
    }


    override fun getItemCount(): Int = itemsList.size

    inner class RealEstateViewHolder(private val binding: FragmentRealEstateItemsBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bind(realEstate: RealEstate) {
             val imageView = binding.fragmentRealEstateImageView
            Glide.with(context)
                    .load(realEstate.mainPhotoUrl)
                    .into(imageView)

            binding.fragmentRealEstateItemType.text = realEstate.type
            binding.fragmentRealEstateItemPlace.text = realEstate.place
            binding.fragmentRealEstateItemPrice.text = "$" +realEstate.price.toString()
            val activity = context as MainActivity



            binding.fragmentRealEstateImageView.setOnClickListener {
                onRealEstateClickListener?.onRealEstateClicked(realEstate)


            }
             binding.fragmentRealEstateEditButton.setOnClickListener {


                 val tabletSize = activity.resources.getBoolean(com.openclassrooms.realestatemanager.R.bool.isTablet)

                 val fragmentEdit = AddOrCreateRealEstateFragment()
                 val bundle = Bundle()

                 bundle.putLong("id",realEstate.id)
                 fragmentEdit.arguments = bundle

                 if (!tabletSize) {

                     activity.supportFragmentManager
                         .beginTransaction()
                         .replace(R.id.activity_main_fragment_container_view_list,
                             fragmentEdit)
                         .addToBackStack(AddOrCreateRealEstateFragment::class.java.simpleName)
                         .commit()


                 } else {
                     activity?.supportFragmentManager
                         ?.beginTransaction()
                         ?.replace(com.openclassrooms.realestatemanager.R.id.frame_layout_detail, fragmentEdit)
                         ?.commit()

                 }


             }


        }
    }
    interface OnRealEstateClickListener {
        fun onRealEstateClicked(realEstate: RealEstate)
    }


}