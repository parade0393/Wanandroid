package me.parade.lib_common.utils

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import me.parade.lib_common.R

class PermissionDescDialogUtil private constructor() : DefaultLifecycleObserver {

    private val handler = Handler(Looper.getMainLooper())
    private var permissionPopup: PopupWindow? = null

    companion object {
        @Volatile
        private var instance: PermissionDescDialogUtil? = null

        fun getInstance(): PermissionDescDialogUtil =
            instance ?: synchronized(this) {
                instance ?: PermissionDescDialogUtil().also { instance = it }
            }
    }

    fun showPopupWindow(activity: Activity, message: String) {
        val decorView = activity.window.decorView as ViewGroup
        if (permissionPopup == null) {
            val contentView = LayoutInflater.from(activity)
                .inflate(R.layout.public_permission_description_popup, decorView, false)
            permissionPopup = PopupWindow(activity).apply {
                setContentView(contentView)
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
                animationStyle = android.R.style.Animation_Dialog
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                isTouchable = true
                isOutsideTouchable = true
            }
        }

        permissionPopup?.contentView?.findViewById<TextView>(R.id.tv_permission_description_message)?.text = message

        handler.postDelayed({
            permissionPopup?.showAtLocation(decorView, Gravity.TOP, 0, 0)
        }, 300)

        // 将 PermissionDescDialogUtil 添加为 Activity 的生命周期观察者
        if (activity is LifecycleOwner) {
            activity.lifecycle.addObserver(this)
        }
    }

    fun dismissPopupWindow() {
        permissionPopup?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    // 实现 DefaultLifecycleObserver 接口的方法
    override fun onDestroy(owner: LifecycleOwner) {
        dismissPopupWindow()
        permissionPopup = null
        instance = null
        owner.lifecycle.removeObserver(this)
    }
}