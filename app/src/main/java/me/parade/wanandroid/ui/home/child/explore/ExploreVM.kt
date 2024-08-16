package me.parade.wanandroid.ui.home.child.explore

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import me.parade.lib_base.base.BaseViewModel
import me.parade.lib_base.helper.requestFlowResponse
import me.parade.lib_base.net.RetrofitManager
import me.parade.wanandroid.ui.net.model.TestBean
import me.parade.wanandroid.ui.net.service.HomeService

class ExploreVM:BaseViewModel() {
    private val _data = MutableStateFlow(TestBean())
    private val data = _data.asSharedFlow()

    fun getData(){
        viewModelScope.launch {
           requestFlowResponse(
               requestBlock = {RetrofitManager.create(HomeService::class.java).testApi()}
           ).collect{

           }
        }
    }
}