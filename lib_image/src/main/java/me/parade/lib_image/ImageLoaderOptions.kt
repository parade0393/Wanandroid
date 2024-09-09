package me.parade.lib_image

data class ImageLoaderOptions(
   var placeholder: Int? = null,       // 占位图
   var error: Int? = null,             // 错误图
   var cornerRadius: Float? = null,    // 圆角
   var isCircle:Boolean = false,   //是否圆形图片
   var cacheStrategy: CacheStrategy = CacheStrategy.ALL, // 缓存策略
   var isGif: Boolean = false          // 是否加载 GIF
)

enum class CacheStrategy {
    ALL, NONE, DATA, RESOURCE
}

object DefaultImageLoaderOptions {
    val DEFAULT = ImageLoaderOptions(
        placeholder = R.drawable.baseline_image_24_placeholder,
        error = R.drawable.baseline_broken_image_24_error,
        cornerRadius = 0f,
        cacheStrategy = CacheStrategy.ALL,
        isGif = false
    )
}

