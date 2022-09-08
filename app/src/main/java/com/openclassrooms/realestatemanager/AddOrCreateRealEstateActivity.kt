package com.openclassrooms.realestatemanager

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.databinding.ActivityAddOrCreateRealEstateBinding
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstatePhotos
import com.openclassrooms.realestatemanager.utility.Utils
import com.openclassrooms.realestatemanager.views.RealEstateFragment
import java.io.Serializable
import java.util.*


class AddOrCreateRealEstateActivity : AppCompatActivity() , AdapterView.OnItemSelectedListener {

    val TAKE_PICTURE = 0
    val PICK_PHOTO = 1
    val TAKE_PICTURE_FOR_OTHER_PHOTOS = 2
    val PICK_PHOTO_FOR_OTHER_PHOTOS = 3

    private lateinit var binding: ActivityAddOrCreateRealEstateBinding
    private lateinit var newRealEstate: RealEstate
    var othersPhotosList: ArrayList<RealEstatePhotos>? = null

    var lastSelectedDayOfMonth = 0
    var lastSelectedMonth = 0
    var lastSelectedYear = 0
    lateinit var  type: String
    lateinit var status:String
    lateinit var agent:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddOrCreateRealEstateBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        othersPhotosList = ArrayList<RealEstatePhotos>()

        //If intent coming from AddRealEstate
        if (intent.getSerializableExtra(MainActivity.ADD_REAL_ESTATE) != null) {
            newRealEstate = (intent.getSerializableExtra(MainActivity.ADD_REAL_ESTATE) as RealEstate)
        } //Else if intent coming from EditRealEstate
        else if (intent.getSerializableExtra(RealEstateFragment.EDIT_REAL_ESTATE) != null) {
            newRealEstate = intent.getSerializableExtra(RealEstateFragment.EDIT_REAL_ESTATE) as RealEstate
        }
    }


    private fun initializeSpinnerAdapter(resourceArray:Int , spinner: Spinner){
        val adapter = ArrayAdapter.createFromResource(this, resourceArray, R.layout.s)

        adapter.setDropDownViewResource(R.layout.su)

        spinner.adapter
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, i: Int, p3: Long) {
        if (p0?.id == R.id.activity_add_or_edit_real_estate_type_spinner)
            type = p0.getItemAtPosition(i).toString();

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
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
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
                AlertDialog.Builder(this)
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
                    startActivityForResult(pickPhoto, PICK_PHOTO_FOR_OTHER_PHOTOS)
                } else if (options[i] == "Cancel") {
                    dialogInterface.dismiss()
                }
            }
            builder.show()
        }
    }

    private fun selectDate(editText: EditText) {
        //Get current Date
        val calendar: Calendar = Calendar.getInstance()
        lastSelectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        lastSelectedMonth = calendar.get(Calendar.MONTH)
        lastSelectedYear = calendar.get(Calendar.YEAR)
        val dateSetListener =
            OnDateSetListener { datePicker, i, i1, i2 ->
                editText.setText(i2.toString() + "/" + (i1 + 1) + "/" + i)
                lastSelectedDayOfMonth = i2
                lastSelectedMonth = i1
                lastSelectedYear = i
            }
        val datePickerDialog = DatePickerDialog(
            this,
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
        if (newRealEstate.mainPhotoUrl != null) {
            Glide.with(binding.activityAddOrEditRealEstateMainPhoto.context)
                .load(newRealEstate.mainPhotoUrl)
                .into(binding.activityAddOrEditRealEstateMainPhoto)
        } else {
            val mainPhotoUri: Uri? = RealEstatePhotos.stringToUri(newRealEstate.mainPhotoString)
            binding.activityAddOrEditRealEstateMainPhoto.setImageURI(mainPhotoUri)
        }
        binding.activityAddOrEditRealEstateFirstLocationEditText.setText(newRealEstate.place)
        binding.activityAddOrEditRealEstatePriceEditText.setText(newRealEstate.price)
        binding.activityAddOrEditRealEstateDescriptionEditText.setText(newRealEstate.description)
        //othersPhotosList.addAll(newRealEstate.numberOfPhotos)
        binding.activityAddOrEditRealEstateSurfaceEditText.setText(surface)
        binding.activityAddOrEditRealEstateNumberOfRoomsEditText.setText(numberOfRooms)
        binding.activityAddOrEditRealEstateNumberOfBathroomsEditText.setText(numberOfBathrooms)
        binding.activityAddOrEditRealEstateNumberOfBedroomsEditText.setText(numberOfBedrooms)
        //binding.activityAddOrEditRealEstateAddressEditText.text(newRealEstate.getSecondLocation())
        binding.activityAddOrEditRealEstatePointsOfInterestEditText.setText(newRealEstate.pointsOfInterest)
        binding.activityAddOrEditRealEstateEntryDateEditText.setText(Utils.convertDateToString(newRealEstate.entryDate))
        binding.activityAddOrEditRealEstateSaleDateEditText.setText(Utils.convertDateToString(newRealEstate.dateOfSale)
        )
        binding.activityAddOrEditRealEstateVideoIdEditText.setText(newRealEstate.video)
    }

    private fun setNewRealEstateValue() {
        newRealEstate.type = type
        newRealEstate.status = status
        newRealEstate.agent = agent
        if (agent == "Jessica C. Campbell") {
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
        val entryDate: Date = Utils.convertStringToDate(
            binding.activityAddOrEditRealEstateEntryDateEditText.text.toString()
        )
        val saleDate: Date = Utils.convertStringToDate(
            binding.activityAddOrEditRealEstateSaleDateEditText.text.toString()
        )
        val videoId = binding.activityAddOrEditRealEstateVideoIdEditText.text.toString()
        newRealEstate.place = firstLocation
        newRealEstate.price = price
        newRealEstate.description = description
        //mNewRealEstate.setMainPhoto is in onActivityResult or already assigned if editing without changes
        //mNewRealEstate.setOthersPhotos is in onActivityResult or already assigned if editing without changes
        newRealEstate.surface = surface
        newRealEstate.numberOfRooms = numberOfRooms
        newRealEstate.numberOfBathRooms = numberOfBathrooms
        newRealEstate.numberOfBedRooms = numberOfBedrooms
        //newRealEstate.setSecondLocation(secondLocation)
        //Set latitude and longitude
      //  LocationUtil.getLocationFromAddress(this, mNewRealEstate, secondLocation)
        newRealEstate.pointsOfInterest = pointsOfInterest
        newRealEstate.entryDate = entryDate
        newRealEstate.dateOfSale = saleDate
        newRealEstate.video = videoId
    }

    private fun initializeFinishButton() {
        //Set mNewRealEstate all value selected previously
        // If intent comes from Main Activity to add a real estate so pass data back
        if (intent.getSerializableExtra(MainActivity.ADD_REAL_ESTATE) != null) {
            binding.activityAddOrEditRealEstateOkButton.setOnClickListener { view ->
                setNewRealEstateValue()
                val intent = Intent()
                intent.putExtra(MainActivity.ADD_REAL_ESTATE, newRealEstate as Serializable)
                setResult(RESULT_OK, intent)
                finish()
            }
        } // Else if intent comes from Real Estate Fragment to edit a real estate so pass data back
        else if (intent.getSerializableExtra(RealEstateFragment.EDIT_REAL_ESTATE) != null) {
            binding.activityAddOrEditRealEstateOkButton.setOnClickListener { view ->

                //Verify if when "Sold" status is selected that Sale date has a value
                if (binding.activityAddOrEditRealEstateStatusSpinner.selectedItem.toString()
                        .equals("For sale") || binding.activityAddOrEditRealEstateStatusSpinner
                        .selectedItem.toString().equals("Sold") &&
                    !binding.activityAddOrEditRealEstateSaleDateEditText.text.toString()
                        .isEmpty()
                ) {
                    setNewRealEstateValue()
                    val intent = Intent()
                    intent.putExtra(
                        RealEstateFragment.EDIT_REAL_ESTATE,
                        newRealEstate as Serializable
                    )
                    setResult(RESULT_OK, intent)
                    finish()
                } // Toast to inform user that he put "Sold" status without sale date value
                else {
                    Toast.makeText(
                        this,
                        "Oops ...You specified Sold status without value to sale date",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        takePhotoOrGalleryOnActivityResult(requestCode, resultCode, data)
    }

    private fun takePhotoOrGalleryOnActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                //set image in ImageView from camera
                val selectedImage = data!!.extras!!["data"] as Bitmap?
                binding.activityAddOrEditRealEstateMainPhoto.setImageBitmap(selectedImage)
//                val imageUri: Uri = RealEstatePhotos.bitmapToImageUri(this, selectedImage)
                val imageUriToString = RealEstatePhotos.uriToString(imageUri)
                newRealEstate.mainPhotoString = imageUriToString
            }
        } else if (requestCode == PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                //set image in ImageView from gallery
                val selectedImage = data!!.data
                binding.activityAddOrEditRealEstateMainPhoto.setImageURI(selectedImage)
                val selectedImageToString = RealEstatePhotos.uriToString(selectedImage!!)
                newRealEstate.mainPhotoString = selectedImageToString
            }
        } else if (requestCode == TAKE_PICTURE_FOR_OTHER_PHOTOS) {
            if (resultCode == RESULT_OK) {
                //set image in othersPhotosList from camera
                val selectedImage = data!!.extras!!["data"] as Bitmap?
//                val realEstatePhotos = RealEstatePhotos()
                //Set photo uri
//                val imageUri: Uri = RealEstatePhotos.bitmapToImageUri(this, selectedImage)
                val imageUriToString = RealEstatePhotos.uriToString(imageUri)
//                realEstatePhotos.setPhotoUri(imageUriToString)
                othersPhotosList!!.add(realEstatePhotos)
                //Set photo description
//                if (othersPhotosList!!.size != 0) {
//                    val photoDescription: String = MyPickPhotosRecyclerViewAdapter.map
//                        .get(othersPhotosList!!.size - 1)
//                    realEstatePhotos.setDescription(photoDescription)
//                    mNewRealEstate.setPhotos(othersPhotosList)
//                }
            }
        } else if (requestCode == PICK_PHOTO_FOR_OTHER_PHOTOS) {
            if (resultCode == RESULT_OK) {
                //set image in othersPhotosList from gallery
                val selectedImage = data!!.data
                val realEstatePhotos = RealEstatePhotos()
                val imageUriToString = RealEstatePhotos.uriToString(selectedImage!!)
//                realEstatePhotos.setPhotoUri(imageUriToString)
                othersPhotosList!!.add(realEstatePhotos)
                //Set photo description
//                if (othersPhotosList!!.size != 0) {
//                    val photoDescription: String = MyPickPhotosRecyclerViewAdapter.map.get(
//                        othersPhotosList!!.size - 1
//                    )
//                    realEstatePhotos.setDescription(photoDescription)
//                    newRealEstate.setP
//                        .setPhotos(othersPhotosList)
//                }
            }
        }
    }


}