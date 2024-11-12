package me.parade.lib_common.dialog

import androidx.annotation.FloatRange

abstract class BaseDialogBuilder<T:BaseDialogBuilder<T>> {
    protected var title: String? = "提示"
    protected var animStyle: DialogAnimation = DialogAnimation.CENTER
    protected var cancelable: Boolean = true
    protected var dimAmount = 0.5f

    companion object{
        private const val DEFAULT_DIM_AMOUNT = 0.5f
    }

    @Suppress("UNCHECKED_CAST")
    fun setTitle(title: String): T {
        this.title = title
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    fun setAnimation(animation: DialogAnimation): T {
        this.animStyle = animation
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    fun setCancelable(cancelable: Boolean): T {
        this.cancelable = cancelable
        return this as T
    }

    // 设置背景透明度（0.0f - 1.0f）
    @Suppress("UNCHECKED_CAST")
    fun setDimAmount( @FloatRange(from = 0.0, to = 1.0) dim: Float): T {
        dimAmount = dim.coerceIn(0f, 1f)  // 确保值在0-1之间
        return this as T
    }

    abstract fun build(): BaseDialogFragment
}