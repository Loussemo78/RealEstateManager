package com.openclassrooms.realestatemanager

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.database.RealEstateHandlerThread
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.di.DI
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.utility.TAG_REAL_ESTATE_FRAGMENT
import com.openclassrooms.realestatemanager.views.MapFragment
import com.openclassrooms.realestatemanager.views.RealEstateFragment
import com.openclassrooms.realestatemanager.views.RealEstateViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var  realEstateHandlerThread: RealEstateHandlerThread
    private lateinit var viewModel: RealEstateViewModel
    private lateinit var  repository: RealEstateRepository

    private lateinit var realEstateFilteredList: ArrayList<RealEstate>


    companion object{
        private const val ALL_PERMISSIONS = 10
        const val CHANNEL_ID = "notification_add_real_estate"

        const val ADD_REAL_ESTATE_REQUEST_CODE = 100
        const val ADD_REAL_ESTATE = "ADD_REAL_ESTATE"
        const val NOTIFICATION_REQUEST_CODE = 20

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
  // gestion connexion internet
        checkPermissionsGranted()

        repository =  DI.getRepository(application)
        GlobalScope.launch(Dispatchers.Main) {

            val repository = RealEstateRepository(this@MainActivity)
            val realEstateObject = RealEstate(1,"Type",12 ,"Place",
                    1500,2,"Description"
                    ,"https://www.notreloft.com/images/2016/10/loft-Manhattan-New-York-00500-800x533.jpg",3,"Address","mainPhoto",
                    "points","", null,-73.97619
                    ,40.75691,"video")

            repository.insertRealEstate(realEstateObject)
        }
        initializeBottomNavigationItemView()

        viewModel = ViewModelProvider(this)[RealEstateViewModel::class.java]

        supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_fragment_container_view_list,
                        RealEstateFragment()).commit()
        realEstateHandlerThread = RealEstateHandlerThread("insertRealEstate")

        realEstateFilteredList = ArrayList()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {

               supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.activity_main_fragment_container_view_list, AddOrCreateRealEstateFragment())
                    .addToBackStack(RealEstateFragment::class.java.simpleName)
                    .commit()


            }
            R.id.menu_search -> {


                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.activity_main_fragment_container_view_list, SearchRealEstateFragment())
                    .addToBackStack(RealEstateFragment::class.java.simpleName)
                    .commit()
            }

            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun initializeBottomNavigationItemView(){
        binding.bottomNavigationView.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.page1 -> supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_fragment_container_view_list,
                                RealEstateFragment()).commit()
                R.id.page2 -> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.activity_main_fragment_container_view_list,
                                    MapFragment()).commit()
                }
            }
            true
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun setFragment(fragment: Fragment, addBackStack: Boolean, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
            when (tag) {
                TAG_REAL_ESTATE_FRAGMENT -> transaction.replace(R.id.activity_main_fragment_container_view_list, fragment, tag)
                else -> transaction.replace(R.id.activity_main_fragment_container_view_list, fragment, tag)
            }

        if (addBackStack) transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun checkPermissionsGranted() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
                    == PackageManager.PERMISSION_GRANTED) && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
        } else {
            getPermissions()
        }
    }

    private fun getPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), ALL_PERMISSIONS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ALL_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] ==
                PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                getPermissions()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_REAL_ESTATE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val newRealEstate = data?.getSerializableExtra(ADD_REAL_ESTATE) as RealEstate?
                realEstateHandlerThread.startCreateRealEstateHandler(newRealEstate, viewModel)
                showNotificationOnAddRealEstate()
            }
        }
    }

    private fun showNotificationOnAddRealEstate() {
        val title = "RealEstateManager"
        val message = "Real Estate correctly added"
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)

        notificationBuilder
            .setSmallIcon(R.drawable.icons8_android_os)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        val notificationManager = applicationContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName: CharSequence = "RealEstateAddChannel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, channelName,
                importance
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(NOTIFICATION_REQUEST_CODE, notificationBuilder.build())
    }


}