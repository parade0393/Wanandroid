package me.parade.wanandroid.ui.home.child.explore

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import me.parade.lib_base.base.BaseViewModel
import me.parade.lib_base.download.DownloadHelper
import me.parade.lib_base.download.DownloadResult
import me.parade.lib_base.ext.logd
import me.parade.lib_base.helper.requestFlowResponse
import me.parade.lib_base.net.RetrofitManager
import me.parade.wanandroid.net.model.TestBean
import me.parade.wanandroid.net.service.HomeService

class ExploreVM:BaseViewModel() {
    private val _data = MutableSharedFlow<TestBean>()
    val data = _data.asSharedFlow()

    private val _down = MutableStateFlow<DownloadResult>(DownloadResult.Idle)
    val down:StateFlow<DownloadResult> = _down

    fun getData(){
        viewModelScope.launch {
           requestFlowResponse(
               requestBlock = {RetrofitManager.create(HomeService::class.java).testApi()}
           ).collect{ res->
               _data.emit(res.data)
           }
        }
    }

    fun  downLoad(context: Context){
        viewModelScope.launch {
            DownloadHelper.downloadFile(context,"http://218.29.175.190:8090/zwtjava//file/6447/other/20240830/13703937803/164917729/10-20240830164917729.jpg")
                .collect{ result->
                    _down.emit(result)
                }
        }
    }
}