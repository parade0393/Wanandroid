package me.parade.lib_common.toast

import androidx.fragment.app.FragmentManager
import java.lang.ref.WeakReference

object ToastManager {
    private var currentToast: WeakReference<CustomToastDialog>? = null

    fun show(
        fragmentManager: FragmentManager,
        type: ToastType,
        message: String
    ) {
        // 当显示新 Toast 时关闭旧的
        currentToast?.get()?.dismissAllowingStateLoss()

        // 显示新的 Toast
        CustomToastDialog.newInstance(type, message).apply {
            currentToast = WeakReference(this)
            show(fragmentManager, "CustomToast")
        }
    }

    fun dismissLoading() {
        // 用户可能在任何时候调用这个方法
        currentToast?.get()?.dismissAllowingStateLoss()
        currentToast?.clear()
    }

    fun isShow() = currentToast?.get()?.dialog?.isShowing?:false
}