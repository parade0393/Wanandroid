package me.parade.lib_image

data class ImageLoaderOptions(
    val placeholder: Int? = null,       // 占位图
    val error: Int? = null,             // 错误图
    val cornerRadius: Float? = null,    // 圆角
    val cacheStrategy: CacheStrategy = CacheStrategy.ALL, // 缓存策略
    val isGif: Boolean = false          // 是否加载 GIF
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

