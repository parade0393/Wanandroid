package me.parade.lib_common.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import me.parade.lib_common.R
import me.parade.lib_common.ext.px
import me.parade.lib_common.utils.DownloadResult
import me.parade.lib_common.utils.FileSizeFormatter

class DownloadProgressDialogFragment : BaseDialogFragment() {

    private var showCancel: Boolean = true
    private var showConfirm: Boolean = true
    private var title: String? = null
    private var content: String? = null
    private var positiveButton: String? = "确定"
    private var negativeButton: String? = "取消"
    private var onPositiveClick: (() -> Unit)? = null
    private var onNegativeClick: (() -> Unit)? = null

    private lateinit var button_group: Group
    private lateinit var v_divider: View
    private lateinit var tvCancel: TextView
    private lateinit var tvConfirm: TextView
    private lateinit var v_vertical_divider: View
    private lateinit var progressBar:ProgressBar
    private lateinit var tvSize:TextView
    private lateinit var tvPercent:TextView

    override fun getDialogWidth() = WindowManager.LayoutParams.MATCH_PARENT
    override fun getDialogHeight() = WindowManager.LayoutParams.WRAP_CONTENT
    override fun getDialogHorizontalMargin() = 16.px

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.public_download_progress_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            findViewById<TextView>(R.id.tvTitle).text = title
            findViewById<TextView>(R.id.tvContent).text = content
            tvConfirm = findViewById<TextView>(R.id.tvConfirm)
            tvCancel = findViewById<TextView>(R.id.tvCancel)
            v_divider = findViewById(R.id.v_divider)
            v_vertical_divider = findViewById(R.id.v_vertical_divider)
            button_group = findViewById(R.id.button_group)
            progressBar = findViewById(R.id.pb_download)
            tvSize = findViewById(R.id.tvSize)
            tvPercent = findViewById(R.id.tvPercent)
            tvConfirm.apply {
                text = positiveButton
                setOnClickListener {
                    if (onPositiveClick != null) {
                        onPositiveClick?.invoke()
                    }
                    dismiss()
                }
            }
            tvCancel.apply {
                text = negativeButton
                setOnClickListener {
                    if (onNegativeClick != null) {
                        onNegativeClick?.invoke()
                    }
                    dismiss()
                }
            }


            updateButtonVisibility(showCancel, showConfirm)
        }
    }


    fun updateProgress(result: DownloadResult.Progress) {
        progressBar.progress = result.progress
        if (result.progress>=100){
            if (!showCancel && !showConfirm){
                dismissAllowingStateLoss()
            }
        }
        if (tvSize.text.isNullOrEmpty()){
            tvSize.text = FileSizeFormatter.formatSize(result.totalBytes)
        }
        @SuppressLint("SetTextI18n")
        tvPercent.text = "${FileSizeFormatter.formatSize(result.downloadedBytes)}/${FileSizeFormatter.formatSize(result.totalBytes)}"

    }

    fun updateConfirmText(content:String){
        tvConfirm.text = content
    }

    private fun updateButtonVisibility(showCancel: Boolean, showConfirm: Boolean) {
        // 显示按钮组
        button_group.visibility = if (showCancel || showConfirm) View.VISIBLE else View.GONE

        // 控制各个组件的显示
        v_divider.visibility = if (showCancel || showConfirm) View.VISIBLE else View.GONE
        tvCancel.visibility = if (showCancel) View.VISIBLE else View.GONE
        tvConfirm.visibility = if (showConfirm) View.VISIBLE else View.GONE

        // 只有当两个按钮都显示时才显示垂直分割线
        v_vertical_divider.visibility = if (showCancel && showConfirm) View.VISIBLE else View.GONE

        // 调整确认按钮的约束
        val params = tvConfirm.layoutParams as ConstraintLayout.LayoutParams
        if (showCancel) {
            params.startToEnd = v_vertical_divider.id
        } else {
            params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        }
        tvConfirm.layoutParams = params
    }

    class Builder : BaseDialogBuilder<Builder>() {

        private var content: String? = null
        private var positiveButton: String? = "确定"
        private var negativeButton: String? = "取消"
        private var onPositiveClick: (() -> Unit)? = null
        private var onNegativeClick: (() -> Unit)? = null
        override var cancelable = false
        private var showCancel: Boolean = true
        private var showConfirm: Boolean = true

        fun setContent(content: String) = apply { this.content = content }
        fun setPositiveButton(text: String, onClick: () -> Unit) = apply {
            this.positiveButton = text
        }

        fun setPositiveClick(onClick: () -> Unit) = apply {
            this.onPositiveClick = onClick
        }

        fun setNegativeButton(text: String, onClick: () -> Unit) = apply {
            this.negativeButton = text
        }

        fun setNegativeClick(onClick: () -> Unit) = apply {
            this.onNegativeClick = onClick
        }

        fun setShowCancel(isShow:Boolean) = apply {
            this.showCancel = isShow
        }

        fun setShowConfirm(isShow:Boolean) = apply {
            this.showConfirm = isShow
        }

        override fun build(): BaseDialogFragment {
            return DownloadProgressDialogFragment().apply {
                content = this@Builder.content
                positiveButton = this@Builder.positiveButton
                negativeButton = this@Builder.negativeButton
                onPositiveClick = this@Builder.onPositiveClick
                onNegativeClick = this@Builder.onNegativeClick
                animStyle = this@Builder.animStyle
                title = this@Builder.title
                showCancel = this@Builder.showCancel
                showConfirm = this@Builder.showConfirm
            }
        }

    }
}