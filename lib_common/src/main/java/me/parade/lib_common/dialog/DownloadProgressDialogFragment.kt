package me.parade.lib_common.dialog

import android.view.WindowManager

class DownloadProgressDialogFragment:BaseDialogFragment() {
    override fun getDialogWidth() = WindowManager.LayoutParams.MATCH_PARENT
    override fun getDialogHeight() = WindowManager.LayoutParams.WRAP_CONTENT
}