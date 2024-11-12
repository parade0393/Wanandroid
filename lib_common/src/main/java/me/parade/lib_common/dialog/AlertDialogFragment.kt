package me.parade.lib_common.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import me.parade.lib_common.R
import me.parade.lib_common.ext.px

/**
 * 提示性Dialog
 */
class AlertDialogFragment : BaseDialogFragment() {
    private var title: String? = null
    private var content: String? = null
    private var positiveButton: String? = "确定"
    private var negativeButton: String? = "取消"
    private var onPositiveClick: ((BaseDialogFragment) -> Unit)? = null
    private var onNegativeClick: (() -> Unit)? = null
    private var dimAmount = 0.5f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.public_dialog_alert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            findViewById<TextView>(R.id.tvTitle).text = title
            findViewById<TextView>(R.id.tvContent).text = content
            findViewById<TextView>(R.id.tvConfirm).apply {
                text = positiveButton
                setOnClickListener {
                    if (onPositiveClick != null) {
                        onPositiveClick?.invoke(this@AlertDialogFragment)
                    } else {
                        dismiss()
                    }
                }
            }
            findViewById<TextView>(R.id.tvCancel).apply {
                text = negativeButton
                setOnClickListener {
                    if (onNegativeClick != null) {
                        onNegativeClick?.invoke()
                    } else {
                        dismiss()
                    }
                }
            }
        }
    }

    override fun getDialogWidth() = WindowManager.LayoutParams.MATCH_PARENT
    override fun getDialogHeight() = WindowManager.LayoutParams.WRAP_CONTENT

    override fun getDialogHorizontalMargin() = 16.px

    class Builder : BaseDialogBuilder<Builder>() {
        private var content: String? = null
        private var positiveButton: String? = "确定"
        private var negativeButton: String? = "取消"
        private var onPositiveClick: ((BaseDialogFragment) -> Unit)? = null
        private var onNegativeClick: (() -> Unit)? = null

        fun setContent(content: String) = apply { this.content = content }
        fun setPositiveButton(text: String, onClick: () -> Unit) = apply {
            this.positiveButton = text
        }

        fun setPositiveClick(onClick: (dialog:BaseDialogFragment) -> Unit) = apply {
            this.onPositiveClick = onClick
        }

        fun setNegativeButton(text: String, onClick: () -> Unit) = apply {
            this.negativeButton = text
        }

        fun setNegativeClick(onClick: () -> Unit) = apply {
            this.onNegativeClick = onClick
        }

        override fun build(): AlertDialogFragment {
            return AlertDialogFragment().apply {
                content = this@Builder.content
                positiveButton = this@Builder.positiveButton
                negativeButton = this@Builder.negativeButton
                onPositiveClick = this@Builder.onPositiveClick
                onNegativeClick = this@Builder.onNegativeClick
                animStyle = this@Builder.animStyle
                title = this@Builder.title
                canClickOutCancel = this@Builder.cancelable
                dimAmount = this@Builder.dimAmount
            }
        }
    }
}