package me.parade.wanandroid.ui.net.service

import me.parade.lib_base.net.BaseResponse
import me.parade.wanandroid.ui.net.model.TestBean
import retrofit2.http.GET

interface HomeService {
    @GET("user")
    suspend fun testApi():BaseResponse<TestBean>
}