package me.parade.lib_base.net.converter

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import me.parade.lib_base.net.BaseResponse
import me.parade.lib_base.net.ErrorResponse
import me.parade.lib_base.net.exception.ApiException
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class ErrorHandlerConverterFactory:Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        //仅处理BaseResponse类型,
        if (getRawType(type) != BaseResponse::class.java){
            return null
        }

        // 获取默认的 Converter，用于正常的 BaseResponse 解析
        val delegate: Converter<ResponseBody, *> =
            retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
        return Converter {body:ResponseBody->
            //在你使用 body.string() 读取响应内容后，ResponseBody 的流已经被消耗掉并关闭了,因此需要重新构建ResponseBody
            val json = body.string()
            val errorResponse = try {
                //使用Gson解析的时候虽然字段不匹配，但是不会抛异常，依然会成功解析(字段被赋为默认值)
                val jsonObject = JsonParser.parseString(json).asJsonObject
                if (!jsonObject.has("requestUrl")){
                    null
                }else{
                    val errorResponseType = object :TypeToken<ErrorResponse>(){}.type
                    Gson().fromJson<ErrorResponse>(json,errorResponseType)
                }

            }catch (e:Exception){
                null
            }
            if (errorResponse != null){
                // 如果解析成功，抛出自定义异常
                throw ApiException(errorResponse.requestUrl,errorResponse.errorCode,errorResponse.errorMsg)
            }else{
                // 否则使用默认 Converter 解析为 BaseResponse
                delegate.convert(json.toResponseBody(body.contentType()))
            }
        }
    }
}