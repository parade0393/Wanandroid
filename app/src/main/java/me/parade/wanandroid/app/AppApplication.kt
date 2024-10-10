package me.parade.wanandroid.app

import android.app.Application
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import me.parade.lib_image.GlideImageLoader
import me.parade.lib_image.ImageLoaderManager

class AppApplication :Application(){

    override fun onCreate() {
        super.onCreate()
        //初始化图片加载使用Glide
        ImageLoaderManager.init(GlideImageLoader(this))

        //SmartRefreshLayout
        SmartRefreshLayout.setDefaultRefreshHeaderCreator{context,layout->
            ClassicsHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            ClassicsFooter(context)
        }
    }
}