package com.openclassrooms.realestatemanager.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.openclassrooms.realestatemanager.R

class ImagesAdapter(private val itemsList: ArrayList<Uri>, private var context: FragmentActivity?) : BaseAdapter() {


    override fun getCount(): Int {
      return  itemsList.size
    }

    override fun getItem(p0: Int): Any {
      return  itemsList[p0]
    }

    override fun getItemId(p0: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getView(position: Int, convertView: View?, parent:ViewGroup?):
            View? {
        var convertView: View? = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                .inflate(R.layout.item, parent, false)
        }


        val layout = convertView?.findViewById<ImageView>(R.id.img_item)

        layout?.setImageURI(itemsList[position])

        return convertView
    }
}