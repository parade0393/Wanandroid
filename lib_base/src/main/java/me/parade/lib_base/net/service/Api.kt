package me.parade.lib_base.net.service

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface Api {
    //下载
    @FileDownload
    @Streaming
    @GET
    suspend fun downloadFile(@Url fileUrl: String): ResponseBody
}