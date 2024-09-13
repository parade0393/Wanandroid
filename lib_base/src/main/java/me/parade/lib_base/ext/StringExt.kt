package me.parade.lib_base.ext

import android.graphics.Bitmap
import android.os.Build
import android.webkit.MimeTypeMap
import com.blankj.utilcode.util.FileUtils
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.security.MessageDigest
import java.util.regex.Pattern

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

/**
 * 获取BitMap.CompressFormat
 */
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

/**
 * 实现手机号中间4位显示*或者切换不显示*
 * @param originalPhone 真实手机号
 */
fun String.handlePhoneMiddleText(originalPhone: String): String {
    return if (contains("*")) originalPhone else this.replace(
        Regex("(\\d{3})\\d{4}(\\d{4})"),
        "$1****$2"
    )

}

/**
 * 从url中获取指定参数值
 */
fun String.getParamByUrl(name:String):String?{

    var url = this
    url += "&"
    val regex = "([?&])#?${name}=[a-zA-Z0-9]*(&)?"
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(url)
    return if (matcher.find()) {
        matcher.group(0)!!.split("=")[1].replace("&","")
    } else null
}

/**
 * 字符串MD5加密
 */
fun String.getMd5String(): String {
    val instance = MessageDigest.getInstance("md5").also { it.update(this.toByteArray()) }
    return instance.digest().joinToString(separator = "") { "%02x".format(it) }
}

/**
 * 一个字符串里某个特定的字符串出现的次数
 * @param letter 要匹配的字符串
 */
fun String.timesOfStr(letter: String):Int{
    val pattern = Pattern.compile(letter)
    val matcher = pattern.matcher(this)
    var times = 0
    while (matcher.find()){
        times++
    }
    return times
}

/**
 * 查找指定字符在字符串第n次出现的位置
 * @param letter 指定的字符
 * @param position 第几次出现
 * @return 出现的位置 找不到返回-1
 */
fun String.findIndexOfPosition(letter: String, position: Int): Int {
    val pattern = Pattern.compile(letter)
    val matcher = pattern.matcher(this)
    var num = 0
    while (matcher.find()) {
        num++
        if (num == position) break
    }
    return try {
        matcher.start()
    } catch (e: IllegalStateException) {
        -1
    }
}

/** 是否是合法的json字符串 */
fun String.isValidateJson(): Boolean {
    val jsonElement: JsonElement?
    try {
        jsonElement = JsonParser().parse(this)
    } catch (e: Exception) {
        return false
    }

    if (jsonElement == null) {
        return false
    }
    return jsonElement.isJsonArray || jsonElement.isJsonObject || jsonElement.isJsonPrimitive
}

