package me.parade.lib_image

import android.content.Context
import android.widget.ImageView
import coil.decode.GifDecoder
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation

class CoilImageLoader(private val context: Context) : ImageLoader {

    override fun loadImage(url: String, imageView: ImageView) {
        imageView.load(url)
    }

    override fun loadImage(url: String, imageView: ImageView, options: ImageLoaderOptions) {
        imageView.load(url) {
            // 设置占位图和错误图
            options.placeholder?.let { placeholder(it) }
            options.error?.let { error(it) }

            // 设置圆角
            options.cornerRadius?.let {
                transformations(RoundedCornersTransformation(it))
            }
            if (options.isCircle){
                transformations(CircleCropTransformation())
            }

            // 设置缓存策略
            when (options.cacheStrategy) {
                CacheStrategy.NONE -> diskCachePolicy(CachePolicy.DISABLED)
                CacheStrategy.DATA -> diskCachePolicy(CachePolicy.READ_ONLY)
                CacheStrategy.RESOURCE -> diskCachePolicy(CachePolicy.WRITE_ONLY)
                CacheStrategy.ALL -> diskCachePolicy(CachePolicy.ENABLED)
            }

            // 设置是否加载 GIF
            if (options.isGif) {
                decoderFactory(GifDecoder.Factory())
            }
        }
    }
}
