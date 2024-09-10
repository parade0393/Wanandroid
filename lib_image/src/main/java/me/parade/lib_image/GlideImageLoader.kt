package me.parade.lib_image

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class GlideImageLoader(private val context: Context) : ImageLoader {

    override fun loadImage(url: String, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .into(imageView)
    }

    @SuppressLint("CheckResult")
    override fun loadImage(url: String, imageView: ImageView, config: ImageLoaderOptions) {

        val requestOptions = RequestOptions().also {options->
            config.cornerRadius?.let { radius->
                options.transform(RoundedCorners(radius.toInt()))
            }

            if (config.isCircle){
                options.circleCrop()
            }

            config.placeholder?.let { options.placeholder(it) }
            config.error?.let { options.error(it) }

            when (config.cacheStrategy) {
                CacheStrategy.NONE -> options.diskCacheStrategy(DiskCacheStrategy.NONE)
                CacheStrategy.DATA -> options.diskCacheStrategy(DiskCacheStrategy.DATA)
                CacheStrategy.RESOURCE -> options.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                CacheStrategy.ALL -> options.diskCacheStrategy(DiskCacheStrategy.ALL)
            }
        }

        if (config.isGif){
            Glide.with(context)
                .asGif()
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())//供的一个过渡效果,用于在图片加载过程中实现平滑的淡入淡出效果。
                .into(imageView)
        }else{
            Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        }
    }


}
