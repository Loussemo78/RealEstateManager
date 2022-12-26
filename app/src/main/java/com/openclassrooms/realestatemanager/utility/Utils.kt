package com.openclassrooms.realestatemanager.utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import com.openclassrooms.realestatemanager.utility.DateConverter.Companion.simpleDateFormat
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


/**
 * Created by Philippe on 21/02/2018.
 */
  class Utils {


    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    companion object{

        fun convertDollarToEuro(dollars: Int): Int {
            return (dollars * 0.812).roundToInt().toInt()
        }

        fun convertEuroToDollar(euros: Int): Int {
            return (euros * 1.12).roundToInt().toInt()
        }


        /**
         * Conversion de la date d'aujourd'hui en un format plus approprié
         * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
         * @return
         */
        val todayDate: String
            get() {
                val dateFormat: DateFormat = simpleDateFormat
                return dateFormat.format(Date())
            }

        fun getTodayDateRefactor(): String?{
            val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
            return dateFormat.format(Date())
        }


        /**
         * Vérification de la connexion réseau
         * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
         * @param context
         * @return
         */

        fun isInternetAvailable(context: Context): Boolean {
            val wifi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            return wifi.isWifiEnabled
        }

        fun isInternetAvailableSecond(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting
        }

        fun description(): String{
            return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                    "Ut ut efficitur nisi. Quisque consequat ipsum ut semper bibendum." +
                    " Cras consectetur vehicula libero sit amet malesuada. " +
                    "Sed malesuada mi ante, quis ultrices turpis congue at. " +
                    "Duis lacus justo, dictum eu tellus in, mattis cursus nulla. " +
                    "In hac habitasse platea dictumst. Vestibulum nec hendrerit nisi. " +
                    "Nulla id leo ac diam pretium pretium a a nisi. " +
                    "Nulla blandit ornare est, vel condimentum risus. Pellentesque ac blandit arcu."
        }

        fun convertStringToDate(stringDate: String?): Date? {

            var date: Date? = null
            if (stringDate != null){
                date = simpleDateFormat.parse(stringDate)
            }
            return date
        }

        //page details
        fun convertDateToString(date: Date?): String? {
            if (date == null) return ""
            val simpleDateFormat = simpleDateFormat
            return simpleDateFormat.format(date)
        }
    }

}