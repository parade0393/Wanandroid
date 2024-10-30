package me.parade.lib_common.ext

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.location.LocationManager
import android.os.Build
import android.util.TypedValue
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

/**
 * 状态栏高度
 */
fun Activity.statusBarHeight():Int{
    var statusBarHeight:Int = 0
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
         statusBarHeight = window.decorView.rootWindowInsets?.getInsets(WindowInsetsCompat.Type.statusBars())?.top?:0
    }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
        statusBarHeight = ViewCompat.getRootWindowInsets(window.decorView)?.getInsets(WindowInsetsCompat.Type.statusBars())?.top?:0
    }
    if (statusBarHeight == 0){
        val rectangle = Rect()
       window.decorView.getWindowVisibleDisplayFrame(rectangle)
        statusBarHeight = rectangle.top
    }
    if (statusBarHeight == 0){
        try {
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                statusBarHeight = resources.getDimensionPixelSize(resourceId)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    if (statusBarHeight == 0){

        // 如果还是获取不到，使用一个默认值
        val density: Float = resources.displayMetrics.density
        statusBarHeight = (24 * density + 0.5f).toInt()
    }
    return  statusBarHeight
}

/**
 * 标题栏高度
 */
fun Context.actionBarHeight():Int{
    val tv = TypedValue()
    if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
        return TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
    }
    return 56.px
}

