package com.openclassrooms.realestatemanager.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.MainActivity
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstateItemsBinding
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.views.RealEstateDetailFragment
import com.openclassrooms.realestatemanager.views.RealEstateFragment


class RealEstateRecyclerViewAdapter(private val itemsList: List<RealEstate>) : RecyclerView.Adapter<RealEstateRecyclerViewAdapter.RealEstateViewHolder>() {


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
            val url = "https://www.notreloft.com/images/2016/10/loft-Manhattan-New-York-00500-800x533.jpg"
            val imageView = binding.fragmentRealEstateImageView
            Glide.with(context)
                    .load(url)
                    .into(imageView)

            binding.fragmentRealEstateItemType.text = realEstate.type
            binding.fragmentRealEstateItemPlace.text = realEstate.place
            binding.fragmentRealEstateItemPrice.text = realEstate.price.toString()

            binding.fragmentRealEstateImageView.setOnClickListener {
                // val fm = (context as MainActivity).supportFragmentManager
                val fragment = RealEstateDetailFragment()
                val parentFragmentManager = fragment.parentFragmentManager
                val fragmentDetail = RealEstateDetailFragment()
                val bundle = Bundle()
                bundle.putInt(RealEstateFragment.KEY, realEstate.id.toInt())
                fragmentDetail.arguments = bundle

                val fragmentContainerViewDetail = parentFragmentManager.findFragmentById(
                        R.id.activity_main_fragment_container_view_detail)


                if (fragmentContainerViewDetail == null) {
                    parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.activity_main_fragment_container_view_list,
                                    RealEstateDetailFragment())
                            .addToBackStack(RealEstateDetailFragment::class.java.simpleName)
                            .commit()
                } else if (fragmentContainerViewDetail.isVisible) { //on tablet
                    parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.activity_main_fragment_container_view_detail,
                                    RealEstateDetailFragment())
                            .commit()
                }


//                fm.beginTransaction()
//                        .replace(R.id.activity_main_fragment_container_view_detail,fragmentDetail)
//                        .addToBackStack(null)
//                        .commit()

            }


        }
    }

}