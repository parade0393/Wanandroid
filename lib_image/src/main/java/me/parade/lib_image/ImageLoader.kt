package me.parade.lib_image

import android.widget.ImageView

interface ImageLoader {
    fun loadImage(url: String, imageView: ImageView)
    fun loadImage(url: String, imageView: ImageView, options: ImageLoaderOptions = DefaultImageLoaderOptions.DEFAULT)
}
