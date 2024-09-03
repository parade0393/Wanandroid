package me.parade.lib_base.ext

import android.graphics.Bitmap
import android.os.Build
import android.webkit.MimeTypeMap
import com.blankj.utilcode.util.FileUtils

/**
 * 根据URL路径获取文件名
 */
fun String.getFileNameByUrl():String{
   if (this.isBlank()) return ""
    FileUtils.getFileName(this)
    return this.substringAfterLast("/","")
}

/**
 * 根据URL路径获取文件类型
 */
fun String.getFileTypeByUrl():String{
    if (this.isBlank()) return ""
    return this.substringAfterLast(".","")
}

/**
 * 根据URL路径判断文件是不是图片
 */
fun String.isImageFile(): Boolean {
    val imageExtensions = listOf("jpg", "jpeg", "png", "gif", "bmp", "webp")
    return imageExtensions.contains(this.getFileTypeByUrl().lowercase())
}

/**
 * 根据URL路径获取MimeType
 */
fun String.getMimeTypeFromFromUrl(defaultType:String = "image/*"):String{
    val extension = MimeTypeMap.getFileExtensionFromUrl(this)
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)?:defaultType
}

 fun String.getBitmapFormat(): Bitmap.CompressFormat {
    val fileName = this.lowercase()
    return when {
        fileName.endsWith(".png") -> Bitmap.CompressFormat.PNG
        fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") -> Bitmap.CompressFormat.JPEG
        fileName.endsWith(".webp") -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            Bitmap.CompressFormat.WEBP_LOSSLESS else Bitmap.CompressFormat.WEBP
        else -> Bitmap.CompressFormat.PNG
    }
}