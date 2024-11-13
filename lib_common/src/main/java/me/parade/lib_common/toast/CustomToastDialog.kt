package me.parade.lib_common.toast

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.parade.lib_common.R

/**
 * 自定义Toast
 */
class CustomToastDialog:DialogFragment() {
    private var icon: ImageView? = null
    private var rotateAnimation: Animation? = null
    companion object{
        private const val ARG_TYPE = "arg_type"
        private const val ARG_MESSAGE = "arg_message"
        private const val DEFAULT_DURATION = 2000L

        fun newInstance(type: ToastType, message: String): CustomToastDialog {
            return CustomToastDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_TYPE, type)
                    putString(ARG_MESSAGE, message)
                }
            }
        }
    }

    private var dismissJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.CustomToastDialog)
        retainInstance = true
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            window?.apply {
                val params = attributes
                params.gravity = Gravity.TOP
                setWindowAnimations(R.style.DialogAnimationFromTop)
                attributes = params
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.public_custom_toast,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = arguments?.getSerializable(ARG_TYPE) as ToastType
        val message = arguments?.getString(ARG_MESSAGE) ?: ""

        setupToastView(view, type, message)

        if (type != ToastType.LOADING) {
            startAutoDismissTimer()
        }
    }

    private fun setupToastView(view: View,type: ToastType, message: String) {
        icon = view.findViewById<ImageView>(R.id.ivIcon)
        val text = view.findViewById<TextView>(R.id.tvTitle)
        val container = view.findViewById<ConstraintLayout>(R.id.root)

        text.text = message

        when(type){
            ToastType.NORMAL->{
                icon?.setImageResource(R.drawable.public_icon_info_24px)
            }
            ToastType.SUCCESS -> {
                icon?.setImageResource(R.drawable.public_icon_success_24px)
            }
            ToastType.WARNING -> {
                icon?.setImageResource(R.drawable.public_icon_warning_24px)
            }
            ToastType.ERROR -> {
                icon?.setImageResource(R.drawable.public_icon_error_24px)
            }
            ToastType.LOADING -> {
                icon?.setImageResource(R.drawable.public_loading_drawable)
                startLoadingAnimation()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    private fun startAutoDismissTimer() {
        dismissJob = lifecycleScope.launch {
            delay(DEFAULT_DURATION)
            dismissAllowingStateLoss()
        }
    }

    override fun onStop() {
        super.onStop()
        dismissAllowingStateLoss()
        icon?.clearAnimation()
        dismissJob?.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissJob?.cancel()
        dismissJob = null
        icon?.clearAnimation()
        rotateAnimation = null
    }

    private fun startLoadingAnimation() {
        rotateAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.public_roate_loading)
        icon?.startAnimation(rotateAnimation)
    }
}