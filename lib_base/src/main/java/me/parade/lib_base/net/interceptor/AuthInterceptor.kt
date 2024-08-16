package me.parade.lib_base.net.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().apply {
            newBuilder().header("token", "")
                .header("device", "android")
        }
        return chain.proceed(request)
    }
}