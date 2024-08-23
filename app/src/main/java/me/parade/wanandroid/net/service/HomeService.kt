package me.parade.wanandroid.net.service

import me.parade.lib_base.net.BaseResponse
import me.parade.wanandroid.net.model.TestBean
import retrofit2.http.GET

interface HomeService {
    @GET("user")
    suspend fun testApi():BaseResponse<TestBean>
}