package me.parade.wanandroid.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.parade.lib_base.base.BaseViewModel
import me.parade.lib_base.net.RetrofitManager
import me.parade.wanandroid.net.model.ArticleInfo
import me.parade.wanandroid.net.model.PageResponse
import me.parade.wanandroid.net.service.HomeService

class ProfileVm:BaseViewModel() {

    private val _articleList = MutableLiveData<PageResponse<ArticleInfo>>()
    val articleList: LiveData<PageResponse<ArticleInfo>> = _articleList
    private val service by lazy { RetrofitManager.create(HomeService::class.java) }

    fun getArticleList(page:Int = 1,pageSize:Int = 10){
        launchRequest(
            requestCall = {service.getAnswerPageList(page,pageSize)}
        ){data->
            data?.let {
                _articleList.value = it
            }
        }
    }
}