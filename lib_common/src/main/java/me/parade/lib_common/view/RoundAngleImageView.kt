package me.parade.lib_common.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import me.parade.lib_common.R
import kotlin.math.max

/**
 * @author : parade
 * date : 2021/5/7
 * description : 支持分别设置图片的四个圆角
 *
 */
class RoundAngleImageView:AppCompatImageView {
    private var radius: Int = 0
    private var leftTopRadius: Int = 0
    private var rightTopRadius: Int = 0
    private var rightBottomRadius: Int = 0
    private var leftBottomRadius: Int = 0
    private val defaultRadius: Int = 0
    private lateinit var mPath: Path

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        mPath = Path()
        // 读取配置
        val  array = context.obtainStyledAttributes(attrs, R.styleable.RoundAngleImageView);
        radius = array.getDimensionPixelOffset(
            R.styleable.RoundAngleImageView_radius,
            defaultRadius
        );
        leftTopRadius = array.getDimensionPixelOffset(
            R.styleable.RoundAngleImageView_left_top_radius,
            defaultRadius
        );
        rightTopRadius = array.getDimensionPixelOffset(
            R.styleable.RoundAngleImageView_right_top_radius,
            defaultRadius
        );
        rightBottomRadius = array.getDimensionPixelOffset(
            R.styleable.RoundAngleImageView_right_bottom_radius,
            defaultRadius
        );
        leftBottomRadius = array.getDimensionPixelOffset(
            R.styleable.RoundAngleImageView_left_bottom_radius,
            defaultRadius
        );



        //如果四个角的值没有设置，那么就使用通用的radius的值。
        if (defaultRadius == leftTopRadius) {
            leftTopRadius = radius;
        }
        if (defaultRadius == rightTopRadius) {
            rightTopRadius = radius;
        }
        if (defaultRadius == rightBottomRadius) {
            rightBottomRadius = radius;
        }
        if (defaultRadius == leftBottomRadius) {
            leftBottomRadius = radius;
        }
        array.recycle();

    }

    override fun onDraw(canvas: Canvas) {
        //这里做下判断，只有图片的宽高大于设置的圆角距离的时候才进行裁剪
        val maxLeft = max(leftTopRadius, leftBottomRadius)
        val maxRight = max(rightTopRadius, rightBottomRadius)
        val minWidth = maxLeft + maxRight
        val maxTop = max(leftTopRadius, rightTopRadius)
        val maxBottom = max(leftBottomRadius, rightBottomRadius)
        val minHeight = maxTop + maxBottom
        if (width >= minWidth && height > minHeight) {
            //四个角：右上，右下，左下，左上
            mPath.moveTo(leftTopRadius.toFloat(), 0f)
            mPath.lineTo(width - rightTopRadius.toFloat(), 0f)
            mPath.quadTo(width.toFloat(), 0f, width.toFloat(), rightTopRadius.toFloat())
            mPath.lineTo(width.toFloat(), height - rightBottomRadius.toFloat())
            mPath.quadTo(width.toFloat(), height.toFloat(), width - rightBottomRadius.toFloat(), height.toFloat())
            mPath.lineTo(leftBottomRadius.toFloat(), height.toFloat())
            mPath.quadTo(0f, height.toFloat(), 0f, height.toFloat() - leftBottomRadius)
            mPath.lineTo(0f, leftTopRadius.toFloat())
            mPath.quadTo(0f, 0f, leftTopRadius.toFloat(), 0f)
            canvas.clipPath(mPath)
        }
        super.onDraw(canvas)
    }

}