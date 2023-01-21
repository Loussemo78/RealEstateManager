package com.openclassrooms.realestatemanager.views

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.ImagesAdapter
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstateDetailBinding
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstatePhotos
import java.util.*


class RealEstateDetailFragment:Fragment() {

private lateinit var binding:FragmentRealEstateDetailBinding
private lateinit var othersPhotosList: ArrayList<RealEstatePhotos>

private lateinit var realEstate:RealEstate
private var realEstateId:Int = 0
    private lateinit var  map: GoogleMap


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentRealEstateDetailBinding.inflate(inflater,container,false)
        othersPhotosList = ArrayList<RealEstatePhotos>()

        if (arguments?.getSerializable(RealEstateFragment.KEY) != null) {
            realEstateId = requireArguments().getInt(RealEstateFragment.KEY)
        } else if (arguments?.getSerializable(MapFragment.MAPS_MARKER_CLICK_REAL_ESTATE) != null) {
            realEstateId = arguments?.getInt(MapFragment.MAPS_MARKER_CLICK_REAL_ESTATE)!!
        }

        val realEstateViewModel = ViewModelProvider(this)[RealEstateViewModel::class.java]
        realEstateViewModel.getRealEstate(realEstateId.toLong()).observe(viewLifecycleOwner
        ) {
            displayData(it)// it = realEstate;
        }


        return binding.root
    }

      private fun displayData(realEstate: RealEstate) {

        Glide.with(binding.fragmentOnClickRealEstateImageView.context)
                .load(realEstate.mainPhotoUrl)
                .into(binding.fragmentOnClickRealEstateAgentPhoto)


        binding.fragmentOnClickRealEstateDescription.text = realEstate.description

        Glide.with(binding.fragmentOnClickRealEstateAgentPhoto.context)
                .load(realEstate.mainPhotoUrl)
                .into(binding.fragmentOnClickRealEstateAgentPhoto)

          //gridView  uri
          // implémenter GridView
          val selectedPhotos = ArrayList<Uri>()



          if (realEstate.listPhotos != null){

                      for (item in realEstate.listPhotos!!){

                       val imageUri = RealEstatePhotos.stringToUri(item.photoUri)

                       if (imageUri != null) {
                           selectedPhotos.add(imageUri)
                           Log.d("TAG"," display image uri   : $imageUri + ${selectedPhotos.size}")

                       }
                   }


           }
          val adapter = ImagesAdapter(selectedPhotos, activity)
          binding.fragmentRealEstateOtherPhotosGrid!!.adapter = adapter









          binding.fragmentOnClickRealEstateAgentName.text = realEstate.agent

        if (realEstate.status == "For sale") {
            binding.fragmentOnClickRealEstateStatus.text = realEstate.status
            binding.fragmentOnClickRealEstateStatus.setTextColor(resources
                    .getColor(R.color.fragment_on_click_real_estate_for_sale_status_color))
        } else {
            binding.fragmentOnClickRealEstateStatus.text = realEstate.status
            binding.fragmentOnClickRealEstateStatus.setTextColor(resources.getColor(
                    R.color.fragment_on_click_real_estate_sold_status_color))
        }


        val dateOfSale: String = realEstate.dateOfSale


        val surface: String = realEstate.surface.toString() + " sq" + " m"
        val numberOfRooms = realEstate.numberOfRooms.toString()


        binding.fragmentOnClickRealEstateSurfaceValue.text = surface
        binding.fragmentOnClickRealEstateRoomsValue.text = numberOfRooms
        binding.fragmentOnClickRealEstateLocationValue.text = realEstate.secondLocation
        binding.fragmentOnClickRealEstatePriceValue.text = "$" + realEstate.price
        binding.fragmentOnClickRealEstateEntryDateValue.text = realEstate.entryDate
          binding.fragmentOnClickRealEstateSaleDateValue.text = dateOfSale
   //Enregistrer gridView dans le detail fragment

        val supportMapFragment = this.childFragmentManager
                .findFragmentById(R.id.fragment_on_click_real_estate_map_fragment) as SupportMapFragment?
        supportMapFragment!!.getMapAsync(OnMapReadyCallback {
            map = it
            if (realEstate.latitude != 0.0 && realEstate.longitude != 0.0) {
                val realEstateLatLng = LatLng(realEstate.latitude, realEstate.longitude)
                map.addMarker(MarkerOptions().position(realEstateLatLng).title("Real estate marker"))
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(realEstateLatLng, 18f))
            }
        })
          val youtubeWebView: WebView? = binding.youtubeWebView

          val myVideoYoutubeId = "-bvXmLR3Ozc"

          if (youtubeWebView != null) {
              youtubeWebView.webViewClient = object : WebViewClient() {
                  override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                      return false
                  }
              }
              val webSettings: WebSettings = youtubeWebView.settings
              webSettings.javaScriptEnabled = true
              webSettings.loadWithOverviewMode = true
              webSettings.useWideViewPort = true

              youtubeWebView.loadUrl("https://www.youtube.com/embed/$myVideoYoutubeId")
          }


              /*lifecycle.addObserver(binding.fragmentOnClickRealEstateVideo)
              binding.fragmentOnClickRealEstateVideo.initialize(object : AbstractYouTubePlayerListener() {

                  override fun onReady(youTubePlayer: YouTubePlayer) {
                      if (realEstate.video.isNotEmpty()) {
                          youTubePlayer.cueVideo(realEstate.video, 0f)
                      }
                  }
              })*/


    }


    companion object {
        //fun newInstance() = RealEstateDetailFragment()
        const val KEY_ESTATE_FOR_DETAILS: String = "ID_ESTATE"

        fun newInstance(id: Long): RealEstateDetailFragment {
            val fragment = RealEstateDetailFragment()
            val args = Bundle()
            args.putLong(KEY_ESTATE_FOR_DETAILS, id)
            fragment.arguments = args
            return fragment
        }
    }


}