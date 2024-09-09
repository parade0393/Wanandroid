package me.parade.lib_image

import android.widget.ImageView

object ImageLoaderManager {
    private var imageLoader: ImageLoader? = null

    fun init(imageLoader: ImageLoader) {
        this.imageLoader = imageLoader
    }

    fun loadImage(url: String, imageView: ImageView) {
        imageLoader?.loadImage(url, imageView)
    }

    fun loadImage(url: String, imageView: ImageView, options: ImageLoaderOptions) {
        imageLoader?.loadImage(url, imageView, options)
    }
}
