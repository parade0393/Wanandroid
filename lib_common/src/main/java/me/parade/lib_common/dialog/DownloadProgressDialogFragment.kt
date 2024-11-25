package me.parade.lib_common.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import me.parade.lib_common.R
import me.parade.lib_common.ext.logd
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
    override var canClickOutCancel: Boolean = false

    private lateinit var tvCancel: TextView
    private lateinit var tvConfirm: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvSize: TextView
    private lateinit var tvPercent: TextView
    private lateinit var llBtn:LinearLayout
    private lateinit var vDivider:View

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
            tvConfirm = findViewById(R.id.tvConfirm)
            tvCancel = findViewById(R.id.tvCancel)
            progressBar = findViewById(R.id.pb_download)
            tvSize = findViewById(R.id.tvSize)
            tvPercent = findViewById(R.id.tvPercent)
            llBtn = findViewById(R.id.llBtn)
            vDivider = findViewById(R.id.v_divider)
            tvConfirm.apply {
                text = positiveButton
                setOnClickListener {
                    if (onPositiveClick != null) {
                        onPositiveClick?.invoke()
                    }
                }
            }
            tvCancel.apply {
                text = negativeButton
                setOnClickListener {
                    if (onNegativeClick != null) {
                        onNegativeClick?.invoke()
                    }
                    dismissAllowingStateLoss()
                }
            }


            updateButtonVisibility(showCancel, showConfirm)
        }
    }


    @SuppressLint("SetTextI18n")
    fun updateProgress(result: DownloadResult.Progress) {
        if (!isAdded) return
        view?.post{
            try {
                if (result.progress >= 100) {
                    if (!showCancel && !showConfirm) {
                        dismissAllowingStateLoss()
                        return@post
                    }
                }

                if (progressBar.progress != result.progress) {
                    progressBar.apply {
                        progress = result.progress
                        invalidate()
                    }
                }
                tvSize.apply {
                    text = "${result.progress}/%"
                    invalidate()
                }
                tvPercent.apply {
                    text = "${FileSizeFormatter.formatSize(result.downloadedBytes)}/${
                        FileSizeFormatter.formatSize(result.totalBytes)
                    }"
                    invalidate()
                }

                "${result.progress}/%".logd()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun updateConfirmText(content: String) {
        tvConfirm.text = content
    }

    fun isCanAutoDismiss():Boolean = !showCancel && !showConfirm

    private fun updateButtonVisibility(showCancel: Boolean, showConfirm: Boolean) {
        tvCancel.visibility = if (showCancel) View.VISIBLE else View.GONE
        tvConfirm.visibility = if (showConfirm) View.VISIBLE else View.GONE
        if (!showCancel && !showConfirm){
            llBtn.visibility = View.GONE
            vDivider.visibility = View.GONE
        }
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
            this.onPositiveClick = onClick
        }

        fun setNegativeButton(text: String, onClick: () -> Unit) = apply {
            this.negativeButton = text
            this.onNegativeClick = onClick
        }

        fun setShowCancel(isShow: Boolean) = apply {
            this.showCancel = isShow
        }

        fun setShowConfirm(isShow: Boolean) = apply {
            this.showConfirm = isShow
        }

        override fun build(): DownloadProgressDialogFragment {
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