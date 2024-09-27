package me.parade.wanandroid.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.parade.lib_base.base.BaseViewModel
import me.parade.lib_base.net.RetrofitManager
import me.parade.wanandroid.net.model.ArticleInfo
import me.parade.wanandroid.net.model.Banner
import me.parade.wanandroid.net.model.PageResponse
import me.parade.wanandroid.net.service.HomeService

class HomeViewModel : BaseViewModel() {

    private val _banners = MutableLiveData<List<Banner>>(emptyList())
    val banners:LiveData<List<Banner>> = _banners

    private val _articleList = MutableLiveData<PageResponse<ArticleInfo>>();
    val articleList:LiveData<PageResponse<ArticleInfo>> = _articleList

    private val homeService by lazy { RetrofitManager.create(HomeService::class.java) }

    /**
     * 这里的requestCall是一个lambda表达式，代表一个函数
     */
    fun getBannerData(){
        launchRequest(
            requestCall = { RetrofitManager.create(HomeService::class.java).getBanner()},
        ){data->
            data?.let { result->
                _banners.value = result
            }
        }
    }



}