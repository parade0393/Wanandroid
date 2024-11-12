package me.parade.lib_common.recy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView通用分割线
 * @param context 上下文
 * @param orientation 列表方向 [LinearLayoutManager.HORIZONTAL] 或 [LinearLayoutManager.VERTICAL]
 * @param dividerSize 分割线宽度（水平列表）或高度（垂直列表），单位dp
 * @param dividerColor 分割线颜色
 * @param marginStart 分割线起始边距，单位dp
 * @param marginEnd 分割线结束边距，单位dp
 * @param skipLastDivider 是否跳过最后一个item的分割线
 */
class CustomItemDecoration @JvmOverloads constructor(
    context: Context,
    private val orientation: Int = LinearLayoutManager.VERTICAL,
    dividerSizeDp: Float = 1f,
    @ColorInt dividerColor: Int = 0xFFE0E0E0.toInt(),
    marginStartDp: Float = 0f,
    marginEndDp: Float = 0f,
    private val skipLastDivider: Boolean = false
) : RecyclerView.ItemDecoration() {

    private val density = context.resources.displayMetrics.density
    private val dividerSize = (dividerSizeDp * density).toInt()
    private val marginStart = (marginStartDp * density).toInt()
    private val marginEnd = (marginEndDp * density).toInt()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = dividerColor
        style = Paint.Style.FILL
    }

    init {
        require(orientation == LinearLayoutManager.VERTICAL || orientation == LinearLayoutManager.HORIZONTAL) {
            "Invalid orientation. It should be either LinearLayoutManager.VERTICAL or LinearLayoutManager.HORIZONTAL"
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0
        
        // 如果设置跳过最后一个分割线，且当前是最后一个item，则不设置间距
        if (skipLastDivider && position == itemCount - 1) {
            return
        }

        if (orientation == LinearLayoutManager.VERTICAL) {
            outRect.bottom = dividerSize
        } else {
            outRect.right = dividerSize
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (orientation == LinearLayoutManager.VERTICAL) {
            drawVertical(canvas, parent)
        } else {
            drawHorizontal(canvas, parent)
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft + marginStart
        val right = parent.width - parent.paddingRight - marginEnd

        val childCount = if (skipLastDivider) {
            parent.childCount - 1
        } else {
            parent.childCount
        }

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            val itemCount = parent.adapter?.itemCount ?: 0
            
            // 如果设置跳过最后一个分割线，且当前是最后一个item，则跳过绘制
            if (skipLastDivider && position == itemCount - 1) {
                continue
            }

            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + dividerSize

            canvas.drawRect(
                left.toFloat(),
                top.toFloat(),
                right.toFloat(),
                bottom.toFloat(),
                paint
            )
        }
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop + marginStart
        val bottom = parent.height - parent.paddingBottom - marginEnd

        val childCount = if (skipLastDivider) {
            parent.childCount - 1
        } else {
            parent.childCount
        }

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            val itemCount = parent.adapter?.itemCount ?: 0
            
            // 如果设置跳过最后一个分割线，且当前是最后一个item，则跳过绘制
            if (skipLastDivider && position == itemCount - 1) {
                continue
            }

            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + dividerSize

            canvas.drawRect(
                left.toFloat(),
                top.toFloat(),
                right.toFloat(),
                bottom.toFloat(),
                paint
            )
        }
    }
}