package me.parade.lib_base.net.interceptor

import com.google.gson.Gson
import me.parade.lib_base.net.BaseResponse
import me.parade.lib_base.net.ErrorResponse
import me.parade.lib_base.net.service.FileDownload
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Invocation

class ExceptionTransformInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestUrl = request.url.toString()
        val originProtocol = chain.connection()?.protocol() ?: Protocol.HTTP_1_1
        return try {
            val response = chain.proceed(request)

            // 检查是否有 @FileDownload 注解
            val isDownloadRequest = request.tag(Invocation::class.java)
                ?.method()
                ?.annotations
                ?.any { it is FileDownload } == true
            // 检查是否为文件下载请求,这里如果不判断，走下面把bodyStr再相应出去可能会破坏响应体结构，导致图片等无法显示
            if (isDownloadRequest) {
                // 对于文件下载，直接返回原始响应
                return response
            }
            //如果response code是200,即服务端成功响应，但是响应体是空，即ContentLength是0，这里的body是空字符串
            val body = response.body?.string()
//            val body = response.peekBody(Long.MAX_VALUE).string()
            body?.let { bodyStr ->
                // 尝试解析为 BaseResponse，如果body是空字符串，那这里不会有异常，但baseResponse是null
                val baseResponse = try {
                    Gson().fromJson(bodyStr, BaseResponse::class.java)
                } catch (e: Exception) {
                    // 如果解析失败,则直接返回原始响应体
                    return response.newBuilder()
                        .body(bodyStr.toResponseBody(response.body?.contentType())).build()
                }
                if (baseResponse == null) {
                    return response.newBuilder().body(
                        Gson().toJson(BaseResponse(data = Any()))
                            .toResponseBody(response.body?.contentType())
                    ).build()
                }
                if (baseResponse.errorCode != 0) {
                    //业务异常
                    return response.newBuilder().body(
                        Gson().toJson(
                            ErrorResponse(
                                requestUrl,
                                baseResponse.errorCode,
                                baseResponse.errorMsg
                            )
                        ).toResponseBody(response.body?.contentType())
                    ).build()
                }
                response.newBuilder().body(bodyStr.toResponseBody(response.body?.contentType()))
                    .build()

            } ?: response.newBuilder().body(
                Gson().toJson(BaseResponse(data = Any()))
                    .toResponseBody(response.body?.contentType())
            ).build()
        } catch (e: Exception) {
            val errorResponse = ErrorResponse(
                requestUrl,
                errorCode = when (e) {
                    is HttpException -> e.code()
                    else -> 500
                },
                errorMsg = e.message ?: "未知异常"
            )
            return Response.Builder()
                .protocol(originProtocol)
                .code(200)
                .message("Ok")
                .request(request)
                .body(
                    Gson().toJson(errorResponse)
                        .toResponseBody("application/json; charset=UTF-8".toMediaTypeOrNull())
                )
                .build()
        }
    }

    private fun isFileDownloadRequest(response: Response): Boolean {
        // 根据URL或者请求头来判断是否为文件下载请求
        val contentType = response.header("Content-Type")
        return when {
            // 检查Content-Type是否指示二进制内容或特定文件类型
            contentType?.startsWith("image/") == true -> true
            contentType?.startsWith("audio/") == true -> true
            contentType?.startsWith("video/") == true -> true
            contentType?.startsWith("application/octet-stream") == true -> true
            contentType?.startsWith("application/pdf") == true -> true
            contentType?.startsWith("application/") == true -> true
            // 检查Content-Disposition头，通常用于指示文件下载
            response.header("Content-Disposition")?.contains("attachment") == true -> true
            // 检查响应大小，如果超过某个阈值，可能是文件下载
            response.body?.contentLength()?.let { it > 1024 * 1024 } == true -> true // 假设大于1MB就是文件
            else -> false
        }
    }
}