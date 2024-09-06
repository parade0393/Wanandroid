package me.parade.wanandroid.ui.home.child.explore

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import me.parade.lib_base.base.BaseViewModel
import me.parade.wanandroid.net.model.Banner
import me.parade.wanandroid.net.model.TestBean

class ExploreVM:BaseViewModel() {
    private val _data = MutableSharedFlow<TestBean>()
    val data = _data.asSharedFlow()

    private val _banners = MutableSharedFlow<List<Banner>>()
    val banners = _banners.asSharedFlow()


}