package me.parade.lib_image

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.gif.GifDrawable

class GlideImageLoader(private val context: Context) : ImageLoader {

    override fun loadImage(url: String, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .into(imageView)
    }

    override fun loadImage(url: String, imageView: ImageView, options: ImageLoaderOptions) {
        val requestBuilder = Glide.with(context)
            .`as`(if (options.isGif) GifDrawable::class.java else Drawable::class.java)
            .load(url)

        // 设置占位图和错误图
        options.placeholder?.let { requestBuilder.placeholder(it) }
        options.error?.let { requestBuilder.error(it) }

        // 设置圆角
        options.cornerRadius?.let {
          requestBuilder.transform(CenterCrop(),RoundedCorners(it.toInt()))
        }

        // 设置缓存策略
       val strategy =  when (options.cacheStrategy) {
            CacheStrategy.NONE -> requestBuilder.diskCacheStrategy(DiskCacheStrategy.NONE)
            CacheStrategy.DATA -> requestBuilder.diskCacheStrategy(DiskCacheStrategy.DATA)
            CacheStrategy.RESOURCE -> requestBuilder.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            CacheStrategy.ALL -> requestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL)
        }

        // 设置是否加载 GIF
        if (options.isGif) {

        }

        requestBuilder.into(imageView)
    }
}
