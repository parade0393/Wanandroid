package me.parade.lib_common.view
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import me.parade.lib_common.ext.logd

class RefreshHeaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var progress = 0f
    private var rotationAnimator: ObjectAnimator? = null
    private var progressAnimator: ObjectAnimator? = null

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        paint.color = Color.BLUE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        "RefreshHeaderView--onDraw".logd()
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = 40f

        canvas.drawCircle(centerX, centerY, radius, paint)
        canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius,
            0f, progress * 360, false, paint)
    }

    fun updateProgress(progress: Float) {
        this.progress = progress
        invalidate()
    }

    fun startAnimation() {
        stopAnimation() // Ensure any existing animations are stopped

        rotationAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f).apply {
            duration = 5000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            start()
        }

        progressAnimator = ObjectAnimator.ofFloat(this, "progress", 0f, 1f).apply {
            duration = 5000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            start()
        }
    }

    fun stopAnimation() {
        rotationAnimator?.cancel()
        progressAnimator?.cancel()
        rotation = 0f
        progress = 0f
        invalidate()
    }

    // Setter method for the custom property "progress"
    fun setProgress(value: Float) {
        progress = value
        invalidate()
    }
}