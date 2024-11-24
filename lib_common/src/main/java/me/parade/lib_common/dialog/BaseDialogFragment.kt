package me.parade.lib_common.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import me.parade.lib_common.R

/**
 * 基础DialogFragment
 *
 * 生命周期：onCreateDialog() -> onCreateView() -> onViewCreated() -> onStart()
 *
 * onCreateDialog():
 * 在 Dialog 实例首次创建时调用
 * 此时 Dialog 的 Window 已经创建，但还未显示
 * 在此设置窗口属性可以避免闪烁，因为用户还看不到对话框
 * 特别适合设置那些一旦设置就不会改变的属性
 *
 * onStart():
 * 在 Dialog 即将显示给用户之前调用
 * 此时 View 已经完全创建并测量完成
 * 可以获取到准确的 View 尺寸信息
 * 如果需要基于 View 的尺寸来设置窗口属性，则必须在这里进行
 * 在此设置属性可能会造成轻微的闪烁，因为 Dialog 即将显示
 *
 * 为什么要同时设置两处 cancelable：
 * isCancelable：这是 DialogFragment 的属性，控制是否可以通过返回键或点击外部区域关闭 Fragment
 * setCanceledOnTouchOutside()：这是 Dialog 的属性，专门控制点击外部区域是否关闭对话框
 * setCancelable()：这是 Dialog 的属性，控制是否可以通过返回键关闭对话框
 *
 * 同时设置这些属性可以确保在所有情况下都能正确控制 Dialog 的取消行为
 *
 * 基于此Base新建dialog实现类时的步骤，①重写两个抽象方法确定宽高①通过onCreateView确定布局
 * ③通过Builder设置参数④在onViewCreated中根据参数更新布局
 */
abstract class BaseDialogFragment:DialogFragment() {
    protected open var animStyle: DialogAnimation = DialogAnimation.CENTER
    protected open var canClickOutCancel: Boolean = true  // 添加 cancelable 属性

    // 方案一：通过抽象方法让子类必须指定宽高
    protected abstract fun getDialogWidth(): Int
    protected abstract fun getDialogHeight(): Int

    // 为了更灵活，可以添加边距的控制
    protected open fun getDialogHorizontalMargin(): Int = 0
    protected open fun getDialogVerticalMargin(): Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置 DialogFragment 级别的 cancelable
        isCancelable = canClickOutCancel
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            // 设置 Dialog 窗口级别的 cancelable
            setCanceledOnTouchOutside(canClickOutCancel)
            setCancelable(canClickOutCancel)
            window?.apply {
                requestFeature(Window.FEATURE_NO_TITLE)
                // 设置基础窗口属性（不依赖View的属性）
                initBasicWindowAttributes(this)
            }
        }
    }

    override fun onStart() {
        super.onStart()// 设置可能依赖View尺寸的属性
        initLayoutDependentAttributes(dialog?.window)

    }

    /**
     * 初始化基础窗口属性（在onCreateDialog中调用）
     * 这些属性通常是固定的，不依赖于View的测量结果
     */
    private fun initBasicWindowAttributes(window: Window) {
        window.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            //设置窗口属性
            val params = attributes
//            params.width = WindowManager.LayoutParams.MATCH_PARENT
//            params.height = WindowManager.LayoutParams.WRAP_CONTENT

            params.gravity = when(animStyle){
                DialogAnimation.TOP -> Gravity.TOP
                DialogAnimation.CENTER,DialogAnimation.CENTER_UP,DialogAnimation.CENTER_DOWN -> Gravity.CENTER
                DialogAnimation.BOTTOM -> Gravity.BOTTOM
            }
            when(animStyle){
                DialogAnimation.TOP -> setWindowAnimations(R.style.DialogAnimationFromTop)
                DialogAnimation.CENTER -> setWindowAnimations(0)
                DialogAnimation.CENTER_UP -> setWindowAnimations(R.style.DialogAnimationFromTop)
                DialogAnimation.CENTER_DOWN -> setWindowAnimations(R.style.DialogAnimationFromBottom)
                DialogAnimation.BOTTOM -> setWindowAnimations(R.style.DialogAnimationFromBottom)
            }

            attributes = params
        }
    }


    private fun initLayoutDependentAttributes(window: Window?){
        window?.apply {
            // 获取屏幕信息
            val displayMetrics = resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val screenHeight = displayMetrics.heightPixels

            // 计算实际宽高
            val width = when (val w = getDialogWidth()) {
                WindowManager.LayoutParams.MATCH_PARENT -> screenWidth - (getDialogHorizontalMargin() * 2)
                WindowManager.LayoutParams.WRAP_CONTENT -> WindowManager.LayoutParams.WRAP_CONTENT
                else -> if (w < 0) WindowManager.LayoutParams.WRAP_CONTENT else w
            }

            val height = when (val h = getDialogHeight()) {
                WindowManager.LayoutParams.MATCH_PARENT -> screenHeight - (getDialogVerticalMargin() * 2)
                WindowManager.LayoutParams.WRAP_CONTENT -> WindowManager.LayoutParams.WRAP_CONTENT
                else -> if (h < 0) WindowManager.LayoutParams.WRAP_CONTENT else h
            }

            setLayout(width, height)

            // 应用边距
            if (getDialogHorizontalMargin() > 0 || getDialogVerticalMargin() > 0) {
                decorView.setPadding(
                    getDialogHorizontalMargin(),
                    getDialogVerticalMargin(),
                    getDialogHorizontalMargin(),
                    getDialogVerticalMargin()
                )
            }
        }
    }
}