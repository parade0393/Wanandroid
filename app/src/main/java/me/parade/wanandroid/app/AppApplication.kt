package me.parade.wanandroid.app

import android.app.Application
import me.parade.lib_image.GlideImageLoader
import me.parade.lib_image.ImageLoaderManager

class AppApplication :Application(){

    override fun onCreate() {
        super.onCreate()
        //初始化图片加载使用Glide
        ImageLoaderManager.init(GlideImageLoader(this))
    }
}