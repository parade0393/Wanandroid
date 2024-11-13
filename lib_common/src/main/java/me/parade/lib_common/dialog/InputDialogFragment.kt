package me.parade.lib_common.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import me.parade.lib_common.R
import me.parade.lib_common.ext.px

class InputDialogFragment:BaseDialogFragment() {

    private var title: String? = null
    private var hintText: String? = null
    private var content:String? = null
    private var positiveButton: String? = "确定"
    private var negativeButton: String? = "取消"
    private var onPositiveClick: ((String) -> Unit)? = null
    private var onNegativeClick: (() -> Unit)? = null

    override fun getDialogWidth() = WindowManager.LayoutParams.MATCH_PARENT

    override fun getDialogHeight() = WindowManager.LayoutParams.WRAP_CONTENT


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.public_dialog_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editText = view.findViewById<EditText>(R.id.etContent)
        view.apply {
            findViewById<TextView>(R.id.tvTitle).text = title
            findViewById<TextView>(R.id.etContent).apply {
                hint = hintText
                text = content
            }
            findViewById<TextView>(R.id.tvConfirm).apply {
                text = positiveButton
                setOnClickListener {
                    val eC = editText.text.toString()
                    if (eC.isNotBlank()){
                        if (onPositiveClick != null) {
                            dismiss()
                            onPositiveClick?.invoke(editText.text.toString())
                        } else {
                            dismiss()
                        }
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

    override fun getDialogHorizontalMargin() = 16.px

    class Builder : BaseDialogBuilder<Builder>() {
        private var content: String? = null
        private var positiveButton: String? = "确定"
        private var negativeButton: String? = "取消"
        private var onPositiveClick: ((String) -> Unit)? = null
        private var onNegativeClick: (() -> Unit)? = null
        private var hintText:String = "请输入···"

        fun setContent(content: String) = apply { this.content = content }
        fun setPositiveButton(text: String, onClick: () -> Unit) = apply {
            this.positiveButton = text
        }

        fun setPositiveClick(onClick: (content: String) -> Unit) = apply {
            this.onPositiveClick = onClick
        }

        fun setNegativeButton(text: String, onClick: () -> Unit) = apply {
            this.negativeButton = text
        }

        fun setNegativeClick(onClick: () -> Unit) = apply {
            this.onNegativeClick = onClick
        }

        fun setHintText(text:String) = apply {
            this.hintText = text
        }

        override fun build(): InputDialogFragment {
            return InputDialogFragment().apply {
                content = this@Builder.content
                positiveButton = this@Builder.positiveButton
                negativeButton = this@Builder.negativeButton
                onPositiveClick = this@Builder.onPositiveClick
                onNegativeClick = this@Builder.onNegativeClick
                animStyle = this@Builder.animStyle
                title = this@Builder.title
                canClickOutCancel = this@Builder.cancelable
                hintText = this@Builder.hintText
            }
        }
    }
}