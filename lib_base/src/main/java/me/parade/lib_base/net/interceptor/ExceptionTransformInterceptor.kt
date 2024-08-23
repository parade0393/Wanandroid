package me.parade.lib_base.net.interceptor

import com.google.gson.Gson
import me.parade.lib_base.net.BaseResponse
import me.parade.lib_base.net.ErrorResponse
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException

class ExceptionTransformInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestUrl = request.url.toString()
        val originProtocol = chain.connection()?.protocol()?:Protocol.HTTP_1_1
        return try {
            val response = chain.proceed(request)
            //如果response code是200,即服务端成功响应，但是响应体是空，即ContentLength是0，这里的body是空字符串
            val body = response.body?.string()
            body?.let { bodyStr->
                // 尝试解析为 BaseResponse，如果body是空字符串，那这里不会有异常，但baseResponse是null
                val baseResponse = try {
                    Gson().fromJson(bodyStr,BaseResponse::class.java)
                }catch (e:Exception){
                    // 如果解析失败,则直接返回原始响应体
                    return response.newBuilder().body(bodyStr.toResponseBody(response.body?.contentType())).build()
                }
                if(baseResponse == null){
                    return response.newBuilder().body(
                        Gson().toJson(BaseResponse(data = Any()))
                            .toResponseBody(response.body?.contentType())
                    ).build()
                }
                if (baseResponse.errorCode != 0){
                    //业务异常
                    return response.newBuilder().body(Gson().toJson(ErrorResponse(requestUrl,baseResponse.errorCode,baseResponse.errorMsg)).toResponseBody(response.body?.contentType())).build()
                }
                response.newBuilder().body(bodyStr.toResponseBody(response.body?.contentType())).build()

            }?:response.newBuilder().body(
                Gson().toJson(BaseResponse(data = Any()))
                    .toResponseBody(response.body?.contentType())
            ).build()
        }catch (e:Exception){
            val errorResponse = ErrorResponse(requestUrl,
                errorCode = when(e) {
                    is  HttpException -> e.code()
                    else -> 500
                },
                errorMsg = e.message?:"未知异常"
                )
            return Response.Builder()
                .protocol(originProtocol)
                .code(200)
                .message("Ok")
                .request(request)
                .body(Gson().toJson(errorResponse).toResponseBody("application/json; charset=UTF-8".toMediaTypeOrNull()))
                .build()
        }
    }
}