package me.parade.lib_common.ext


import android.util.Log
import me.parade.lib_common.BuildConfig

/**
 * @author : parade
 * date : 2020/11/2
 * description : 日志工具类
 */

const val TAG = "parade0393"

var showLog = BuildConfig.DEBUG
var showStackTrace = false

private enum class LEVEL {
    V, D, I, W, E
}

fun String.logv(tag: String = TAG,isPrint:Boolean = false) = log(LEVEL.V, tag, this,isPrint)
fun String.logd(tag: String = TAG,isPrint:Boolean = false) = log(LEVEL.D, tag, this,isPrint)
fun String.logi(tag: String = TAG,isPrint:Boolean = false) = log(LEVEL.I, tag, this,isPrint)
fun String.logw(tag: String = TAG,isPrint:Boolean = false) = log(LEVEL.W, tag, this,isPrint)
fun String.loge(tag: String = TAG,isPrint:Boolean = false) = log(LEVEL.E, tag, this,isPrint)

fun logv(message:Any,tag: String = TAG,isPrint:Boolean = false) = log(LEVEL.V, tag, message.toString(),isPrint)
fun logd(message:Any,tag: String = TAG,isPrint:Boolean = false) = log(LEVEL.D, tag, message.toString(),isPrint)
fun logi(message:Any,tag: String = TAG,isPrint:Boolean = false) = log(LEVEL.I, tag, message.toString(),isPrint)
fun logw(message:Any,tag: String = TAG,isPrint:Boolean = false) = log(LEVEL.W, tag, message.toString(),isPrint)
fun loge(message:Any,tag: String = TAG,isPrint:Boolean = false) = log(LEVEL.E, tag, message.toString(),isPrint)


private fun log(level: LEVEL, tag: String, message: String,isPrint:Boolean = false) {
    if (isPrint){
        if (!showLog) return
    }
    val tagBuilder = StringBuilder()
    tagBuilder.append(tag)

    if (showStackTrace){
        val stackTrace = Thread.currentThread().stackTrace[5]
        tagBuilder.append(" ${stackTrace.methodName}(${stackTrace.fileName}:${stackTrace.lineNumber})")
    }
    when (level) {
        LEVEL.V -> Log.v(tagBuilder.toString(), message)
        LEVEL.D -> Log.d(tagBuilder.toString(), message)
        LEVEL.I -> Log.i(tagBuilder.toString(), message)
        LEVEL.W -> Log.w(tagBuilder.toString(), message)
        LEVEL.E -> Log.e(tagBuilder.toString(), message)
    }
}
