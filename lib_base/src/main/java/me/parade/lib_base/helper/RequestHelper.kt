package me.parade.lib_base.helper

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import me.parade.lib_base.ext.loge
import me.parade.lib_base.net.BaseResponse
import me.parade.lib_base.net.exception.ApiException


/**
 * 通过flow执行请求，需要在协程作用域中执行
 * @param errorBlock 错误回调
 * @param requestCall 执行的请求
 * @param showLoading 开启和关闭加载框
 * @return 请求结果直接返回[BaseResponse.data]
 */
suspend fun <T> requestFlow(
    requestCall: suspend () -> BaseResponse<T>,
    errorBlock: ((Int?, String?) -> Unit)? = null,
    showLoading: ((Boolean) -> Unit)? = null
): T? {
    var data: T? = null
    val flow = requestFlowResponse(requestCall,errorBlock,showLoading)
    //7.调用collect获取emit()回调的结果，就是请求最后的结果
    flow.collect {
        data = it.data
    }
    return data
}


/**
 * @param requestBlock 执行的请求
 * @param errorBlock 异常回调
 * @param showLoading dialog事件
 * @param dispatcher 请求的线程
 * @return 返回flow包装的[BaseResponse]
 */
suspend fun <T> requestFlowResponse(
    requestBlock:suspend ()->BaseResponse<T>,
    errorBlock:((Int?,String?) ->Unit)? = null,
    showLoading: ((Boolean) -> Unit)? = null,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
):Flow<BaseResponse<T>>{
    val flow = flow {
        val response = requestBlock()
        if(!response.isSuccess()){
            errorBlock?.invoke(response.errorCode,response.errorMsg)
        }
        //2.发送网络请求结果回调
        emit(response)
    }
        .flowOn(dispatcher)
        .onStart {
            showLoading?.invoke(true)
        }
        .catch { e->
            val code = if (e is ApiException) e.code else 500
            val msg = if (e is ApiException) e.msg else e.message?:"未知异常"
            loge(msg)
            errorBlock?.invoke(code,msg)
        }
        .onCompletion {
            showLoading?.invoke(false)
        }
    return flow
}