package me.parade.lib_okhttp_log

import android.util.Log
import cn.netdiscovery.http.interceptor.LoggingInterceptor
import me.parade.lib_okhttp_log.log.LogManager
import me.parade.lib_okhttp_log.log.LogProxy

/**
 *
 * @FileName:
 *          com.safframework.http.interceptor.AndroidLoggingInterceptor
 * @author: Tony Shen
 * @date: 2020-09-29 11:18
 * @version: V1.0 <描述当前版本功能>
 */
object AndroidLoggingInterceptor {

    @JvmOverloads
    @JvmStatic
    fun build(isDebug:Boolean = true, hideVerticalLine:Boolean = false, requestTag:String = "Request", responseTag:String = "Response"):LoggingInterceptor {

        init()

        return if (hideVerticalLine) {
            LoggingInterceptor.Builder()
                    .loggable(isDebug)
                    .androidPlatform()
                    .request()
                    .requestTag(requestTag)
                    .response()
                    .responseTag(responseTag)
                    .hideVerticalLine()// 隐藏竖线边框
                    .build()
        } else {
            LoggingInterceptor.Builder()
                    .loggable(isDebug) 
                    .androidPlatform()
                    .request()
                    .requestTag(requestTag)
                    .response()
                    .responseTag(responseTag)
//                    .hideVerticalLine()// 隐藏竖线边框
                    .build()
        }
    }

    private fun init() {

        LogManager.logProxy(object : LogProxy {
            override fun e(tag: String, msg: String) {
                Log.e(tag,msg)
            }

            override fun w(tag: String, msg: String) {
                Log.w(tag,msg)
            }

            override fun i(tag: String, msg: String) {
                Log.i(tag,msg)
            }

            override fun d(tag: String, msg: String) {
                Log.d(tag,msg)
            }
        })
    }
}
