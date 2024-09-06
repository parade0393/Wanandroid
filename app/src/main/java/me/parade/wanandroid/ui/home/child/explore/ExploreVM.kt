package me.parade.wanandroid.ui.home.child.explore

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import me.parade.lib_base.base.BaseViewModel
import me.parade.lib_base.net.RetrofitManager
import me.parade.wanandroid.net.model.Banner
import me.parade.wanandroid.net.model.TestBean
import me.parade.wanandroid.net.service.HomeService

class ExploreVM:BaseViewModel() {
    private val _data = MutableSharedFlow<TestBean>()
    val data = _data.asSharedFlow()

    private val _banners = MutableSharedFlow<List<Banner>>()
    val banners = _banners.asSharedFlow()

    private val service by lazy { RetrofitManager.create(HomeService::class.java) }


    fun getBannerData(){
        val banners = launchUI{service.getBanner()}
    }
}