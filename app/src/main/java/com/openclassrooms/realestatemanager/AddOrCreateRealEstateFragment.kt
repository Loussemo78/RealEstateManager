package com.openclassrooms.realestatemanager

import FileUtils
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.adapter.ImagesAdapter
import com.openclassrooms.realestatemanager.adapter.PickPhotosRecyclerViewAdapter
import com.openclassrooms.realestatemanager.databinding.ActivityAddOrCreateRealEstateBinding
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstatePhotos
import com.openclassrooms.realestatemanager.utility.DateConverter.Companion.simpleDateFormat
import com.openclassrooms.realestatemanager.utility.LocationUtil
import com.openclassrooms.realestatemanager.utility.TAG_REAL_ESTATE_FRAGMENT
import com.openclassrooms.realestatemanager.views.RealEstateFragment
import com.openclassrooms.realestatemanager.views.RealEstateViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList


class AddOrCreateRealEstateFragment : Fragment(), AdapterView.OnItemSelectedListener {

    val TAKE_PICTURE = 0
    val PICK_PHOTO = 1
    val TAKE_PICTURE_FOR_OTHER_PHOTOS = 2
    val PICK_PHOTO_FOR_OTHER_PHOTOS = 3

    private lateinit var binding: ActivityAddOrCreateRealEstateBinding
    private lateinit var newRealEstate: RealEstate
    private lateinit var realEstateViewModel: RealEstateViewModel
    private lateinit var othersPhotosList: ArrayList<RealEstatePhotos>

    var lastSelectedDayOfMonth = 0
    var lastSelectedMonth = 0
    var lastSelectedYear = 0
    lateinit var type: String
    lateinit var status: String
    lateinit var agent: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        newRealEstate = RealEstate()

        realEstateViewModel = ViewModelProvider(this)[RealEstateViewModel::class.java]

        //If intent coming from AddRealEstate
//        if (intent.getSerializableExtra(MainActivity.ADD_REAL_ESTATE) != null) {
//            newRealEstate = (intent.getSerializableExtra(MainActivity.ADD_REAL_ESTATE) as RealEstate)
//        } //Else if intent coming from EditRealEstate
//        else if (intent.getSerializableExtra(RealEstateFragment.EDIT_REAL_ESTATE) != null) {
//            newRealEstate = intent.getSerializableExtra(RealEstateFragment.EDIT_REAL_ESTATE) as RealEstate
//        }




    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityAddOrCreateRealEstateBinding.inflate(layoutInflater)

        othersPhotosList = ArrayList<RealEstatePhotos>()
        initializeSpinners()

        selectMainPhotoIntent()
        selectOtherPhotosIntent()


        initializeButtonSelectEntryDate();
        initializeButtonSelectSaleDate();

        //if editing, set all value in spinners, editTexts and ImageViews
        if (newRealEstate == activity?.intent?.getSerializableExtra(RealEstateFragment.EDIT_REAL_ESTATE)) {
            initializeRealEstateToEdit();
        }
        initializeFinishButton();

        return binding.root

    }

    private fun initializeSpinners() {
        val spinnerType = binding.activityAddOrEditRealEstateTypeSpinner
        val spinnerStatus = binding.activityAddOrEditRealEstateStatusSpinner
        val spinnerAgent = binding.activityAddOrEditRealEstateAgentSpinner
        initializeSpinnerAdapter(spinnerType, R.array.real_estate_types)
        initializeSpinnerAdapter(spinnerStatus, R.array.real_estate_status)
        initializeSpinnerAdapter(spinnerAgent, R.array.real_estate_agent)
        spinnerType.onItemSelectedListener = this
        spinnerStatus.onItemSelectedListener = this
        spinnerAgent.onItemSelectedListener = this
    }

    private fun initializeSpinnerAdapter(spinner: Spinner, resourceArray: Int) {
        val adapter = ArrayAdapter.createFromResource(
            requireActivity(),
            resourceArray,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, i: Int, p3: Long) {
        if (p0?.id == R.id.activity_add_or_edit_real_estate_type_spinner)
                type = p0.getItemAtPosition(i).toString()

        if (p0?.id == R.id.activity_add_or_edit_real_estate_status_spinner)
            status = p0.getItemAtPosition(i).toString();

        if (p0?.id == R.id.activity_add_or_edit_real_estate_agent_spinner)
            agent = p0.getItemAtPosition(i).toString();
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun selectMainPhotoIntent() {
        binding.activityAddOrEditRealEstateButtonMainPhoto.setOnClickListener { view ->
            val options =
                arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
            val builder: AlertDialog.Builder = AlertDialog.Builder(  requireActivity())
            builder.setTitle("Choose real estate main photo")
            builder.setItems(options,
                DialogInterface.OnClickListener { dialogInterface, i ->
                    if (options[i] == "Take Photo") {
                        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(takePicture, TAKE_PICTURE)
                    } else if (options[i] == "Choose from Gallery") {
                        val pickPhoto = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        startActivityForResult(pickPhoto, PICK_PHOTO)
                    } else if (options[i] == "Cancel") {
                        dialogInterface.dismiss()
                    }
                })
            builder.show()
        }
    }

    private fun selectOtherPhotosIntent() {
        binding.activityAddOrEditRealEstatePickPhotosButton.setOnClickListener { view ->
            val options =
                arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
            val builder =
                AlertDialog.Builder(requireActivity())
            builder.setTitle("Choose real estate other photos")
            builder.setItems(
                options
            ) { dialogInterface, i ->
                if (options[i] == "Take Photo") {
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePicture, TAKE_PICTURE_FOR_OTHER_PHOTOS)
                } else if (options[i] == "Choose from Gallery") {
                    val pickPhoto = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    pickPhoto.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(pickPhoto, PICK_PHOTO_FOR_OTHER_PHOTOS)
                } else if (options[i] == "Cancel") {
                    dialogInterface.dismiss()
                }
            }
            builder.show()
        }
    }

    private fun initializeButtonSelectEntryDate() {
        binding.activityAddOrEditRealEstateEntryDateButton.setOnClickListener { view ->
            selectDate(
                binding.activityAddOrEditRealEstateEntryDateEditText
            )
        }
    }

    private fun initializeButtonSelectSaleDate() {
        binding.activityAddOrEditRealEstateSaleDateButton.setOnClickListener { view ->
            selectDate(
                binding.activityAddOrEditRealEstateSaleDateEditText
            )
        }
    }


    private fun selectDate(editText: EditText) {
        //Get current Date
        val calendar: Calendar = Calendar.getInstance()
        lastSelectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        lastSelectedMonth = calendar.get(Calendar.MONTH)
        lastSelectedYear = calendar.get(Calendar.YEAR)


        val dateSetListener =
            OnDateSetListener { _, year, monthOfYear, dayOfMonth  ->

                calendar.set(year, monthOfYear, dayOfMonth)

                val dateString = simpleDateFormat.format(calendar.time)

                editText.setText(dateString)
                lastSelectedDayOfMonth = dayOfMonth
                lastSelectedMonth = monthOfYear
                lastSelectedYear = year

            }
        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth
        )
        datePickerDialog.show()
    }

    private fun initializeRealEstateToEdit() {
        //Initialize Spinners with the value of editing real estate
        val types = resources.getStringArray(R.array.real_estate_types)
        val statusArray = resources.getStringArray(R.array.real_estate_status)
        val agents = resources.getStringArray(R.array.real_estate_agent)
        type = newRealEstate.type
        status = newRealEstate.status
        agent = newRealEstate.agent
        for (i in types.indices) {
            if (type == types[i]) {
                binding.activityAddOrEditRealEstateTypeSpinner.setSelection(i)
            }
        }
        for (i in statusArray.indices) {
            if (status == statusArray[i]) {
                binding.activityAddOrEditRealEstateStatusSpinner.setSelection(i)
            }
        }
        for (i in agents.indices) {
            if (agent == agents[i]) {
                binding.activityAddOrEditRealEstateAgentSpinner.setSelection(i)
            }
        }
        val price = newRealEstate.price.toString()
        val surface = newRealEstate.surface.toString()
        val numberOfRooms = newRealEstate.numberOfRooms.toString()
        val numberOfBathrooms = newRealEstate.numberOfBathRooms.toString()
        val numberOfBedrooms = newRealEstate.numberOfBedRooms.toString()
        Glide.with(binding.activityAddOrEditRealEstateMainPhoto.context)
            .load(newRealEstate.mainPhotoUrl)
            .into(binding.activityAddOrEditRealEstateMainPhoto)

        binding.activityAddOrEditRealEstateFirstLocationEditText.setText(newRealEstate.place)
        binding.activityAddOrEditRealEstateFirstLocationEditText.setText(newRealEstate.place)
        binding.activityAddOrEditRealEstatePriceEditText.setText(newRealEstate.price)
        binding.activityAddOrEditRealEstateDescriptionEditText.setText(newRealEstate.description)
        newRealEstate.listPhotos?.let { othersPhotosList.addAll(it) }
        binding.activityAddOrEditRealEstateSurfaceEditText.setText(surface)
        binding.activityAddOrEditRealEstateNumberOfRoomsEditText.setText(numberOfRooms)
        binding.activityAddOrEditRealEstateNumberOfBathroomsEditText.setText(numberOfBathrooms)
        binding.activityAddOrEditRealEstateNumberOfBedroomsEditText.setText(numberOfBedrooms)
        binding.activityAddOrEditRealEstateAddressEditText.setText(newRealEstate.secondLocation)
        binding.activityAddOrEditRealEstatePointsOfInterestEditText.setText(newRealEstate.pointsOfInterest)
        binding.activityAddOrEditRealEstateEntryDateEditText.setText(
            newRealEstate.entryDate
        )
        binding.activityAddOrEditRealEstateSaleDateEditText.setText(
            newRealEstate.dateOfSale
        )
        binding.activityAddOrEditRealEstateVideoIdEditText.setText(newRealEstate.video)
    }

    private fun setNewRealEstateValue() {
        newRealEstate.type = type
        newRealEstate.status = status
        newRealEstate.agent = agent
        if (agent == "Michael  Mfoundou") {
            newRealEstate.agentPhotoUrl = "https://i.ibb.co/0MZZf43/Jessica-CCampbell.jpg"
        } else {
            newRealEstate.agentPhotoUrl = "https://i.ibb.co/Y71g9LB/Christian-Haag.jpg"
        }
        val firstLocation = binding.activityAddOrEditRealEstateFirstLocationEditText.text.toString()
        val price = binding.activityAddOrEditRealEstatePriceEditText.text.toString().toInt()
        val description = binding.activityAddOrEditRealEstateDescriptionEditText.text.toString()
        val surface = binding.activityAddOrEditRealEstateSurfaceEditText.text.toString().toInt()
        val numberOfRooms =
            binding.activityAddOrEditRealEstateNumberOfRoomsEditText.text.toString().toInt()
        val numberOfBathrooms =
            binding.activityAddOrEditRealEstateNumberOfBathroomsEditText.text.toString().toInt()
        val numberOfBedrooms =
            binding.activityAddOrEditRealEstateNumberOfBedroomsEditText.text.toString().toInt()
        val secondLocation = binding.activityAddOrEditRealEstateAddressEditText.text.toString()
        val pointsOfInterest = binding.activityAddOrEditRealEstatePointsOfInterestEditText.text
            .toString()
        val entryDate: String =  binding.activityAddOrEditRealEstateEntryDateEditText.text.toString()
        val saleDate: String =   binding.activityAddOrEditRealEstateSaleDateEditText.text.toString()
        val videoId = binding.activityAddOrEditRealEstateVideoIdEditText.text.toString()
        newRealEstate.place = firstLocation
        newRealEstate.price = price
        newRealEstate.description = description

//        newRealEstate.setMainPhoto is in onActivityResult or already assigned if editing without changes
        //mNewRealEstate.setOthersPhotos is in onActivityResult or already assigned if editing without changes
        newRealEstate.surface = surface
        newRealEstate.numberOfRooms = numberOfRooms
        newRealEstate.numberOfBathRooms = numberOfBathrooms
        newRealEstate.numberOfBedRooms = numberOfBedrooms
        newRealEstate.secondLocation = secondLocation
        //Set latitude and longitude
        LocationUtil.getLocationFromAddress(requireContext(), newRealEstate, secondLocation)
        newRealEstate.pointsOfInterest = pointsOfInterest
        newRealEstate.entryDate = entryDate.toString()
        newRealEstate.dateOfSale = saleDate.toString()
        newRealEstate.video = videoId

        GlobalScope.launch(Dispatchers.Main) {

            realEstateViewModel.addRealEstate(newRealEstate)
            requireActivity().supportFragmentManager.popBackStack()

        }
    }

    private fun initializeFinishButton() {
        //Set mNewRealEstate all value selected previously
        // If intent comes from Main Activity to add a real estate so pass data back
        // Else if intent comes from Real Estate Fragment to edit a real estate so pass data back
        binding.activityAddOrEditRealEstateOkButton.setOnClickListener { view ->
//                if (intent.getSerializableExtra(MainActivity.ADD_REAL_ESTATE) != null) {

//                } //
            //Verify if when "Sold" status is selected that Sale date has a value
            // else
            if (activity?.intent?.getSerializableExtra(RealEstateFragment.EDIT_REAL_ESTATE) != null) {
                if (binding.activityAddOrEditRealEstateStatusSpinner.selectedItem.toString() == "For sale" || binding.activityAddOrEditRealEstateStatusSpinner
                        .selectedItem.toString() == "Sold" &&
                    binding.activityAddOrEditRealEstateSaleDateEditText.text.toString().isNotEmpty()
                ) {
                    setNewRealEstateValue()
                    val intent = Intent()
                    intent.putExtra(
                        RealEstateFragment.EDIT_REAL_ESTATE,
                        newRealEstate as Serializable
                    )
                   // setResult(RESULT_OK, intent)
                 //   finish()
                } // Toast to inform user that he put "Sold" status without sale date value
                else {
                    Toast.makeText(
                        requireActivity(),
                        "Oops ...You specified Sold status without value to sale date",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                setNewRealEstateValue()
                (activity as MainActivity).setFragment(
                    RealEstateFragment.newInstance(),
                    true,
                    TAG_REAL_ESTATE_FRAGMENT
                )

//                val intent = Intent()
//                intent.putExtra(MainActivity.ADD_REAL_ESTATE, newRealEstate as Serializable)
              //  setResult(RESULT_OK, intent)
                Toast.makeText(requireActivity(),"submit ok",Toast.LENGTH_SHORT).show()
              //  finish()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        takePhotoOrGalleryOnActivityResult(requestCode, resultCode, data)
    }

    private fun takePhotoOrGalleryOnActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                //set image in ImageView from camera
                val selectedImage = data!!.extras!!["data"] as Bitmap
                binding.activityAddOrEditRealEstateMainPhoto.setImageBitmap(selectedImage)
                val imageUri: Uri = RealEstatePhotos.bitmapToImageUri(requireActivity(), selectedImage)
                // convert uri to URL
                val fileUtils = context?.let { FileUtils(it) }
                val path: String? = fileUtils?.getPath(imageUri)
//                val imageUriToString = RealEstatePhotos.uriToString(imageUri)
                newRealEstate.mainPhotoString = path
            }
        } else if (requestCode == PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                //set image in ImageView from gallery
                val selectedImage = data!!.data
                binding.activityAddOrEditRealEstateMainPhoto.setImageURI(selectedImage)
                val selectedImageToString = RealEstatePhotos.uriToString(selectedImage!!)
                newRealEstate.mainPhotoUrl = selectedImageToString
            }
        } else if (requestCode == TAKE_PICTURE_FOR_OTHER_PHOTOS) {
            if (resultCode == RESULT_OK) {
                //set image in othersPhotosList from camera
                val selectedImage = data!!.extras!!["data"] as Bitmap
                val realEstatePhotos = RealEstatePhotos()
                //Set photo uri
                val imageUri: Uri = RealEstatePhotos.bitmapToImageUri(requireActivity(), selectedImage)
                val imageUriToString = RealEstatePhotos.uriToString(imageUri)
                realEstatePhotos.photoUri = imageUriToString.toString()
                othersPhotosList.add(realEstatePhotos)
                //Set photo description
                if (othersPhotosList.size != 0) {
                    val photoDescription: String? = PickPhotosRecyclerViewAdapter.map[othersPhotosList.size - 1]
                    if (photoDescription != null) {
                        realEstatePhotos.description = photoDescription
                    }
                    newRealEstate.listPhotos = othersPhotosList
                }
            }
        } else if (requestCode == PICK_PHOTO_FOR_OTHER_PHOTOS) {
            if (resultCode == RESULT_OK) {

                val selectedPhotos = ArrayList<Uri>()
                //set image in othersPhotosList from gallery


                if (data?.clipData != null) {
                    val count: Int = data.clipData!!.itemCount
                    //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for (i in 0 until count) {
                        val realEstatePhotos = RealEstatePhotos()
                        val imageUri: Uri = data.clipData!!.getItemAt(i).uri
                        //do something with the image (save it to some directory or whatever you need to do with it here)

                        selectedPhotos.add(imageUri)

                        val imageUriToString = RealEstatePhotos.uriToString(imageUri)
                        realEstatePhotos.photoUri = imageUriToString

                        othersPhotosList.add(realEstatePhotos)

                        //Set photo description
                        if (othersPhotosList.size != 0) {



                            //val photoDescription: String? = PickPhotosRecyclerViewAdapter.map[othersPhotosList.size - 1]
                            /*if (photoDescription != null) {
                                realEstatePhotos.description = photoDescription
                            }*/
                        }
                        newRealEstate.listPhotos = othersPhotosList

                    }
                    //implémenter GridView

                    val adapter = ImagesAdapter(selectedPhotos, activity)
                    binding.activityAddOrEditRealEstatePickPhotosGrid.adapter = adapter

                    // ici la liste des photos séléctionnées
                }
            }
        }
    }
}


