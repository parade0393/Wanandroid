package me.parade.lib_base.net

import me.parade.lib_base.net.converter.ErrorHandlerConverterFactory
import me.parade.lib_base.net.interceptor.AuthInterceptor
import me.parade.lib_base.net.interceptor.ExceptionTransformInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

object RetrofitManager {
    private val serviceMap:ConcurrentHashMap<String,Any> = ConcurrentHashMap<String,Any>()

    //region 常量配置
    private const val BASE_URL = "http://192.168.49.110:3000/api/"
    private const val TIME_OUT_SECONDS = 30
    //endregion常量配置


    private val okHttpClient:OkHttpClient by lazy {
        OkHttpClient().newBuilder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(ExceptionTransformInterceptor())
            .connectTimeout(TIME_OUT_SECONDS.toLong(),TimeUnit.SECONDS)
            .readTimeout(TIME_OUT_SECONDS.toLong(),TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT_SECONDS.toLong(),TimeUnit.SECONDS)
            .build()
    }

    private val retrofit:Retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(ErrorHandlerConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Suppress("UNCHECKED_CAST")
    fun <T> create(serviceClass:Class<T>) : T{
        return serviceMap.getOrPut(serviceClass.name){
            retrofit.create(serviceClass)
        } as T
    }
}