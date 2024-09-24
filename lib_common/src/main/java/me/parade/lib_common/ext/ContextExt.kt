package me.parade.lib_common.ext

import android.content.Context
import android.location.LocationManager

fun Context.checkGPSIsOpen():Boolean{
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

/**
 * 是否安装了某个应用
 */
fun Context.isInstallAvailable(packageName: String):Boolean{
    val installedPackages = packageManager.getInstalledPackages(0)// 获取所有已安装程序的包信息
    installedPackages.forEach {
        if (packageName == it.packageName){
            return true
        }
    }
    return false
}

//是否安装了微信
fun Context.isWeixinAvilible():Boolean{
   return isInstallAvailable("com.tencent.mm")
}

/**
 * 是否安装了支付宝
 */
fun Context.isAliPayInstalled():Boolean{
    return isInstallAvailable("com.eg.android.AlipayGphone")
}

