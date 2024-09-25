package me.parade.wanandroid.net.service

import me.parade.lib_base.net.BaseResponse
import me.parade.wanandroid.net.model.ArticleList
import me.parade.wanandroid.net.model.Banner
import me.parade.wanandroid.net.model.TestBean
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeService {
    @GET("user")
    suspend fun testApi():BaseResponse<TestBean>

    @GET("banner/json")
    suspend fun getBanner():BaseResponse<List<Banner>>

    /**
     * 首页资讯
     * @param page    页码
     * @param pageSize 每页数量
     */
    @GET("article/list/{page}/json")
    suspend fun getHomeList(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int
    ): BaseResponse<ArticleList>
}