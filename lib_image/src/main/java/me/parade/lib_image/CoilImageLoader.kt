package me.parade.lib_image

import android.content.Context
import android.widget.ImageView
import coil.decode.GifDecoder
import coil.load
import coil.request.CachePolicy
import coil.size.Size
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation

class CoilImageLoader(private val context: Context) : ImageLoader {

    override fun loadImage(url: String, imageView: ImageView) {
        imageView.load(url)
    }

    override fun loadImage(url: String, imageView: ImageView, config: ImageLoaderOptions) {
        imageView.load(url) {
            // 设置占位图和错误图
            config.placeholder?.let { placeholder(it) }
            config.error?.let { error(it) }

            // 设置圆角
            config.cornerRadius?.let {
                transformations(RoundedCornersTransformation(it))


            }
            if (config.isCircle){
                transformations(CircleCropTransformation())
            }

            //指定不缩放,否在再处理圆角和圆形图片后尺寸感觉比较怪
            size(Size.ORIGINAL)

            // 设置缓存策略
            when (config.cacheStrategy) {
                CacheStrategy.NONE -> diskCachePolicy(CachePolicy.DISABLED)
                CacheStrategy.DATA -> diskCachePolicy(CachePolicy.READ_ONLY)
                CacheStrategy.RESOURCE -> diskCachePolicy(CachePolicy.WRITE_ONLY)
                CacheStrategy.ALL -> diskCachePolicy(CachePolicy.ENABLED)
            }

            crossfade(true)
            // 设置是否加载 GIF
            if (config.isGif) {
                decoderFactory(GifDecoder.Factory())
            }
        }
    }
}
