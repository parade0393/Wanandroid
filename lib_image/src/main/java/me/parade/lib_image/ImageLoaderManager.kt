package me.parade.lib_image

import android.widget.ImageView

object ImageLoaderManager {
    private var imageLoader: ImageLoader? = null

    fun init(imageLoader: ImageLoader) {
        this.imageLoader = imageLoader
    }



    fun loadImage(url: String, imageView: ImageView, options: ImageLoaderOptions = DefaultImageLoaderOptions.DEFAULT,loader: ImageLoader? = null) {
        val finalOptions = DefaultImageLoaderOptions.DEFAULT.copy().apply {
            options.placeholder?.let { placeholder = it }
            options.error?.let { error = it }
            options.cornerRadius?.let { cornerRadius = it }
            cacheStrategy = options.cacheStrategy
            isGif = options.isGif
            isCircle = options.isCircle
            cacheStrategy = options.cacheStrategy
        }
        (loader ?: imageLoader)?.loadImage(url,imageView,finalOptions)
    }
}
