package com.openclassrooms.realestatemanager.utility

import android.content.Context
import android.net.wifi.WifiManager
import com.openclassrooms.realestatemanager.utility.DateConverter.Companion.simpleDateFormat
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * Created by Philippe on 21/02/2018.
 */
object Utils {


    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    fun convertDollarToEuro(dollars: Int): Int {
        return (dollars * 0.812).roundToInt().toInt()
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

    fun convertDateToString(date: Date?): String? {
        if (date == null) return ""
        val simpleDateFormat = simpleDateFormat
        return simpleDateFormat.format(date)
    }
}