package me.parade.wanandroid.ui.home.child.explore

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.parade.lib_base.base.BaseViewModel
import me.parade.lib_base.net.RetrofitManager
import me.parade.wanandroid.net.model.Banner
import me.parade.wanandroid.net.service.HomeService

class ExploreVM:BaseViewModel() {


    private val _banners = MutableStateFlow<List<Banner>>(emptyList())
    val banners = _banners.asStateFlow()

    private val service by lazy { RetrofitManager.create(HomeService::class.java) }


    /**
     * 这里的requestCall是一个lambda表达式，代表一个函数
     */
    fun getBannerData(){
        launchRequest(
            requestCall = {RetrofitManager.create(HomeService::class.java).getBanner()},
        ){data->
            data?.let { result->
                _banners.value = result
            }
        }
    }
}