package me.parade.lib_common.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ScrollView
import androidx.recyclerview.widget.RecyclerView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

object ViewToBitmapUtil {
    
    /**
     * 将View转换为Bitmap并在使用完后自动回收
     * 使用示例:
     * convertToImage(view) { bitmap ->
     *     // 在这个作用域内使用bitmap
     *     imageView.setImageBitmap(bitmap)
     * }
     */
    inline fun <T> convertToImage(
        view: View,
        scale: Float = 1f,
        config: Bitmap.Config = Bitmap.Config.RGB_565,
        crossinline block: (Bitmap) -> T
    ): T {
        val bitmap = convert(view, scale, config)
        try {
            return block(bitmap)
        } finally {
            bitmap.recycle()
        }
    }

    /**
     * 截取View某个区域并在使用完后自动回收
     */
    inline fun <T> captureRegionToImage(
        view: View,
        rect: Rect,
        config: Bitmap.Config = Bitmap.Config.ARGB_8888,
        crossinline block: (Bitmap) -> T
    ): T {
        val bitmap = captureRegion(view, rect, config)
        try {
            return block(bitmap)
        } finally {
            bitmap.recycle()
        }
    }

    /**
     * 将ScrollView转换为Bitmap并在使用完后自动回收
     */
    inline fun <T> convertScrollViewToImage(
        scrollView: ScrollView,
        config: Bitmap.Config = Bitmap.Config.ARGB_8888,
        crossinline block: (Bitmap) -> T
    ): T {
        val bitmap = convertScrollView(scrollView, config)
        try {
            return block(bitmap)
        } finally {
            bitmap.recycle()
        }
    }

    /**
     * 将RecyclerView转换为Bitmap并在使用完后自动回收
     */
    inline fun <T> convertRecyclerViewToImage(
        recyclerView: RecyclerView,
        config: Bitmap.Config = Bitmap.Config.ARGB_8888,
        crossinline block: (Bitmap) -> T
    ): T {
        val bitmap = convertRecyclerView(recyclerView, config)
        try {
            return block(bitmap)
        } finally {
            bitmap.recycle()
        }
    }

    /**
     * 转换为Drawable并自动回收Bitmap
     */
    fun View.toDrawable(
        scale: Float = 1f,
        config: Bitmap.Config = Bitmap.Config.ARGB_8888
    ): BitmapDrawable {
        return convertToImage(this, scale, config) { bitmap ->
            BitmapDrawable(resources, bitmap)
        }
    }

    /**
     * 保存View为图片文件并自动回收Bitmap
     */
    fun View.saveAsImage(
        file: File,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
        quality: Int = 100
    ): Boolean {
        return convertToImage(this) { bitmap ->
            try {
                file.outputStream().use { stream ->
                    bitmap.compress(format, quality, stream)
                }
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }
    }

//    /**
//     * 转换为Base64字符串并自动回收Bitmap
//     */
//    fun View.toBase64(
//        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
//        quality: Int = 100
//    ): String {
//        return convertToImage(this) { bitmap ->
//            ByteArrayOutputStream().use { stream ->
//                bitmap.compress(format, quality, stream)
//                Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
//            }
//        }
//    }

    // 内部使用的原始转换方法
     fun convert(
        view: View,
        scale: Float = 1f,
        config: Bitmap.Config = Bitmap.Config.ARGB_8888
    ): Bitmap {
        if (view.width == 0 || view.height == 0) {
            view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        }

        val width = (view.width * scale).toInt()
        val height = (view.height * scale).toInt()
        val bitmap = Bitmap.createBitmap(width, height, config)
        val canvas = Canvas(bitmap)
        
        if (scale != 1f) {
            canvas.scale(scale, scale)
        }
        
        view.draw(canvas)
        return bitmap
    }

    /**
     * 将ScrollView完整转换为Bitmap
     * @param scrollView 需要转换的ScrollView
     * @param config Bitmap的配置
     * @return Bitmap
     */
    fun convertScrollView(
        scrollView: ScrollView,
        config: Bitmap.Config = Bitmap.Config.ARGB_8888
    ): Bitmap {
        var height = 0
        // 获取ScrollView的完整高度
        for (i in 0 until scrollView.childCount) {
            height += scrollView.getChildAt(i).height
        }

        // 创建完整大小的Bitmap
        val bitmap = Bitmap.createBitmap(
            scrollView.width,
            height,
            config
        )

        val canvas = Canvas(bitmap)
        scrollView.draw(canvas)

        return bitmap
    }
    /**
     * 将RecyclerView完整转换为Bitmap
     * @param recyclerView 需要转换的RecyclerView
     * @param config Bitmap的配置
     * @return Bitmap
     */
    fun convertRecyclerView(
        recyclerView: RecyclerView,
        config: Bitmap.Config = Bitmap.Config.ARGB_8888
    ): Bitmap {
        val adapter = recyclerView.adapter ?: return convert(recyclerView)

        var height = 0
        val paint = Paint()

        // 测量每个item的高度
        for (i in 0 until adapter.itemCount) {
            val holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i))
            adapter.onBindViewHolder(holder, i)
            holder.itemView.measure(
                View.MeasureSpec.makeMeasureSpec(recyclerView.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            holder.itemView.layout(0, 0, holder.itemView.measuredWidth, holder.itemView.measuredHeight)
            height += holder.itemView.measuredHeight
        }

        val bitmap = Bitmap.createBitmap(recyclerView.width, height, config)
        val canvas = Canvas(bitmap)

        var currentHeight = 0
        // 绘制每个item
        for (i in 0 until adapter.itemCount) {
            val holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i))
            adapter.onBindViewHolder(holder, i)
            holder.itemView.measure(
                View.MeasureSpec.makeMeasureSpec(recyclerView.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            holder.itemView.layout(0, currentHeight, holder.itemView.measuredWidth,
                currentHeight + holder.itemView.measuredHeight)
            holder.itemView.draw(canvas)
            currentHeight += holder.itemView.measuredHeight
        }

        return bitmap
    }

    /**
     * 截取View的某个区域
     * @param view 需要截取的View
     * @param rect 截取的区域
     * @param config Bitmap的配置
     * @return Bitmap
     */
    fun captureRegion(
        view: View,
        rect: Rect,
        config: Bitmap.Config = Bitmap.Config.RGB_565
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(
            rect.width(),
            rect.height(),
            config
        )
        val canvas = Canvas(bitmap)

        // 平移画布到指定区域
        canvas.translate(-rect.left.toFloat(), -rect.top.toFloat())
        view.draw(canvas)

        return bitmap
    }
}