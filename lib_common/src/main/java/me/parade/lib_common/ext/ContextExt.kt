package me.parade.lib_common.ext

import android.content.Context
import android.location.LocationManager

fun Context.checkGPSIsOpen():Boolean{
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}



