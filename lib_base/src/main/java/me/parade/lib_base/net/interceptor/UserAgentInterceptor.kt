package me.parade.lib_base.net.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class UserAgentInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest: Request = originalRequest.newBuilder()
            .header("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 14; 23049RAD8C Build/UKQ1.230804.001)") // 设置与 HttpURLConnection 相同的 User-Agent
            .build()
        return  chain.proceed(newRequest)
    }
}