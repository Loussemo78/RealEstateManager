package com.openclassrooms.realestatemanager

import FileUtils
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
 import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.adapter.ImagesAdapter
import com.openclassrooms.realestatemanager.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.databinding.FragmentAddOrCreateRealEstateBinding
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
import java.util.*
import kotlin.collections.ArrayList


class AddOrCreateRealEstateFragment : Fragment(), AdapterView.OnItemSelectedListener {

    val TAKE_PICTURE = 0
    val PICK_PHOTO = 1
    val TAKE_PICTURE_FOR_OTHER_PHOTOS = 2
    val PICK_PHOTO_FOR_OTHER_PHOTOS = 3

    private lateinit var binding: FragmentAddOrCreateRealEstateBinding
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



    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddOrCreateRealEstateBinding.inflate(layoutInflater)




        Handler(Looper.getMainLooper()).post {


            othersPhotosList = ArrayList<RealEstatePhotos>()

            initializeSpinners()

            selectMainPhotoIntent()
            selectOtherPhotosIntent()


            initializeButtonSelectEntryDate();
            initializeButtonSelectSaleDate();

            if (newRealEstate == activity?.intent?.getSerializableExtra(RealEstateFragment.EDIT_REAL_ESTATE)) {
                initializeRealEstateToEdit();
            }
            initializeFinishButton()
        }


        val id = arguments?.getLong("id")
        if (id != null) {

            val dao: RealEstateDao = context?.let { RealEstateDatabase.getInstance(it)?.realEstateDao }!!

            GlobalScope.launch(Dispatchers.IO) {

                newRealEstate = dao.getRealEstateFromID(id)
                displayDataToUpdate(newRealEstate)// it = realEstate; affichage du AddOrCreate  setter
                //setNewRealEstateValue(editRealEstate)

            }
        }
        return binding.root
    }



    private fun initializeSpinners() {
        val spinnerType = binding.fragmentAddOrEditRealEstateTypeSpinner
        val spinnerStatus = binding.fragmentAddOrEditRealEstateStatusSpinner
        val spinnerAgent = binding.fragmentAddOrEditRealEstateAgentSpinner
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
        if (p0?.id == R.id.fragment_add_or_edit_real_estate_type_spinner)
            type = p0.getItemAtPosition(i).toString()

        if (p0?.id == R.id.fragment_add_or_edit_real_estate_status_spinner)
            status = p0.getItemAtPosition(i).toString();

        if (p0?.id == R.id.fragment_add_or_edit_real_estate_agent_spinner)
            agent = p0.getItemAtPosition(i).toString();
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun selectMainPhotoIntent() {
        binding.fragmentAddOrEditRealEstateButtonMainPhoto.setOnClickListener { view ->
            val options =
                    arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
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
        binding.fragmentAddOrEditRealEstatePickPhotosButton.setOnClickListener { view ->
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
                    takePicture.resolveActivity(requireContext().packageManager)?.also {
                        startActivityForResult(takePicture, TAKE_PICTURE_FOR_OTHER_PHOTOS)
                    }


                } else if (options[i] == "Choose from Gallery") {
                    val filesIntent: Intent = Intent(Intent.ACTION_GET_CONTENT)
                    filesIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    filesIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    filesIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    filesIntent.type = "*/*";  //use image/* for photos, etc.
                    startActivityForResult(filesIntent, PICK_PHOTO_FOR_OTHER_PHOTOS)
                } else if (options[i] == "Cancel") {
                    dialogInterface.dismiss()
                }
            }
            builder.show()
        }
    }


    private fun initializeButtonSelectEntryDate() {
        binding.fragmentAddOrEditRealEstateEntryDateButton.setOnClickListener { view ->
            selectDate(
                    binding.fragmentAddOrEditRealEstateEntryDateEditText
            )
        }
    }

    private fun initializeButtonSelectSaleDate() {
        binding.fragmentAddOrEditRealEstateSaleDateButton.setOnClickListener { view ->
            selectDate(
                    binding.fragmentAddOrEditRealEstateSaleDateEditText
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
                OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

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
                binding.fragmentAddOrEditRealEstateTypeSpinner.setSelection(i)
            }
        }
        for (i in statusArray.indices) {
            if (status == statusArray[i]) {
                binding.fragmentAddOrEditRealEstateStatusSpinner.setSelection(i)
            }
        }
        for (i in agents.indices) {
            if (agent == agents[i]) {
                binding.fragmentAddOrEditRealEstateAgentSpinner.setSelection(i)
            }
        }
        val price = newRealEstate.price.toString()
        val surface = newRealEstate.surface.toString()
        val numberOfRooms = newRealEstate.numberOfRooms.toString()
        Glide.with(binding.fragmentAddOrEditRealEstateMainPhoto.context)
                .load(newRealEstate.mainPhotoUrl)
                .into(binding.fragmentAddOrEditRealEstateMainPhoto)
        binding.fragmentAddOrEditRealEstateFirstLocationEditText.setText(newRealEstate.place)
        binding.fragmentAddOrEditRealEstatePriceEditText.setText(newRealEstate.price)
        binding.fragmentAddOrEditRealEstateDescriptionEditText.setText(newRealEstate.description)
        newRealEstate.listPhotos?.let { othersPhotosList.addAll(it) }
        binding.fragmentAddOrEditRealEstateSurfaceEditText.setText(surface)
        binding.fragmentAddOrEditRealEstateNumberOfRoomsEditText.setText(numberOfRooms)
        binding.fragmentAddOrEditRealEstateAddressEditText.setText(newRealEstate.secondLocation)
        binding.fragmentAddOrEditRealEstateEntryDateEditText.setText(
                newRealEstate.entryDate
        )
        binding.fragmentAddOrEditRealEstateSaleDateEditText.setText(
                newRealEstate.dateOfSale
        )
    }

    private fun displayDataToUpdate(realEstate: RealEstate) {
        type = realEstate.type
        status = realEstate.status
        agent = realEstate.agent
        if (agent == "Michael  Mfoundou") {
            realEstate.agentPhotoUrl = "https://i.ibb.co/0MZZf43/Jessica-CCampbell.jpg"
        } else {
            realEstate.agentPhotoUrl = "https://i.ibb.co/Y71g9LB/Christian-Haag.jpg"
        }
        Looper.prepare() // to be able to make toast
        binding.fragmentAddOrEditRealEstateFirstLocationEditText.setText(realEstate.firstLocation)



        activity?.runOnUiThread(Runnable {
            Glide.with(binding.fragmentAddOrEditRealEstateMainPhoto.context)
                    .load("" + realEstate.mainPhotoUrl)
                    .into(binding.fragmentAddOrEditRealEstateMainPhoto)


        })

        binding.fragmentAddOrEditRealEstatePriceEditText.setText("" + realEstate.price)
        binding.fragmentAddOrEditRealEstateFirstLocationEditText.setText("" + realEstate.place)
        binding.fragmentAddOrEditRealEstateDescriptionEditText.setText("" + realEstate.description)
        binding.fragmentAddOrEditRealEstateSurfaceEditText.setText("" + realEstate.surface)
        binding.fragmentAddOrEditRealEstateNumberOfRoomsEditText.setText("" + realEstate.numberOfRooms)
        binding.fragmentAddOrEditRealEstateAddressEditText.setText("" + realEstate.secondLocation)
        binding.fragmentAddOrEditRealEstateEntryDateEditText.setText("" + realEstate.entryDate).toString()
        binding.fragmentAddOrEditRealEstateSaleDateEditText.setText("" + realEstate.dateOfSale).toString()
        Looper.loop()

    }

    private fun setNewRealEstateValue(newRealEstate: RealEstate) {

        newRealEstate.type = type
        newRealEstate.status = status
        newRealEstate.agent = agent
        if (agent == "Michael  Mfoundou") {
            newRealEstate.agentPhotoUrl = "https://i.ibb.co/0MZZf43/Jessica-CCampbell.jpg"
        } else {
            newRealEstate.agentPhotoUrl = "https://i.ibb.co/Y71g9LB/Christian-Haag.jpg"
        }

        val firstLocation = binding.fragmentAddOrEditRealEstateFirstLocationEditText.text.toString()
        val price = binding.fragmentAddOrEditRealEstatePriceEditText.text.toString().toInt()
        val description = binding.fragmentAddOrEditRealEstateDescriptionEditText.text.toString()
        val surface = binding.fragmentAddOrEditRealEstateSurfaceEditText.text.toString().toInt()
        val numberOfRooms =
                binding.fragmentAddOrEditRealEstateNumberOfRoomsEditText.text.toString().toInt()
        val secondLocation = binding.fragmentAddOrEditRealEstateAddressEditText.text.toString()
        val entryDate: String = binding.fragmentAddOrEditRealEstateEntryDateEditText.text.toString()
        val saleDate: String = binding.fragmentAddOrEditRealEstateSaleDateEditText.text.toString()
        newRealEstate.place = firstLocation
        newRealEstate.price = price
        newRealEstate.description = description


        newRealEstate.surface = surface
        newRealEstate.numberOfRooms = numberOfRooms
        newRealEstate.secondLocation = secondLocation
        //Set latitude and longitude
        LocationUtil.getLocationFromAddress(requireContext(), newRealEstate, secondLocation)
        newRealEstate.entryDate = entryDate.toString()
        newRealEstate.dateOfSale = saleDate.toString()


        GlobalScope.launch(Dispatchers.Main) {

            if (newRealEstate != null && newRealEstate.id != null) {
                var temp = newRealEstate
                realEstateViewModel.deleteRealEstate(temp)
                realEstateViewModel.addRealEstate(newRealEstate)
            } else
                realEstateViewModel.addRealEstate(newRealEstate)
        }
    }


    private fun initializeFinishButton() {
        //Set mNewRealEstate all value selected previously
        // If intent comes from Main Activity to add a real estate so pass data back
        // Else if intent comes from Real Estate Fragment to edit a real estate so pass data back
        binding.fragmentAddOrEditRealEstateOkButton.setOnClickListener {


            setNewRealEstateValue(newRealEstate)

            (activity as MainActivity).setFragment(
                    RealEstateFragment.newInstance(),
                    true,
                    TAG_REAL_ESTATE_FRAGMENT
            )

            context?.let { it1 -> createNotification(it1) }


        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        takePhotoOrGalleryOnActivityResult(requestCode, resultCode, data)
    }


    fun createNotification(context: Context){
        //Create an instance of NotificationCompat.Builder
        val channelId = "notification_add_real_estate"

        val builder = NotificationCompat.Builder(requireContext(), channelId)
                .setSmallIcon(R.drawable.icons8_android_os)
                .setContentTitle("RealEstateManager")
                .setContentText("You have just added a property")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        val notificationManager = context.applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // If the Android version is greater than or equal to Oreo (26), you must create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Display notification
        notificationManager.notify(0, builder.build())
    }


    private fun takePhotoOrGalleryOnActivityResult(
            requestCode: Int,
            resultCode: Int,
            data1: Intent?
    ) {
        if (requestCode == TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                //set image in ImageView from camera
                val selectedImage = data1!!.extras!!["data"] as Bitmap
                binding.fragmentAddOrEditRealEstateMainPhoto.setImageBitmap(selectedImage)
                val imageUri: Uri = RealEstatePhotos.bitmapToImageUri(requireActivity(), selectedImage)
                // convert uri to URL
                val fileUtils = context?.let { FileUtils(it) }
                val path: String? = fileUtils?.getPath(imageUri)
                if (path != null) {
                    newRealEstate.mainPhotoUrl = path
                }
            }
        } else if (requestCode == PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                //set image in ImageView from gallery
                val selectedImage = data1!!.data
                binding.fragmentAddOrEditRealEstateMainPhoto.setImageURI(selectedImage)
                val selectedImageToString = RealEstatePhotos.uriToString(selectedImage!!)
                newRealEstate.mainPhotoUrl = selectedImageToString
            }
        } else if (requestCode == TAKE_PICTURE_FOR_OTHER_PHOTOS) {
            if (resultCode == RESULT_OK) {
                //set image in othersPhotosList from camera
                //val selectedImage = data1!!.extras!!["data"] as Bitmap
                val selectedImage = ArrayList<Bitmap>().apply {
                    add(data1!!.extras!!["data"] as Bitmap)
                }

                val count: Int? = data1?.extras?.size()
                for (i in 0 until count!!) {
                    val realEstatePhotos = RealEstatePhotos()
                    //Set photo uri
                    val imageUri: ArrayList<Uri> = RealEstatePhotos.bitmapToListUri(requireActivity(), selectedImage)
                    val imageUriToString = RealEstatePhotos.listUriToString(imageUri)
                    realEstatePhotos.photoUri = imageUriToString.toString()
                    othersPhotosList.add(realEstatePhotos)

                    newRealEstate.listPhotos = othersPhotosList


                }

                val adapter = ImagesAdapter(selectedImage, activity)
                binding.fragmentAddOrEditRealEstatePickPhotosGrid.adapter = adapter

            }
        } else if (requestCode == PICK_PHOTO_FOR_OTHER_PHOTOS) {
            if (resultCode == RESULT_OK) {

                val selectedPhotos = ArrayList<Uri>()
                //set image in othersPhotosList from gallery


                if (data1?.data != null) {


                    val realEstatePhotos = RealEstatePhotos()
                    val imageUri: Uri = data1.data!!
                    //do something with the image (save it to some directory or whatever you need to do with it here)

                    selectedPhotos.add(imageUri)

                    val imageUriToString = RealEstatePhotos.uriToString(imageUri)
                    realEstatePhotos.photoUri = imageUriToString

                    othersPhotosList.add(realEstatePhotos)

                    newRealEstate.listPhotos = othersPhotosList


                }
                if (data1?.clipData != null) {
                    val count: Int = data1.clipData!!.itemCount
                    //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for (i in 0 until count) {
                        //val realEstatePhotos = RealEstatePhotos()
                        val imageUri: Uri = data1.clipData!!.getItemAt(i).uri
                        //do something with the image (save it to some directory or whatever you need to do with it here)

                        selectedPhotos.add(imageUri)

                        val realEstatePhotos = RealEstatePhotos()
                        val imageUriToString = RealEstatePhotos.uriToString(imageUri)

                        realEstatePhotos.photoUri = imageUriToString



                        othersPhotosList.add(realEstatePhotos)



                        newRealEstate.listPhotos = othersPhotosList

                    }

                }

                //implement GridView
                // Add Strings instead of URIs

                val adapter = ImagesAdapter(selectedPhotos, activity)
                // here the list of selected photos
                binding.fragmentAddOrEditRealEstatePickPhotosGrid.adapter = adapter

            }
        }
    }

}



