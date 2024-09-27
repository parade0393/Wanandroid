package me.parade.wanandroid.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.parade.lib_base.base.BaseViewModel
import me.parade.lib_base.net.RetrofitManager
import me.parade.lib_common.ext.logd
import me.parade.wanandroid.net.model.Banner
import me.parade.wanandroid.net.service.HomeService

class HomeViewModel : BaseViewModel() {

    private val _banners = MutableLiveData<List<Banner>>(emptyList())
    val banners:LiveData<List<Banner>> = _banners



    /**
     * 这里的requestCall是一个lambda表达式，代表一个函数
     */
    fun getBannerData(){
        "getBannerData".logd()
        launchRequest(
            requestCall = { RetrofitManager.create(HomeService::class.java).getBanner()},
        ){data->
            data?.let { result->
                _banners.value = result
            }
        }
    }

}